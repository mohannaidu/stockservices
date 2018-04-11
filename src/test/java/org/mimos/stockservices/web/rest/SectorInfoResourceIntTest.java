package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;

import org.mimos.stockservices.domain.SectorInfo;
import org.mimos.stockservices.repository.SectorInfoRepository;
import org.mimos.stockservices.service.SectorInfoService;
import org.mimos.stockservices.repository.search.SectorInfoSearchRepository;
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
 * Test class for the SectorInfoResource REST controller.
 *
 * @see SectorInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class SectorInfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private SectorInfoRepository sectorInfoRepository;

    @Autowired
    private SectorInfoService sectorInfoService;

    @Autowired
    private SectorInfoSearchRepository sectorInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSectorInfoMockMvc;

    private SectorInfo sectorInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SectorInfoResource sectorInfoResource = new SectorInfoResource(sectorInfoService);
        this.restSectorInfoMockMvc = MockMvcBuilders.standaloneSetup(sectorInfoResource)
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
    public static SectorInfo createEntity(EntityManager em) {
        SectorInfo sectorInfo = new SectorInfo()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE);
        return sectorInfo;
    }

    @Before
    public void initTest() {
        sectorInfoSearchRepository.deleteAll();
        sectorInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSectorInfo() throws Exception {
        int databaseSizeBeforeCreate = sectorInfoRepository.findAll().size();

        // Create the SectorInfo
        restSectorInfoMockMvc.perform(post("/api/sector-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectorInfo)))
            .andExpect(status().isCreated());

        // Validate the SectorInfo in the database
        List<SectorInfo> sectorInfoList = sectorInfoRepository.findAll();
        assertThat(sectorInfoList).hasSize(databaseSizeBeforeCreate + 1);
        SectorInfo testSectorInfo = sectorInfoList.get(sectorInfoList.size() - 1);
        assertThat(testSectorInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSectorInfo.getCode()).isEqualTo(DEFAULT_CODE);

        // Validate the SectorInfo in Elasticsearch
        SectorInfo sectorInfoEs = sectorInfoSearchRepository.findOne(testSectorInfo.getId());
        assertThat(sectorInfoEs).isEqualToIgnoringGivenFields(testSectorInfo);
    }

    @Test
    @Transactional
    public void createSectorInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sectorInfoRepository.findAll().size();

        // Create the SectorInfo with an existing ID
        sectorInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectorInfoMockMvc.perform(post("/api/sector-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectorInfo)))
            .andExpect(status().isBadRequest());

        // Validate the SectorInfo in the database
        List<SectorInfo> sectorInfoList = sectorInfoRepository.findAll();
        assertThat(sectorInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSectorInfos() throws Exception {
        // Initialize the database
        sectorInfoRepository.saveAndFlush(sectorInfo);

        // Get all the sectorInfoList
        restSectorInfoMockMvc.perform(get("/api/sector-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sectorInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getSectorInfo() throws Exception {
        // Initialize the database
        sectorInfoRepository.saveAndFlush(sectorInfo);

        // Get the sectorInfo
        restSectorInfoMockMvc.perform(get("/api/sector-infos/{id}", sectorInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sectorInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSectorInfo() throws Exception {
        // Get the sectorInfo
        restSectorInfoMockMvc.perform(get("/api/sector-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSectorInfo() throws Exception {
        // Initialize the database
        sectorInfoService.save(sectorInfo);

        int databaseSizeBeforeUpdate = sectorInfoRepository.findAll().size();

        // Update the sectorInfo
        SectorInfo updatedSectorInfo = sectorInfoRepository.findOne(sectorInfo.getId());
        // Disconnect from session so that the updates on updatedSectorInfo are not directly saved in db
        em.detach(updatedSectorInfo);
        updatedSectorInfo
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);

        restSectorInfoMockMvc.perform(put("/api/sector-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSectorInfo)))
            .andExpect(status().isOk());

        // Validate the SectorInfo in the database
        List<SectorInfo> sectorInfoList = sectorInfoRepository.findAll();
        assertThat(sectorInfoList).hasSize(databaseSizeBeforeUpdate);
        SectorInfo testSectorInfo = sectorInfoList.get(sectorInfoList.size() - 1);
        assertThat(testSectorInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSectorInfo.getCode()).isEqualTo(UPDATED_CODE);

        // Validate the SectorInfo in Elasticsearch
        SectorInfo sectorInfoEs = sectorInfoSearchRepository.findOne(testSectorInfo.getId());
        assertThat(sectorInfoEs).isEqualToIgnoringGivenFields(testSectorInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingSectorInfo() throws Exception {
        int databaseSizeBeforeUpdate = sectorInfoRepository.findAll().size();

        // Create the SectorInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSectorInfoMockMvc.perform(put("/api/sector-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sectorInfo)))
            .andExpect(status().isCreated());

        // Validate the SectorInfo in the database
        List<SectorInfo> sectorInfoList = sectorInfoRepository.findAll();
        assertThat(sectorInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSectorInfo() throws Exception {
        // Initialize the database
        sectorInfoService.save(sectorInfo);

        int databaseSizeBeforeDelete = sectorInfoRepository.findAll().size();

        // Get the sectorInfo
        restSectorInfoMockMvc.perform(delete("/api/sector-infos/{id}", sectorInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean sectorInfoExistsInEs = sectorInfoSearchRepository.exists(sectorInfo.getId());
        assertThat(sectorInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<SectorInfo> sectorInfoList = sectorInfoRepository.findAll();
        assertThat(sectorInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSectorInfo() throws Exception {
        // Initialize the database
        sectorInfoService.save(sectorInfo);

        // Search the sectorInfo
        restSectorInfoMockMvc.perform(get("/api/_search/sector-infos?query=id:" + sectorInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sectorInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SectorInfo.class);
        SectorInfo sectorInfo1 = new SectorInfo();
        sectorInfo1.setId(1L);
        SectorInfo sectorInfo2 = new SectorInfo();
        sectorInfo2.setId(sectorInfo1.getId());
        assertThat(sectorInfo1).isEqualTo(sectorInfo2);
        sectorInfo2.setId(2L);
        assertThat(sectorInfo1).isNotEqualTo(sectorInfo2);
        sectorInfo1.setId(null);
        assertThat(sectorInfo1).isNotEqualTo(sectorInfo2);
    }
}
