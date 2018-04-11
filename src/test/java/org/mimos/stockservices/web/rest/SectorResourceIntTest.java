package org.mimos.stockservices.web.rest;

import org.mimos.stockservices.StockservicesApp;
import org.mimos.stockservices.service.SectorService;
import org.mimos.stockservices.service.StockPriceService;
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
 * Test class for the Sector REST controller.
 *
 * @see SectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StockservicesApp.class)
public class SectorResourceIntTest {

    private MockMvc restMockMvc;
    
    @Autowired
    private SectorService sectorService;
    
    @Autowired
    private StockPriceService stockPriceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        SectorResource sectorResource = new SectorResource(sectorService, stockPriceService);
        restMockMvc = MockMvcBuilders
            .standaloneSetup(sectorResource)
            .build();
    }

    /**
    * Test getDailySummary
    */
    @Test
    public void testGetDailySummary() throws Exception {
        restMockMvc.perform(get("/api/sector/get-daily-summary"))
            .andExpect(status().isOk());
    }

}
