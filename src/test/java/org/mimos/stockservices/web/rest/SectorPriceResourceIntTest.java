package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;

import org.mimos.stockservices.domain.SectorPrice;
import org.mimos.stockservices.repository.SectorPriceRepository;
import org.mimos.stockservices.service.SectorPriceService;
import org.mimos.stockservices.repository.search.SectorPriceSearchRepository;
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
 * Test class for the SectorPriceResource REST controller.
 *
 * @see SectorPriceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class SectorPriceResourceIntTest {

    private static final Double DEFAULT_OPENING = 1D;
    private static final Double UPDATED_OPENING = 2D;

    private static final Double DEFAULT_CLOSING = 1D;
    private static final Double UPDATED_CLOSING = 2D;

    private static final Double DEFAULT_HIGH = 1D;
    private static final Double UPDATED_HIGH = 2D;

    private static final Double DEFAULT_LOW = 1D;
    private static final Double UPDATED_LOW = 2D;

    private static final Double DEFAULT_CHANGE = 1D;
    private static final Double UPDATED_CHANGE = 2D;

    private static final Double DEFAULT_PERCENTAGE_CHANGE = 1D;
    private static final Double UPDATED_PERCENTAGE_CHANGE = 2D;

    private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SectorPriceRepository sectorPriceRepository;

    @Autowired
    private SectorPriceService sectorPriceService;

    @Autowired
    private SectorPriceSearchRepository sectorPriceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSectorPriceMockMvc;

    private SectorPrice sectorPrice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SectorPriceResource sectorPriceResource = new SectorPriceResource(sectorPriceService);
        this.restSectorPriceMockMvc = MockMvcBuilders.standaloneSetup(sectorPriceResource)
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
    public static SectorPrice createEntity(EntityManager em) {
        SectorPrice sectorPrice = new SectorPrice()
            .opening(DEFAULT_OPENING)
            .closing(DEFAULT_CLOSING)
            .high(DEFAULT_HIGH)
            .low(DEFAULT_LOW)
            .change(DEFAULT_CHANGE)
            .percentageChange(DEFAULT_PERCENTAGE_CHANGE)
            .publishDate(DEFAULT_PUBLISH_DATE);
        return sectorPrice;
    }

    @Before
    public void initTest() {
        sectorPriceSearchRepository.deleteAll();
        sectorPrice = createEntity(em);
    }

    @Test
    @Transactional
    public void createSectorPrice() throws Exception {
        int databaseSizeBeforeCreate = sectorPriceRepository.findAll().size();

        // Create the SectorPrice
        restSectorPriceMockMvc.perform(post("/api/sector-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectorPrice)))
            .andExpect(status().isCreated());

        // Validate the SectorPrice in the database
        List<SectorPrice> sectorPriceList = sectorPriceRepository.findAll();
        assertThat(sectorPriceList).hasSize(databaseSizeBeforeCreate + 1);
        SectorPrice testSectorPrice = sectorPriceList.get(sectorPriceList.size() - 1);
        assertThat(testSectorPrice.getOpening()).isEqualTo(DEFAULT_OPENING);
        assertThat(testSectorPrice.getClosing()).isEqualTo(DEFAULT_CLOSING);
        assertThat(testSectorPrice.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testSectorPrice.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testSectorPrice.getChange()).isEqualTo(DEFAULT_CHANGE);
        assertThat(testSectorPrice.getPercentageChange()).isEqualTo(DEFAULT_PERCENTAGE_CHANGE);
        assertThat(testSectorPrice.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);

        // Validate the SectorPrice in Elasticsearch
        SectorPrice sectorPriceEs = sectorPriceSearchRepository.findOne(testSectorPrice.getId());
        assertThat(sectorPriceEs).isEqualToIgnoringGivenFields(testSectorPrice);
    }

    @Test
    @Transactional
    public void createSectorPriceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sectorPriceRepository.findAll().size();

        // Create the SectorPrice with an existing ID
        sectorPrice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectorPriceMockMvc.perform(post("/api/sector-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectorPrice)))
            .andExpect(status().isBadRequest());

        // Validate the SectorPrice in the database
        List<SectorPrice> sectorPriceList = sectorPriceRepository.findAll();
        assertThat(sectorPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSectorPrices() throws Exception {
        // Initialize the database
        sectorPriceRepository.saveAndFlush(sectorPrice);

        // Get all the sectorPriceList
        restSectorPriceMockMvc.perform(get("/api/sector-prices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sectorPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].opening").value(hasItem(DEFAULT_OPENING.doubleValue())))
            .andExpect(jsonPath("$.[*].closing").value(hasItem(DEFAULT_CLOSING.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].percentageChange").value(hasItem(DEFAULT_PERCENTAGE_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSectorPrice() throws Exception {
        // Initialize the database
        sectorPriceRepository.saveAndFlush(sectorPrice);

        // Get the sectorPrice
        restSectorPriceMockMvc.perform(get("/api/sector-prices/{id}", sectorPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sectorPrice.getId().intValue()))
            .andExpect(jsonPath("$.opening").value(DEFAULT_OPENING.doubleValue()))
            .andExpect(jsonPath("$.closing").value(DEFAULT_CLOSING.doubleValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.doubleValue()))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.doubleValue()))
            .andExpect(jsonPath("$.change").value(DEFAULT_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.percentageChange").value(DEFAULT_PERCENTAGE_CHANGE.doubleValue()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSectorPrice() throws Exception {
        // Get the sectorPrice
        restSectorPriceMockMvc.perform(get("/api/sector-prices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSectorPrice() throws Exception {
        // Initialize the database
        sectorPriceService.save(sectorPrice);

        int databaseSizeBeforeUpdate = sectorPriceRepository.findAll().size();

        // Update the sectorPrice
        SectorPrice updatedSectorPrice = sectorPriceRepository.findOne(sectorPrice.getId());
        // Disconnect from session so that the updates on updatedSectorPrice are not directly saved in db
        em.detach(updatedSectorPrice);
        updatedSectorPrice
            .opening(UPDATED_OPENING)
            .closing(UPDATED_CLOSING)
            .high(UPDATED_HIGH)
            .low(UPDATED_LOW)
            .change(UPDATED_CHANGE)
            .percentageChange(UPDATED_PERCENTAGE_CHANGE)
            .publishDate(UPDATED_PUBLISH_DATE);

        restSectorPriceMockMvc.perform(put("/api/sector-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSectorPrice)))
            .andExpect(status().isOk());

        // Validate the SectorPrice in the database
        List<SectorPrice> sectorPriceList = sectorPriceRepository.findAll();
        assertThat(sectorPriceList).hasSize(databaseSizeBeforeUpdate);
        SectorPrice testSectorPrice = sectorPriceList.get(sectorPriceList.size() - 1);
        assertThat(testSectorPrice.getOpening()).isEqualTo(UPDATED_OPENING);
        assertThat(testSectorPrice.getClosing()).isEqualTo(UPDATED_CLOSING);
        assertThat(testSectorPrice.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testSectorPrice.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testSectorPrice.getChange()).isEqualTo(UPDATED_CHANGE);
        assertThat(testSectorPrice.getPercentageChange()).isEqualTo(UPDATED_PERCENTAGE_CHANGE);
        assertThat(testSectorPrice.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);

        // Validate the SectorPrice in Elasticsearch
        SectorPrice sectorPriceEs = sectorPriceSearchRepository.findOne(testSectorPrice.getId());
        assertThat(sectorPriceEs).isEqualToIgnoringGivenFields(testSectorPrice);
    }

    @Test
    @Transactional
    public void updateNonExistingSectorPrice() throws Exception {
        int databaseSizeBeforeUpdate = sectorPriceRepository.findAll().size();

        // Create the SectorPrice

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSectorPriceMockMvc.perform(put("/api/sector-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectorPrice)))
            .andExpect(status().isCreated());

        // Validate the SectorPrice in the database
        List<SectorPrice> sectorPriceList = sectorPriceRepository.findAll();
        assertThat(sectorPriceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSectorPrice() throws Exception {
        // Initialize the database
        sectorPriceService.save(sectorPrice);

        int databaseSizeBeforeDelete = sectorPriceRepository.findAll().size();

        // Get the sectorPrice
        restSectorPriceMockMvc.perform(delete("/api/sector-prices/{id}", sectorPrice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sectorPriceExistsInEs = sectorPriceSearchRepository.exists(sectorPrice.getId());
        assertThat(sectorPriceExistsInEs).isFalse();

        // Validate the database is empty
        List<SectorPrice> sectorPriceList = sectorPriceRepository.findAll();
        assertThat(sectorPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSectorPrice() throws Exception {
        // Initialize the database
        sectorPriceService.save(sectorPrice);

        // Search the sectorPrice
        restSectorPriceMockMvc.perform(get("/api/_search/sector-prices?query=id:" + sectorPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sectorPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].opening").value(hasItem(DEFAULT_OPENING.doubleValue())))
            .andExpect(jsonPath("$.[*].closing").value(hasItem(DEFAULT_CLOSING.doubleValue())))
            .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
            .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].percentageChange").value(hasItem(DEFAULT_PERCENTAGE_CHANGE.doubleValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SectorPrice.class);
        SectorPrice sectorPrice1 = new SectorPrice();
        sectorPrice1.setId(1L);
        SectorPrice sectorPrice2 = new SectorPrice();
        sectorPrice2.setId(sectorPrice1.getId());
        assertThat(sectorPrice1).isEqualTo(sectorPrice2);
        sectorPrice2.setId(2L);
        assertThat(sectorPrice1).isNotEqualTo(sectorPrice2);
        sectorPrice1.setId(null);
        assertThat(sectorPrice1).isNotEqualTo(sectorPrice2);
    }
}
