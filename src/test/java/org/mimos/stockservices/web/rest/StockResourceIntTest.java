package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;
import org.mimos.stockservices.service.StockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the Stock REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class StockResourceIntTest {

    private MockMvc restMockMvc;
    
    @Autowired
    private StockService stockService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        StockResource stockResource = new StockResource(stockService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(stockResource)
            .build();
    }

    /**
    * Test defaultAction
    */
    @Test
    public void testDefaultAction() throws Exception {
        restMockMvc.perform(get("/api/stock/default-action"))
            .andExpect(status().isOk());
    }

}
