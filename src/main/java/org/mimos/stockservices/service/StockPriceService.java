package org.mimos.stockservices.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.domain.StockInfo;
import org.mimos.stockservices.domain.StockPrice;

public interface StockPriceService {
	
	public List<StockPrice> findAllByStockInfoIdsAndPublishDate(List<Long> stockInfoIds, LocalDate publishDate);
	
	
	public List<StockPrice> findTopStockActiveByPublishDate(LocalDate publishDate);
	
	public List<StockPrice> findTopStockPercentageByPublishDate(LocalDate publishDate);
	
	public List<StockPrice> findTopStockGainersByPublishDate(LocalDate publishDate);
	
	public List<StockPrice> findTopStockLosersByPublishDate(LocalDate publishDate);
	
	
	
	
	public List<StockPrice> findTopStockActiveBySectorAndPublishDate(Long sectorInfoId, LocalDate publishDate);
	
	public List<StockPrice> findTopStockPercentageBySectorAndPublishDate(Long sectorInfoId, LocalDate publishDate);
	
	public List<StockPrice> findTopStockGainersBySectorAndPublishDate(Long sectorInfoId, LocalDate publishDate);
	
	public List<StockPrice> findTopStockLosersBySectorAndPublishDate(Long sectorInfoId, LocalDate publishDate);
	
}
