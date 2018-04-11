package org.mimos.stockservices.web.rest;

import java.time.LocalDate;
import java.util.Map;

import org.mimos.stockservices.domain.StockPrice;
import org.mimos.stockservices.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * Stock controller
 */
@RestController
@RequestMapping("/api/stocks")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private final StockService stockService;
    
    public StockResource(StockService stockService) {
		this.stockService = stockService;
	}
    
    /**
     * 
     * 
     * @param stockInfoId
     * @param publishDate
     * @return
     */
    @GetMapping("/{stockInfoId}/stock-prices/daily-summary")
    @Timed
    public Map getStockPriceDaily(@PathVariable Long stockInfoId, @RequestParam LocalDate publishDate) {
        return stockService.getStockDailySummary(stockInfoId, publishDate);
    }
    
    @GetMapping("/{stockInfoId}/stock-prices/history")
    @Timed
    public Map getStockPriceHistory(@PathVariable Long stockInfoId, @RequestParam LocalDate publishDate, @RequestParam Long duration) {
        return stockService.getStockPriceHistory(stockInfoId, publishDate, duration);
    }
    
}
