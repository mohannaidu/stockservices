package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;

import org.mimos.stockservices.domain.IndiceInfo;
import org.mimos.stockservices.repository.IndiceInfoRepository;
import org.mimos.stockservices.service.IndiceInfoService;
import org.mimos.stockservices.repository.search.IndiceInfoSearchRepository;
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
 * Test class for the IndiceInfoResource REST controller.
 *
 * @see IndiceInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class IndiceInfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    @Autowired
    private IndiceInfoRepository indiceInfoRepository;

    @Autowired
    private IndiceInfoService indiceInfoService;

    @Autowired
    private IndiceInfoSearchRepository indiceInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIndiceInfoMockMvc;

    private IndiceInfo indiceInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndiceInfoResource indiceInfoResource = new IndiceInfoResource(indiceInfoService);
        this.restIndiceInfoMockMvc = MockMvcBuilders.standaloneSetup(indiceInfoResource)
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
    public static IndiceInfo createEntity(EntityManager em) {
        IndiceInfo indiceInfo = new IndiceInfo()
            .name(DEFAULT_NAME)
            .symbol(DEFAULT_SYMBOL);
        return indiceInfo;
    }

    @Before
    public void initTest() {
        indiceInfoSearchRepository.deleteAll();
        indiceInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndiceInfo() throws Exception {
        int databaseSizeBeforeCreate = indiceInfoRepository.findAll().size();

        // Create the IndiceInfo
        restIndiceInfoMockMvc.perform(post("/api/indice-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indiceInfo)))
            .andExpect(status().isCreated());

        // Validate the IndiceInfo in the database
        List<IndiceInfo> indiceInfoList = indiceInfoRepository.findAll();
        assertThat(indiceInfoList).hasSize(databaseSizeBeforeCreate + 1);
        IndiceInfo testIndiceInfo = indiceInfoList.get(indiceInfoList.size() - 1);
        assertThat(testIndiceInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIndiceInfo.getSymbol()).isEqualTo(DEFAULT_SYMBOL);

        // Validate the IndiceInfo in Elasticsearch
        IndiceInfo indiceInfoEs = indiceInfoSearchRepository.findOne(testIndiceInfo.getId());
        assertThat(indiceInfoEs).isEqualToIgnoringGivenFields(testIndiceInfo);
    }

    @Test
    @Transactional
    public void createIndiceInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indiceInfoRepository.findAll().size();

        // Create the IndiceInfo with an existing ID
        indiceInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndiceInfoMockMvc.perform(post("/api/indice-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indiceInfo)))
            .andExpect(status().isBadRequest());

        // Validate the IndiceInfo in the database
        List<IndiceInfo> indiceInfoList = indiceInfoRepository.findAll();
        assertThat(indiceInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIndiceInfos() throws Exception {
        // Initialize the database
        indiceInfoRepository.saveAndFlush(indiceInfo);

        // Get all the indiceInfoList
        restIndiceInfoMockMvc.perform(get("/api/indice-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indiceInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())));
    }

    @Test
    @Transactional
    public void getIndiceInfo() throws Exception {
        // Initialize the database
        indiceInfoRepository.saveAndFlush(indiceInfo);

        // Get the indiceInfo
        restIndiceInfoMockMvc.perform(get("/api/indice-infos/{id}", indiceInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(indiceInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIndiceInfo() throws Exception {
        // Get the indiceInfo
        restIndiceInfoMockMvc.perform(get("/api/indice-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndiceInfo() throws Exception {
        // Initialize the database
        indiceInfoService.save(indiceInfo);

        int databaseSizeBeforeUpdate = indiceInfoRepository.findAll().size();

        // Update the indiceInfo
        IndiceInfo updatedIndiceInfo = indiceInfoRepository.findOne(indiceInfo.getId());
        // Disconnect from session so that the updates on updatedIndiceInfo are not directly saved in db
        em.detach(updatedIndiceInfo);
        updatedIndiceInfo
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL);

        restIndiceInfoMockMvc.perform(put("/api/indice-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndiceInfo)))
            .andExpect(status().isOk());

        // Validate the IndiceInfo in the database
        List<IndiceInfo> indiceInfoList = indiceInfoRepository.findAll();
        assertThat(indiceInfoList).hasSize(databaseSizeBeforeUpdate);
        IndiceInfo testIndiceInfo = indiceInfoList.get(indiceInfoList.size() - 1);
        assertThat(testIndiceInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIndiceInfo.getSymbol()).isEqualTo(UPDATED_SYMBOL);

        // Validate the IndiceInfo in Elasticsearch
        IndiceInfo indiceInfoEs = indiceInfoSearchRepository.findOne(testIndiceInfo.getId());
        assertThat(indiceInfoEs).isEqualToIgnoringGivenFields(testIndiceInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingIndiceInfo() throws Exception {
        int databaseSizeBeforeUpdate = indiceInfoRepository.findAll().size();

        // Create the IndiceInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIndiceInfoMockMvc.perform(put("/api/indice-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indiceInfo)))
            .andExpect(status().isCreated());

        // Validate the IndiceInfo in the database
        List<IndiceInfo> indiceInfoList = indiceInfoRepository.findAll();
        assertThat(indiceInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIndiceInfo() throws Exception {
        // Initialize the database
        indiceInfoService.save(indiceInfo);

        int databaseSizeBeforeDelete = indiceInfoRepository.findAll().size();

        // Get the indiceInfo
        restIndiceInfoMockMvc.perform(delete("/api/indice-infos/{id}", indiceInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean indiceInfoExistsInEs = indiceInfoSearchRepository.exists(indiceInfo.getId());
        assertThat(indiceInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<IndiceInfo> indiceInfoList = indiceInfoRepository.findAll();
        assertThat(indiceInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIndiceInfo() throws Exception {
        // Initialize the database
        indiceInfoService.save(indiceInfo);

        // Search the indiceInfo
        restIndiceInfoMockMvc.perform(get("/api/_search/indice-infos?query=id:" + indiceInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indiceInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndiceInfo.class);
        IndiceInfo indiceInfo1 = new IndiceInfo();
        indiceInfo1.setId(1L);
        IndiceInfo indiceInfo2 = new IndiceInfo();
        indiceInfo2.setId(indiceInfo1.getId());
        assertThat(indiceInfo1).isEqualTo(indiceInfo2);
        indiceInfo2.setId(2L);
        assertThat(indiceInfo1).isNotEqualTo(indiceInfo2);
        indiceInfo1.setId(null);
        assertThat(indiceInfo1).isNotEqualTo(indiceInfo2);
    }
}
