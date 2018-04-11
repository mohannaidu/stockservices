package org.mimos.stockservices.web.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.mimos.stockservices.domain.StockPrice;
import org.mimos.stockservices.repository.SectorPriceRepository;
import org.mimos.stockservices.service.SectorPriceService;
import org.mimos.stockservices.service.SectorService;
import org.mimos.stockservices.service.StockPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * Sector controller
 */
@RestController
@RequestMapping("/api/sectors")
public class SectorResource {

    private final Logger log = LoggerFactory.getLogger(SectorResource.class);
    
    private final SectorService sectorService;
    
    private final StockPriceService stockPriceService;
    
    public SectorResource(SectorService sectorService, StockPriceService stockPriceService) {
		this.sectorService = sectorService;
		this.stockPriceService = stockPriceService;
	}
    
    /**
    * GET getDailySummary
    */
    @GetMapping("/daily-summary")
    @Timed
    public List<Map> getDailySummary(@RequestParam LocalDate publishDate) {
    	return sectorService.getAllSectorsSummary(publishDate);
    }
    
    /**
     * 
     * @param sectorInfoId
     * @param publishDate
     * @param duration
     * @return
     */
	@GetMapping("{sectorInfoId}/price/history")
	@Timed
	public Map getSectorPriceHistory(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate, @RequestParam Long duration) {
		return sectorService.getSectorPriceHistory(sectorInfoId, publishDate, duration);
	}
    
	/**
	 * 
	 * 
	 * @param sectorInfoId
	 * @param publishDate
	 * @param duration
	 * @return
	 */
	@GetMapping("{sectorInfoId}/percentage/history")
	@Timed
	public Map getSectorPercentageHistory(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate, @RequestParam Long duration) {
		return sectorService.getSectorPercentageChangeHistory(sectorInfoId, publishDate, duration);
	}
    
//    @GetMapping("{sectorInfoId}/daily-summary")
//    @Timed
//    public Map getDailySummary(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate, @RequestParam Long duration) {
//    	return sectorService.getSectorOverview(sectorInfoId, publishDate, duration);
//    }
    
	
//    /**
//     * GET  /stock-prices/top-active : get all the top active in stock prices by sector.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of stockPrices in body
//     */
//    @GetMapping("/{sectorInfoId}/stock-prices/top-active")
//    @Timed
//    public List<StockPrice> getTopActiveStockPrices(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate) {
//        log.debug("REST request to get all StockPrices");
//        return stockPriceService.findTopStockActiveBySectorAndPublishDate(sectorInfoId, publishDate);
//    }
//    
//    /**
//     * GET /{sectorInfoId}/stock-prices/top-gainers : get all the top percentage in stock prices by sector.
//     * 
//     * @param sectorInfoId
//     * @param publishDate
//     * @return
//     */
//    @GetMapping("/{sectorInfoId}/stock-prices/top-percentage")
//    @Timed
//    public List<StockPrice> getTopPercentageStockPrices(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate) {
//        log.debug("REST request to get all StockPrices");
//        return stockPriceService.findTopStockPercentageBySectorAndPublishDate(sectorInfoId, publishDate);
//    }
//    
//    /**
//     * GET /{sectorInfoId}/stock-prices/top-gainers : get all the top gainers in stock prices by sector.
//     * 
//     * @param sectorInfoId
//     * @param publishDate
//     * @return
//     */
//    @GetMapping("/{sectorInfoId}/stock-prices/top-gainers")
//    @Timed
//    public List<StockPrice> getTopGainersStockPrices(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate) {
//        log.debug("REST request to get all StockPrices");
//        return stockPriceService.findTopStockGainersBySectorAndPublishDate(sectorInfoId, publishDate);
//    }
//    
//    /**
//     * GET /{sectorInfoId}/stock-prices/top-gainers : get all the top losers in stock prices by sector.
//     * 
//     * @param sectorInfoId
//     * @param publishDate
//     * @return
//     */
//    @GetMapping("/{sectorInfoId}/stock-prices/top-losers")
//    @Timed
//    public List<StockPrice> getTopLosersStockPrices(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate) {
//        log.debug("REST request to get all StockPrices");
//        return stockPriceService.findTopStockLosersBySectorAndPublishDate(sectorInfoId, publishDate);
//    }
    
   
    
}
