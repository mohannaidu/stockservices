package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;

import org.mimos.stockservices.domain.StockInfo;
import org.mimos.stockservices.repository.StockInfoRepository;
import org.mimos.stockservices.repository.search.StockInfoSearchRepository;
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
import java.util.List;

import static org.mimos.stockservices.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockInfoResource REST controller.
 *
 * @see StockInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class StockInfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    @Autowired
    private StockInfoRepository stockInfoRepository;

    @Autowired
    private StockInfoSearchRepository stockInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockInfoMockMvc;

    private StockInfo stockInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockInfoResource stockInfoResource = new StockInfoResource(stockInfoRepository, stockInfoSearchRepository);
        this.restStockInfoMockMvc = MockMvcBuilders.standaloneSetup(stockInfoResource)
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
    public static StockInfo createEntity(EntityManager em) {
        StockInfo stockInfo = new StockInfo()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .symbol(DEFAULT_SYMBOL);
        return stockInfo;
    }

    @Before
    public void initTest() {
        stockInfoSearchRepository.deleteAll();
        stockInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockInfo() throws Exception {
        int databaseSizeBeforeCreate = stockInfoRepository.findAll().size();

        // Create the StockInfo
        restStockInfoMockMvc.perform(post("/api/stock-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInfo)))
            .andExpect(status().isCreated());

        // Validate the StockInfo in the database
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        assertThat(stockInfoList).hasSize(databaseSizeBeforeCreate + 1);
        StockInfo testStockInfo = stockInfoList.get(stockInfoList.size() - 1);
        assertThat(testStockInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStockInfo.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStockInfo.getSymbol()).isEqualTo(DEFAULT_SYMBOL);

        // Validate the StockInfo in Elasticsearch
        StockInfo stockInfoEs = stockInfoSearchRepository.findOne(testStockInfo.getId());
        assertThat(stockInfoEs).isEqualToIgnoringGivenFields(testStockInfo);
    }

    @Test
    @Transactional
    public void createStockInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockInfoRepository.findAll().size();

        // Create the StockInfo with an existing ID
        stockInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockInfoMockMvc.perform(post("/api/stock-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInfo)))
            .andExpect(status().isBadRequest());

        // Validate the StockInfo in the database
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        assertThat(stockInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockInfoRepository.findAll().size();
        // set the field null
        stockInfo.setSymbol(null);

        // Create the StockInfo, which fails.

        restStockInfoMockMvc.perform(post("/api/stock-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInfo)))
            .andExpect(status().isBadRequest());

        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        assertThat(stockInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockInfos() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);

        // Get all the stockInfoList
        restStockInfoMockMvc.perform(get("/api/stock-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())));
    }

    @Test
    @Transactional
    public void getStockInfo() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);

        // Get the stockInfo
        restStockInfoMockMvc.perform(get("/api/stock-infos/{id}", stockInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockInfo() throws Exception {
        // Get the stockInfo
        restStockInfoMockMvc.perform(get("/api/stock-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockInfo() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);
        stockInfoSearchRepository.save(stockInfo);
        int databaseSizeBeforeUpdate = stockInfoRepository.findAll().size();

        // Update the stockInfo
        StockInfo updatedStockInfo = stockInfoRepository.findOne(stockInfo.getId());
        // Disconnect from session so that the updates on updatedStockInfo are not directly saved in db
        em.detach(updatedStockInfo);
        updatedStockInfo
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .symbol(UPDATED_SYMBOL);

        restStockInfoMockMvc.perform(put("/api/stock-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStockInfo)))
            .andExpect(status().isOk());

        // Validate the StockInfo in the database
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        assertThat(stockInfoList).hasSize(databaseSizeBeforeUpdate);
        StockInfo testStockInfo = stockInfoList.get(stockInfoList.size() - 1);
        assertThat(testStockInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStockInfo.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStockInfo.getSymbol()).isEqualTo(UPDATED_SYMBOL);

        // Validate the StockInfo in Elasticsearch
        StockInfo stockInfoEs = stockInfoSearchRepository.findOne(testStockInfo.getId());
        assertThat(stockInfoEs).isEqualToIgnoringGivenFields(testStockInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingStockInfo() throws Exception {
        int databaseSizeBeforeUpdate = stockInfoRepository.findAll().size();

        // Create the StockInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockInfoMockMvc.perform(put("/api/stock-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockInfo)))
            .andExpect(status().isCreated());

        // Validate the StockInfo in the database
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        assertThat(stockInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStockInfo() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);
        stockInfoSearchRepository.save(stockInfo);
        int databaseSizeBeforeDelete = stockInfoRepository.findAll().size();

        // Get the stockInfo
        restStockInfoMockMvc.perform(delete("/api/stock-infos/{id}", stockInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean stockInfoExistsInEs = stockInfoSearchRepository.exists(stockInfo.getId());
        assertThat(stockInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<StockInfo> stockInfoList = stockInfoRepository.findAll();
        assertThat(stockInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStockInfo() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);
        stockInfoSearchRepository.save(stockInfo);

        // Search the stockInfo
        restStockInfoMockMvc.perform(get("/api/_search/stock-infos?query=id:" + stockInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockInfo.class);
        StockInfo stockInfo1 = new StockInfo();
        stockInfo1.setId(1L);
        StockInfo stockInfo2 = new StockInfo();
        stockInfo2.setId(stockInfo1.getId());
        assertThat(stockInfo1).isEqualTo(stockInfo2);
        stockInfo2.setId(2L);
        assertThat(stockInfo1).isNotEqualTo(stockInfo2);
        stockInfo1.setId(null);
        assertThat(stockInfo1).isNotEqualTo(stockInfo2);
    }
}
