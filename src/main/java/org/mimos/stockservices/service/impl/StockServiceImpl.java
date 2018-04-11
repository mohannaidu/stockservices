package org.mimos.stockservices.service.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mimos.stockservices.domain.StockPrice;
import org.mimos.stockservices.model.StockInfoChart;
import org.mimos.stockservices.repository.StockPriceRepository;
import org.mimos.stockservices.service.SentimentServiceClient;
import org.mimos.stockservices.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);
    
    private final StockPriceRepository stockPriceRepository;
    
    private final SentimentServiceClient sentimentServiceClient;
    
    public StockServiceImpl(StockPriceRepository stockPriceRepository, SentimentServiceClient sentimentServiceClient) {
		this.stockPriceRepository = stockPriceRepository;
		this.sentimentServiceClient = sentimentServiceClient;
	}
    
	@Override
	public List<StockPrice> getTopStocksActiveDaily(LocalDate publishDate) {
		return stockPriceRepository.findAllByPublishDateOrderByVolumeDesc(publishDate);
	}

	/**
	 * 
	 */
	@Override
	public Map getStockDailySummary(Long stockInfoId, LocalDate publishDate) {
		Map r = new HashMap();
		StockPrice stockPrice = stockPriceRepository.findByStockInfoIdAndPublishDate(stockInfoId, publishDate);
		r.put("stockPrice", stockPrice);
		
		// get total article
//		int totalArticle = sentimentServiceClient.getTotalArticleBySector("finance");
//		r.put("totalArticle", totalArticle);
		
		return r;
	}

	
	/**
	 * 
	 */
	@Override
	public Map getSectorPriceHistory(Long stockInfoId, LocalDate publishDate,	Long duration) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Map getStockPriceHistory(Long stockInfoId, LocalDate publishDate, Long duration) {
		Map r = new HashMap();
		StockPrice stock = stockPriceRepository.findOne(stockInfoId);	
		
		LocalDate startDate = publishDate.minusDays(duration);
		
		r.put("id", stock.getId());
		r.put("name", stock.getStockInfo().getName());
		r.put("code", stock.getStockInfo().getCode());
		List<StockPrice> stockPrices = stockPriceRepository.findAllByStockInfoIdAndPublishDateBetween(stockInfoId, startDate, publishDate);

		
		List<Map> stockInfoCharts = new ArrayList();
	    for( StockPrice stockprice : stockPrices) {
	     Map main = new HashMap();
	     main.put("main", new StockInfoChart(stockprice.getVolume(),stockprice.getClosing(),stock.getPercentageChange(),stockprice.getPublishDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()));
	     stockInfoCharts.add(main);
	    }
	    
	    r.put("values",stockInfoCharts);
	    
		Long totalArticle = sentimentServiceClient.getTotalArticleByStockName(stock.getStockInfo().getName());
		r.put("totalArticle", totalArticle);
	    
	    
		return r;
	}

}
