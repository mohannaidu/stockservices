package org.mimos.stockservices.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.mimos.stockservices.domain.StockPrice;

public interface StockService {
	
	@Deprecated
	public List<StockPrice> getTopStocksActiveDaily(LocalDate publishDate);
	
	public Map getStockDailySummary(Long stockInfoId, LocalDate publishDate);
	
	public Map getSectorPriceHistory(Long stockInfoId, LocalDate publishDate, Long duration);
	
	public Map getStockPriceHistory(Long stockInfoId, LocalDate publishDate, Long duration);
}
