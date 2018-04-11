package org.mimos.stockservices.service;

import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.domain.StockPrice;

public interface UserPortfolioService {
	
	public List<StockPrice> getUserPortfolioStockPrices(String userid, LocalDate publishDate);
	
}
