package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;
import org.mimos.stockservices.domain.UserPortfolio;
import org.mimos.stockservices.domain.StockInfo;
import org.mimos.stockservices.repository.UserPortfolioRepository;
import org.mimos.stockservices.repository.search.UserPortfolioSearchRepository;
import org.mimos.stockservices.service.StockPriceService;
import org.mimos.stockservices.service.UserPortfolioService;
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
 * Test class for the UserPortfolioResource REST controller.
 *
 * @see UserPortfolioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class UserPortfolioResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    @Autowired
    private UserPortfolioRepository userPortfolioRepository;

    @Autowired
    private UserPortfolioSearchRepository userPortfolioSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserPortfolioMockMvc;

    private UserPortfolio userPortfolio;
    
    @Autowired
    private StockPriceService stockPriceService;
    
    @Autowired
    private UserPortfolioService userPortfolioService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserPortfolioResource userPortfolioResource = new UserPortfolioResource(userPortfolioRepository, userPortfolioSearchRepository, stockPriceService, userPortfolioService);
        this.restUserPortfolioMockMvc = MockMvcBuilders.standaloneSetup(userPortfolioResource)
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
    public static UserPortfolio createEntity(EntityManager em) {
        UserPortfolio userPortfolio = new UserPortfolio()
            .username(DEFAULT_USERNAME);
        // Add required entity
        StockInfo stockInfo = StockInfoResourceIntTest.createEntity(em);
        em.persist(stockInfo);
        em.flush();
        userPortfolio.setStockInfo(stockInfo);
        return userPortfolio;
    }

    @Before
    public void initTest() {
        userPortfolioSearchRepository.deleteAll();
        userPortfolio = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserPortfolio() throws Exception {
        int databaseSizeBeforeCreate = userPortfolioRepository.findAll().size();

        // Create the UserPortfolio
        restUserPortfolioMockMvc.perform(post("/api/user-portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPortfolio)))
            .andExpect(status().isCreated());

        // Validate the UserPortfolio in the database
        List<UserPortfolio> userPortfolioList = userPortfolioRepository.findAll();
        assertThat(userPortfolioList).hasSize(databaseSizeBeforeCreate + 1);
        UserPortfolio testUserPortfolio = userPortfolioList.get(userPortfolioList.size() - 1);
        assertThat(testUserPortfolio.getUsername()).isEqualTo(DEFAULT_USERNAME);

        // Validate the UserPortfolio in Elasticsearch
        UserPortfolio userPortfolioEs = userPortfolioSearchRepository.findOne(testUserPortfolio.getId());
        assertThat(userPortfolioEs).isEqualToIgnoringGivenFields(testUserPortfolio);
    }

    @Test
    @Transactional
    public void createUserPortfolioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userPortfolioRepository.findAll().size();

        // Create the UserPortfolio with an existing ID
        userPortfolio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPortfolioMockMvc.perform(post("/api/user-portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPortfolio)))
            .andExpect(status().isBadRequest());

        // Validate the UserPortfolio in the database
        List<UserPortfolio> userPortfolioList = userPortfolioRepository.findAll();
        assertThat(userPortfolioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPortfolioRepository.findAll().size();
        // set the field null
        userPortfolio.setUsername(null);

        // Create the UserPortfolio, which fails.

        restUserPortfolioMockMvc.perform(post("/api/user-portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPortfolio)))
            .andExpect(status().isBadRequest());

        List<UserPortfolio> userPortfolioList = userPortfolioRepository.findAll();
        assertThat(userPortfolioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserPortfolios() throws Exception {
        // Initialize the database
        userPortfolioRepository.saveAndFlush(userPortfolio);

        // Get all the userPortfolioList
        restUserPortfolioMockMvc.perform(get("/api/user-portfolios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPortfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())));
    }

    @Test
    @Transactional
    public void getUserPortfolio() throws Exception {
        // Initialize the database
        userPortfolioRepository.saveAndFlush(userPortfolio);

        // Get the userPortfolio
        restUserPortfolioMockMvc.perform(get("/api/user-portfolios/{id}", userPortfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userPortfolio.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserPortfolio() throws Exception {
        // Get the userPortfolio
        restUserPortfolioMockMvc.perform(get("/api/user-portfolios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPortfolio() throws Exception {
        // Initialize the database
        userPortfolioRepository.saveAndFlush(userPortfolio);
        userPortfolioSearchRepository.save(userPortfolio);
        int databaseSizeBeforeUpdate = userPortfolioRepository.findAll().size();

        // Update the userPortfolio
        UserPortfolio updatedUserPortfolio = userPortfolioRepository.findOne(userPortfolio.getId());
        // Disconnect from session so that the updates on updatedUserPortfolio are not directly saved in db
        em.detach(updatedUserPortfolio);
        updatedUserPortfolio
            .username(UPDATED_USERNAME);

        restUserPortfolioMockMvc.perform(put("/api/user-portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserPortfolio)))
            .andExpect(status().isOk());

        // Validate the UserPortfolio in the database
        List<UserPortfolio> userPortfolioList = userPortfolioRepository.findAll();
        assertThat(userPortfolioList).hasSize(databaseSizeBeforeUpdate);
        UserPortfolio testUserPortfolio = userPortfolioList.get(userPortfolioList.size() - 1);
        assertThat(testUserPortfolio.getUsername()).isEqualTo(UPDATED_USERNAME);

        // Validate the UserPortfolio in Elasticsearch
        UserPortfolio userPortfolioEs = userPortfolioSearchRepository.findOne(testUserPortfolio.getId());
        assertThat(userPortfolioEs).isEqualToIgnoringGivenFields(testUserPortfolio);
    }

    @Test
    @Transactional
    public void updateNonExistingUserPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = userPortfolioRepository.findAll().size();

        // Create the UserPortfolio

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserPortfolioMockMvc.perform(put("/api/user-portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userPortfolio)))
            .andExpect(status().isCreated());

        // Validate the UserPortfolio in the database
        List<UserPortfolio> userPortfolioList = userPortfolioRepository.findAll();
        assertThat(userPortfolioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserPortfolio() throws Exception {
        // Initialize the database
        userPortfolioRepository.saveAndFlush(userPortfolio);
        userPortfolioSearchRepository.save(userPortfolio);
        int databaseSizeBeforeDelete = userPortfolioRepository.findAll().size();

        // Get the userPortfolio
        restUserPortfolioMockMvc.perform(delete("/api/user-portfolios/{id}", userPortfolio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userPortfolioExistsInEs = userPortfolioSearchRepository.exists(userPortfolio.getId());
        assertThat(userPortfolioExistsInEs).isFalse();

        // Validate the database is empty
        List<UserPortfolio> userPortfolioList = userPortfolioRepository.findAll();
        assertThat(userPortfolioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserPortfolio() throws Exception {
        // Initialize the database
        userPortfolioRepository.saveAndFlush(userPortfolio);
        userPortfolioSearchRepository.save(userPortfolio);

        // Search the userPortfolio
        restUserPortfolioMockMvc.perform(get("/api/_search/user-portfolios?query=id:" + userPortfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPortfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPortfolio.class);
        UserPortfolio userPortfolio1 = new UserPortfolio();
        userPortfolio1.setId(1L);
        UserPortfolio userPortfolio2 = new UserPortfolio();
        userPortfolio2.setId(userPortfolio1.getId());
        assertThat(userPortfolio1).isEqualTo(userPortfolio2);
        userPortfolio2.setId(2L);
        assertThat(userPortfolio1).isNotEqualTo(userPortfolio2);
        userPortfolio1.setId(null);
        assertThat(userPortfolio1).isNotEqualTo(userPortfolio2);
    }
}
