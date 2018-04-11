package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;
import org.mimos.stockservices.domain.IndicePrice;
import org.mimos.stockservices.domain.IndiceInfo;
import org.mimos.stockservices.repository.IndicePriceRepository;
import org.mimos.stockservices.service.IndiceInfoService;
import org.mimos.stockservices.service.IndicePriceService;
import org.mimos.stockservices.repository.search.IndicePriceSearchRepository;
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
 * Test class for the IndicePriceResource REST controller.
 *
 * @see IndicePriceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class IndicePriceResourceIntTest {

    private static final Double DEFAULT_OPEN = 1D;
    private static final Double UPDATED_OPEN = 2D;

    private static final Double DEFAULT_HIGH = 1D;
    private static final Double UPDATED_HIGH = 2D;

    private static final Double DEFAULT_LOW = 1D;
    private static final Double UPDATED_LOW = 2D;

    private static final Double DEFAULT_CHANGE = 1D;
    private static final Double UPDATED_CHANGE = 2D;

    private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_PERCENTAGE_CHANGE = 1D;
    private static final Double UPDATED_PERCENTAGE_CHANGE = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Integer DEFAULT_VOLUME = 1;
    private static final Integer UPDATED_VOLUME = 2;

    @Autowired
    private IndicePriceRepository indicePriceRepository;

    @Autowired
    private IndicePriceService indicePriceService;
    
    @Autowired
    private IndiceInfoService indiceInfoService;

    @Autowired
    private IndicePriceSearchRepository indicePriceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIndicePriceMockMvc;

    private IndicePrice indicePrice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndicePriceResource indicePriceResource = new IndicePriceResource(indicePriceService, indiceInfoService);
        this.restIndicePriceMockMvc = MockMvcBuilders.standaloneSetup(indicePriceResource)
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
    public static IndicePrice createEntity(EntityManager em) {
        IndicePrice indicePrice = new IndicePrice()
            .open(DEFAULT_OPEN)
            .high(DEFAULT_HIGH)
            .low(DEFAULT_LOW)
            .change(DEFAULT_CHANGE)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .percentageChange(DEFAULT_PERCENTAGE_CHANGE)
            .price(DEFAULT_PRICE)
            .volume(DEFAULT_VOLUME);
        // Add required entity
        IndiceInfo indiceInfo = IndiceInfoResourceIntTest.createEntity(em);
        em.persist(indiceInfo);
        em.flush();
        indicePrice.setIndiceInfo(indiceInfo);
        return indicePrice;
    }

    @Before
    public void initTest() {
        indicePriceSearchRepository.deleteAll();
        indicePrice = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndicePrice() throws Exception {
        int databaseSizeBeforeCreate = indicePriceRepository.findAll().size();

        // Create the IndicePrice
        restIndicePriceMockMvc.perform(post("/api/indice-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicePrice)))
            .andExpect(status().isCreated());

        // Validate the IndicePrice in the database
        List<IndicePrice> indicePriceList = indicePriceRepository.findAll();
        assertThat(indicePriceList).hasSize(databaseSizeBeforeCreate + 1);
        IndicePrice testIndicePrice = indicePriceList.get(indicePriceList.size() - 1);
        assertThat(testIndicePrice.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testIndicePrice.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testIndicePrice.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testIndicePrice.getChange()).isEqualTo(DEFAULT_CHANGE);
        assertThat(testIndicePrice.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testIndicePrice.getPercentageChange()).isEqualTo(DEFAULT_PERCENTAGE_CHANGE);
        assertThat(testIndicePrice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testIndicePrice.getVolume()).isEqualTo(DEFAULT_VOLUME);

        // Validate the IndicePrice in Elasticsearch
        IndicePrice indicePriceEs = indicePriceSearchRepository.findOne(testIndicePrice.getId());
        assertThat(indicePriceEs).isEqualToIgnoringGivenFields(testIndicePrice);
    }

    @Test
    @Transactional
    public void createIndicePriceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indicePriceRepository.findAll().size();

        // Create the IndicePrice with an existing ID
        indicePrice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndicePriceMockMvc.perform(post("/api/indice-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicePrice)))
            .andExpect(status().isBadRequest());

        // Validate the IndicePrice in the database
        List<IndicePrice> indicePriceList = indicePriceRepository.findAll();
        assertThat(indicePriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIndicePrices() throws Exception {
        // Initialize the database
        indicePriceRepository.saveAndFlush(indicePrice);

        // Get all the indicePriceList
        restIndicePriceMockMvc.perform(get("/api/indice-prices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indicePrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].percentageChange").value(hasItem(DEFAULT_PERCENTAGE_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)));
    }

    @Test
    @Transactional
    public void getIndicePrice() throws Exception {
        // Initialize the database
        indicePriceRepository.saveAndFlush(indicePrice);

        // Get the indicePrice
        restIndicePriceMockMvc.perform(get("/api/indice-prices/{id}", indicePrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(indicePrice.getId().intValue()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.doubleValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.doubleValue()))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.doubleValue()))
            .andExpect(jsonPath("$.change").value(DEFAULT_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
            .andExpect(jsonPath("$.percentageChange").value(DEFAULT_PERCENTAGE_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME));
    }

    @Test
    @Transactional
    public void getNonExistingIndicePrice() throws Exception {
        // Get the indicePrice
        restIndicePriceMockMvc.perform(get("/api/indice-prices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndicePrice() throws Exception {
        // Initialize the database
        indicePriceService.save(indicePrice);

        int databaseSizeBeforeUpdate = indicePriceRepository.findAll().size();

        // Update the indicePrice
        IndicePrice updatedIndicePrice = indicePriceRepository.findOne(indicePrice.getId());
        // Disconnect from session so that the updates on updatedIndicePrice are not directly saved in db
        em.detach(updatedIndicePrice);
        updatedIndicePrice
            .open(UPDATED_OPEN)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .change(UPDATED_CHANGE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .percentageChange(UPDATED_PERCENTAGE_CHANGE)
            .price(UPDATED_PRICE)
            .volume(UPDATED_VOLUME);

        restIndicePriceMockMvc.perform(put("/api/indice-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndicePrice)))
            .andExpect(status().isOk());

        // Validate the IndicePrice in the database
        List<IndicePrice> indicePriceList = indicePriceRepository.findAll();
        assertThat(indicePriceList).hasSize(databaseSizeBeforeUpdate);
        IndicePrice testIndicePrice = indicePriceList.get(indicePriceList.size() - 1);
        assertThat(testIndicePrice.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testIndicePrice.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testIndicePrice.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testIndicePrice.getChange()).isEqualTo(UPDATED_CHANGE);
        assertThat(testIndicePrice.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testIndicePrice.getPercentageChange()).isEqualTo(UPDATED_PERCENTAGE_CHANGE);
        assertThat(testIndicePrice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testIndicePrice.getVolume()).isEqualTo(UPDATED_VOLUME);

        // Validate the IndicePrice in Elasticsearch
        IndicePrice indicePriceEs = indicePriceSearchRepository.findOne(testIndicePrice.getId());
        assertThat(indicePriceEs).isEqualToIgnoringGivenFields(testIndicePrice);
    }

    @Test
    @Transactional
    public void updateNonExistingIndicePrice() throws Exception {
        int databaseSizeBeforeUpdate = indicePriceRepository.findAll().size();

        // Create the IndicePrice

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIndicePriceMockMvc.perform(put("/api/indice-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicePrice)))
            .andExpect(status().isCreated());

        // Validate the IndicePrice in the database
        List<IndicePrice> indicePriceList = indicePriceRepository.findAll();
        assertThat(indicePriceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIndicePrice() throws Exception {
        // Initialize the database
        indicePriceService.save(indicePrice);

        int databaseSizeBeforeDelete = indicePriceRepository.findAll().size();

        // Get the indicePrice
        restIndicePriceMockMvc.perform(delete("/api/indice-prices/{id}", indicePrice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean indicePriceExistsInEs = indicePriceSearchRepository.exists(indicePrice.getId());
        assertThat(indicePriceExistsInEs).isFalse();

        // Validate the database is empty
        List<IndicePrice> indicePriceList = indicePriceRepository.findAll();
        assertThat(indicePriceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIndicePrice() throws Exception {
        // Initialize the database
        indicePriceService.save(indicePrice);

        // Search the indicePrice
        restIndicePriceMockMvc.perform(get("/api/_search/indice-prices?query=id:" + indicePrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indicePrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].percentageChange").value(hasItem(DEFAULT_PERCENTAGE_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndicePrice.class);
        IndicePrice indicePrice1 = new IndicePrice();
        indicePrice1.setId(1L);
        IndicePrice indicePrice2 = new IndicePrice();
        indicePrice2.setId(indicePrice1.getId());
        assertThat(indicePrice1).isEqualTo(indicePrice2);
        indicePrice2.setId(2L);
        assertThat(indicePrice1).isNotEqualTo(indicePrice2);
        indicePrice1.setId(null);
        assertThat(indicePrice1).isNotEqualTo(indicePrice2);
    }
}
