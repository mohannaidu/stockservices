package org.mimos.stockservices.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mimos.stockservices.domain.StockInfo;
import org.mimos.stockservices.domain.StockPrice;
import org.mimos.stockservices.domain.UserPortfolio;
import org.mimos.stockservices.repository.UserPortfolioRepository;
import org.mimos.stockservices.service.StockPriceService;
import org.mimos.stockservices.service.UserPortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserPortfolioServiceImpl implements UserPortfolioService {

    private final Logger log = LoggerFactory.getLogger(UserPortfolioServiceImpl.class);

    private final UserPortfolioRepository userPortfolioRepository;
    
    private final StockPriceService stockPriceService;
    
    public UserPortfolioServiceImpl(UserPortfolioRepository userPortfolioRepository, StockPriceService stockPriceService) {
		this.userPortfolioRepository = userPortfolioRepository;
		this.stockPriceService = stockPriceService;
	}

	@Override
	public List<StockPrice> getUserPortfolioStockPrices(String username, LocalDate publishDate) {
		List<UserPortfolio> userPortfolios = userPortfolioRepository.findAllByUsername(username);
        List<Long> stockInfoIds = userPortfolios.stream().map(s -> s.getStockInfo().getId()).collect(Collectors.toList());
        
        List<StockPrice> stockPrices = new ArrayList<StockPrice>();
        if (stockInfoIds.size() > 0) {
        	stockPrices = stockPriceService.findAllByStockInfoIdsAndPublishDate(stockInfoIds, publishDate);
        }
        
		return stockPrices;
	}
	
	
}
