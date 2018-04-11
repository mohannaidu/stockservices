package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;

import org.mimos.stockservices.domain.StockPrice;
import org.mimos.stockservices.domain.StockInfo;
import org.mimos.stockservices.repository.StockPriceRepository;
import org.mimos.stockservices.repository.search.StockPriceSearchRepository;
import org.mimos.stockservices.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.mimos.stockservices.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockPriceResource REST controller.
 *
 * @see StockPriceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class StockPriceResourceIntTest {

    private static final Double DEFAULT_OPENING = 1D;
    private static final Double UPDATED_OPENING = 2D;

    private static final Double DEFAULT_CLOSING = 1D;
    private static final Double UPDATED_CLOSING = 2D;

    private static final Double DEFAULT_VOLUME = 1D;
    private static final Double UPDATED_VOLUME = 2D;

    private static final Double DEFAULT_HIGH = 1D;
    private static final Double UPDATED_HIGH = 2D;

    private static final Double DEFAULT_LOW = 1D;
    private static final Double UPDATED_LOW = 2D;

    private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_CHANGE = 1D;
    private static final Double UPDATED_CHANGE = 2D;

    private static final Double DEFAULT_PERCENTAGE_CHANGE = 1D;
    private static final Double UPDATED_PERCENTAGE_CHANGE = 2D;

    @Autowired
    private StockPriceRepository stockPriceRepository;

    @Autowired
    private StockPriceSearchRepository stockPriceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockPriceMockMvc;

    private StockPrice stockPrice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockPriceResource stockPriceResource = new StockPriceResource(stockPriceRepository, stockPriceSearchRepository);
        this.restStockPriceMockMvc = MockMvcBuilders.standaloneSetup(stockPriceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockPrice createEntity(EntityManager em) {
        StockPrice stockPrice = new StockPrice()
            .opening(DEFAULT_OPENING)
            .closing(DEFAULT_CLOSING)
            .volume(DEFAULT_VOLUME)
            .high(DEFAULT_HIGH)
            .low(DEFAULT_LOW)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .change(DEFAULT_CHANGE)
            .percentageChange(DEFAULT_PERCENTAGE_CHANGE);
        // Add required entity
        StockInfo stockInfo = StockInfoResourceIntTest.createEntity(em);
        em.persist(stockInfo);
        em.flush();
        stockPrice.setStockInfo(stockInfo);
        return stockPrice;
    }

    @Before
    public void initTest() {
        stockPriceSearchRepository.deleteAll();
        stockPrice = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockPrice() throws Exception {
        int databaseSizeBeforeCreate = stockPriceRepository.findAll().size();

        // Create the StockPrice
        restStockPriceMockMvc.perform(post("/api/stock-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockPrice)))
            .andExpect(status().isCreated());

        // Validate the StockPrice in the database
        List<StockPrice> stockPriceList = stockPriceRepository.findAll();
        assertThat(stockPriceList).hasSize(databaseSizeBeforeCreate + 1);
        StockPrice testStockPrice = stockPriceList.get(stockPriceList.size() - 1);
        assertThat(testStockPrice.getOpening()).isEqualTo(DEFAULT_OPENING);
        assertThat(testStockPrice.getClosing()).isEqualTo(DEFAULT_CLOSING);
        assertThat(testStockPrice.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testStockPrice.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testStockPrice.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testStockPrice.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testStockPrice.getChange()).isEqualTo(DEFAULT_CHANGE);
        assertThat(testStockPrice.getPercentageChange()).isEqualTo(DEFAULT_PERCENTAGE_CHANGE);

        // Validate the StockPrice in Elasticsearch
        StockPrice stockPriceEs = stockPriceSearchRepository.findOne(testStockPrice.getId());
        assertThat(stockPriceEs).isEqualToIgnoringGivenFields(testStockPrice);
    }

    @Test
    @Transactional
    public void createStockPriceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockPriceRepository.findAll().size();

        // Create the StockPrice with an existing ID
        stockPrice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockPriceMockMvc.perform(post("/api/stock-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockPrice)))
            .andExpect(status().isBadRequest());

        // Validate the StockPrice in the database
        List<StockPrice> stockPriceList = stockPriceRepository.findAll();
        assertThat(stockPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStockPrices() throws Exception {
        // Initialize the database
        stockPriceRepository.saveAndFlush(stockPrice);

        // Get all the stockPriceList
        restStockPriceMockMvc.perform(get("/api/stock-prices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].opening").value(hasItem(DEFAULT_OPENING.doubleValue())))
            .andExpect(jsonPath("$.[*].closing").value(hasItem(DEFAULT_CLOSING.doubleValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].percentageChange").value(hasItem(DEFAULT_PERCENTAGE_CHANGE.doubleValue())));
    }

    @Test
    @Transactional
    public void getStockPrice() throws Exception {
        // Initialize the database
        stockPriceRepository.saveAndFlush(stockPrice);

        // Get the stockPrice
        restStockPriceMockMvc.perform(get("/api/stock-prices/{id}", stockPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockPrice.getId().intValue()))
            .andExpect(jsonPath("$.opening").value(DEFAULT_OPENING.doubleValue()))
            .andExpect(jsonPath("$.closing").value(DEFAULT_CLOSING.doubleValue()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME.doubleValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.doubleValue()))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.doubleValue()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
            .andExpect(jsonPath("$.change").value(DEFAULT_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.percentageChange").value(DEFAULT_PERCENTAGE_CHANGE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockPrice() throws Exception {
        // Get the stockPrice
        restStockPriceMockMvc.perform(get("/api/stock-prices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockPrice() throws Exception {
        // Initialize the database
        stockPriceRepository.saveAndFlush(stockPrice);
        stockPriceSearchRepository.save(stockPrice);
        int databaseSizeBeforeUpdate = stockPriceRepository.findAll().size();

        // Update the stockPrice
        StockPrice updatedStockPrice = stockPriceRepository.findOne(stockPrice.getId());
        // Disconnect from session so that the updates on updatedStockPrice are not directly saved in db
        em.detach(updatedStockPrice);
        updatedStockPrice
            .opening(UPDATED_OPENING)
            .closing(UPDATED_CLOSING)
            .volume(UPDATED_VOLUME)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .publishDate(UPDATED_PUBLISH_DATE)
            .change(UPDATED_CHANGE)
            .percentageChange(UPDATED_PERCENTAGE_CHANGE);

        restStockPriceMockMvc.perform(put("/api/stock-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStockPrice)))
            .andExpect(status().isOk());

        // Validate the StockPrice in the database
        List<StockPrice> stockPriceList = stockPriceRepository.findAll();
        assertThat(stockPriceList).hasSize(databaseSizeBeforeUpdate);
        StockPrice testStockPrice = stockPriceList.get(stockPriceList.size() - 1);
        assertThat(testStockPrice.getOpening()).isEqualTo(UPDATED_OPENING);
        assertThat(testStockPrice.getClosing()).isEqualTo(UPDATED_CLOSING);
        assertThat(testStockPrice.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testStockPrice.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testStockPrice.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testStockPrice.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testStockPrice.getChange()).isEqualTo(UPDATED_CHANGE);
        assertThat(testStockPrice.getPercentageChange()).isEqualTo(UPDATED_PERCENTAGE_CHANGE);

        // Validate the StockPrice in Elasticsearch
        StockPrice stockPriceEs = stockPriceSearchRepository.findOne(testStockPrice.getId());
        assertThat(stockPriceEs).isEqualToIgnoringGivenFields(testStockPrice);
    }

    @Test
    @Transactional
    public void updateNonExistingStockPrice() throws Exception {
        int databaseSizeBeforeUpdate = stockPriceRepository.findAll().size();

        // Create the StockPrice

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockPriceMockMvc.perform(put("/api/stock-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockPrice)))
            .andExpect(status().isCreated());

        // Validate the StockPrice in the database
        List<StockPrice> stockPriceList = stockPriceRepository.findAll();
        assertThat(stockPriceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockPrice() throws Exception {
        // Initialize the database
        stockPriceRepository.saveAndFlush(stockPrice);
        stockPriceSearchRepository.save(stockPrice);
        int databaseSizeBeforeDelete = stockPriceRepository.findAll().size();

        // Get the stockPrice
        restStockPriceMockMvc.perform(delete("/api/stock-prices/{id}", stockPrice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean stockPriceExistsInEs = stockPriceSearchRepository.exists(stockPrice.getId());
        assertThat(stockPriceExistsInEs).isFalse();

        // Validate the database is empty
        List<StockPrice> stockPriceList = stockPriceRepository.findAll();
        assertThat(stockPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStockPrice() throws Exception {
        // Initialize the database
        stockPriceRepository.saveAndFlush(stockPrice);
        stockPriceSearchRepository.save(stockPrice);

        // Search the stockPrice
        restStockPriceMockMvc.perform(get("/api/_search/stock-prices?query=id:" + stockPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].opening").value(hasItem(DEFAULT_OPENING.doubleValue())))
            .andExpect(jsonPath("$.[*].closing").value(hasItem(DEFAULT_CLOSING.doubleValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].percentageChange").value(hasItem(DEFAULT_PERCENTAGE_CHANGE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockPrice.class);
        StockPrice stockPrice1 = new StockPrice();
        stockPrice1.setId(1L);
        StockPrice stockPrice2 = new StockPrice();
        stockPrice2.setId(stockPrice1.getId());
        assertThat(stockPrice1).isEqualTo(stockPrice2);
        stockPrice2.setId(2L);
        assertThat(stockPrice1).isNotEqualTo(stockPrice2);
        stockPrice1.setId(null);
        assertThat(stockPrice1).isNotEqualTo(stockPrice2);
    }
}
