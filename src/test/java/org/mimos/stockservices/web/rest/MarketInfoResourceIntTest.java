package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;

import org.mimos.stockservices.domain.MarketInfo;
import org.mimos.stockservices.repository.MarketInfoRepository;
import org.mimos.stockservices.service.MarketInfoService;
import org.mimos.stockservices.repository.search.MarketInfoSearchRepository;
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
 * Test class for the MarketInfoResource REST controller.
 *
 * @see MarketInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class MarketInfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private MarketInfoRepository marketInfoRepository;

    @Autowired
    private MarketInfoService marketInfoService;

    @Autowired
    private MarketInfoSearchRepository marketInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMarketInfoMockMvc;

    private MarketInfo marketInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarketInfoResource marketInfoResource = new MarketInfoResource(marketInfoService);
        this.restMarketInfoMockMvc = MockMvcBuilders.standaloneSetup(marketInfoResource)
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
    public static MarketInfo createEntity(EntityManager em) {
        MarketInfo marketInfo = new MarketInfo()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE);
        return marketInfo;
    }

    @Before
    public void initTest() {
        marketInfoSearchRepository.deleteAll();
        marketInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarketInfo() throws Exception {
        int databaseSizeBeforeCreate = marketInfoRepository.findAll().size();

        // Create the MarketInfo
        restMarketInfoMockMvc.perform(post("/api/market-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketInfo)))
            .andExpect(status().isCreated());

        // Validate the MarketInfo in the database
        List<MarketInfo> marketInfoList = marketInfoRepository.findAll();
        assertThat(marketInfoList).hasSize(databaseSizeBeforeCreate + 1);
        MarketInfo testMarketInfo = marketInfoList.get(marketInfoList.size() - 1);
        assertThat(testMarketInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMarketInfo.getCode()).isEqualTo(DEFAULT_CODE);

        // Validate the MarketInfo in Elasticsearch
        MarketInfo marketInfoEs = marketInfoSearchRepository.findOne(testMarketInfo.getId());
        assertThat(marketInfoEs).isEqualToIgnoringGivenFields(testMarketInfo);
    }

    @Test
    @Transactional
    public void createMarketInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = marketInfoRepository.findAll().size();

        // Create the MarketInfo with an existing ID
        marketInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketInfoMockMvc.perform(post("/api/market-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketInfo)))
            .andExpect(status().isBadRequest());

        // Validate the MarketInfo in the database
        List<MarketInfo> marketInfoList = marketInfoRepository.findAll();
        assertThat(marketInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMarketInfos() throws Exception {
        // Initialize the database
        marketInfoRepository.saveAndFlush(marketInfo);

        // Get all the marketInfoList
        restMarketInfoMockMvc.perform(get("/api/market-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getMarketInfo() throws Exception {
        // Initialize the database
        marketInfoRepository.saveAndFlush(marketInfo);

        // Get the marketInfo
        restMarketInfoMockMvc.perform(get("/api/market-infos/{id}", marketInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marketInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMarketInfo() throws Exception {
        // Get the marketInfo
        restMarketInfoMockMvc.perform(get("/api/market-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarketInfo() throws Exception {
        // Initialize the database
        marketInfoService.save(marketInfo);

        int databaseSizeBeforeUpdate = marketInfoRepository.findAll().size();

        // Update the marketInfo
        MarketInfo updatedMarketInfo = marketInfoRepository.findOne(marketInfo.getId());
        // Disconnect from session so that the updates on updatedMarketInfo are not directly saved in db
        em.detach(updatedMarketInfo);
        updatedMarketInfo
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);

        restMarketInfoMockMvc.perform(put("/api/market-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMarketInfo)))
            .andExpect(status().isOk());

        // Validate the MarketInfo in the database
        List<MarketInfo> marketInfoList = marketInfoRepository.findAll();
        assertThat(marketInfoList).hasSize(databaseSizeBeforeUpdate);
        MarketInfo testMarketInfo = marketInfoList.get(marketInfoList.size() - 1);
        assertThat(testMarketInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMarketInfo.getCode()).isEqualTo(UPDATED_CODE);

        // Validate the MarketInfo in Elasticsearch
        MarketInfo marketInfoEs = marketInfoSearchRepository.findOne(testMarketInfo.getId());
        assertThat(marketInfoEs).isEqualToIgnoringGivenFields(testMarketInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingMarketInfo() throws Exception {
        int databaseSizeBeforeUpdate = marketInfoRepository.findAll().size();

        // Create the MarketInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMarketInfoMockMvc.perform(put("/api/market-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketInfo)))
            .andExpect(status().isCreated());

        // Validate the MarketInfo in the database
        List<MarketInfo> marketInfoList = marketInfoRepository.findAll();
        assertThat(marketInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMarketInfo() throws Exception {
        // Initialize the database
        marketInfoService.save(marketInfo);

        int databaseSizeBeforeDelete = marketInfoRepository.findAll().size();

        // Get the marketInfo
        restMarketInfoMockMvc.perform(delete("/api/market-infos/{id}", marketInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean marketInfoExistsInEs = marketInfoSearchRepository.exists(marketInfo.getId());
        assertThat(marketInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<MarketInfo> marketInfoList = marketInfoRepository.findAll();
        assertThat(marketInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMarketInfo() throws Exception {
        // Initialize the database
        marketInfoService.save(marketInfo);

        // Search the marketInfo
        restMarketInfoMockMvc.perform(get("/api/_search/market-infos?query=id:" + marketInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketInfo.class);
        MarketInfo marketInfo1 = new MarketInfo();
        marketInfo1.setId(1L);
        MarketInfo marketInfo2 = new MarketInfo();
        marketInfo2.setId(marketInfo1.getId());
        assertThat(marketInfo1).isEqualTo(marketInfo2);
        marketInfo2.setId(2L);
        assertThat(marketInfo1).isNotEqualTo(marketInfo2);
        marketInfo1.setId(null);
        assertThat(marketInfo1).isNotEqualTo(marketInfo2);
    }
}
