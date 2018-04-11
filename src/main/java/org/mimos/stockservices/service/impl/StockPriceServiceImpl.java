package org.mimos.stockservices.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mimos.stockservices.domain.StockInfo;
import org.mimos.stockservices.domain.StockPrice;
import org.mimos.stockservices.domain.UserPortfolio;
import org.mimos.stockservices.repository.StockPriceRepository;
import org.mimos.stockservices.repository.UserPortfolioRepository;
import org.mimos.stockservices.service.StockPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockPriceServiceImpl implements StockPriceService {

    private final Logger log = LoggerFactory.getLogger(StockPriceServiceImpl.class);
    
    private final StockPriceRepository stockPriceRepository;
    
    private final UserPortfolioRepository userPortfolioRepository;
    
    public StockPriceServiceImpl(StockPriceRepository stockPriceRepository, UserPortfolioRepository userPortfolioRepository) {
		this.stockPriceRepository = stockPriceRepository;
    	this.userPortfolioRepository = userPortfolioRepository;
	}
    
    @Override
    public List<StockPrice> findAllByStockInfoIdsAndPublishDate(List<Long> stockInfoIds, LocalDate publishDate) {
    	return stockPriceRepository.findAllByInfoIdInAndPublishDate(stockInfoIds, publishDate);
    }

	@Override
	public List<StockPrice> findTopStockActiveBySectorAndPublishDate(
			Long sectorInfoId, LocalDate publishDate) {
		return stockPriceRepository.findAllBySectorInfoIdPublishDateOrderByVolumeDesc(sectorInfoId, publishDate);
	}

	@Override
	public List<StockPrice> findTopStockPercentageBySectorAndPublishDate(
			Long sectorInfoId, LocalDate publishDate) {
		return stockPriceRepository.findAllBySectorInfoIdPublishDateOrderByPercentageChangeDesc(sectorInfoId, publishDate);
	}

	@Override
	public List<StockPrice> findTopStockGainersBySectorAndPublishDate(
			Long sectorInfoId, LocalDate publishDate) {
		return stockPriceRepository.findAllBySectorInfoIdPublishDateOrderByChangeDesc(sectorInfoId, publishDate);
	}

	@Override
	public List<StockPrice> findTopStockLosersBySectorAndPublishDate(
			Long sectorInfoId, LocalDate publishDate) {
		return stockPriceRepository.findAllBySectorInfoIdPublishDateOrderByChangeAsc(sectorInfoId, publishDate);
	}

	@Override
	public List<StockPrice> findTopStockActiveByPublishDate(
			LocalDate publishDate) {
		// TODO Auto-generated method stub
		return stockPriceRepository.findAllByPublishDateOrderByVolumeDesc(publishDate);
	}

	@Override
	public List<StockPrice> findTopStockPercentageByPublishDate(
			LocalDate publishDate) {
		// TODO Auto-generated method stub
		return stockPriceRepository.findAllByPublishDateOrderByPercentageChangeDesc(publishDate);
	}

	@Override
	public List<StockPrice> findTopStockGainersByPublishDate(
			LocalDate publishDate) {
		// TODO Auto-generated method stub
		return stockPriceRepository.findAllByPublishDateOrderByChangeDesc(publishDate);
	}

	@Override
	public List<StockPrice> findTopStockLosersByPublishDate(
			LocalDate publishDate) {
		// TODO Auto-generated method stub
		return stockPriceRepository.findAllByPublishDateOrderByChangeAsc(publishDate);
	}
    
//	@Override
//	public List<StockPrice> finStockPriceByUserPortfolioIdAndDate(Long id, LocalDate date) {
//		UserPortfolio porffolio = userPortfolioRepository.findOne(id);
//		// Set<StockInfo> stockInfos = porffolio.getFavStocks();
//		List<Long> stockInfoIds = porffolio.getFavStocks().stream().map(i -> i.getId()).collect(Collectors.toList());
//		
//		return stockPriceRepository.findByInfoIdInAndPublishDate(stockInfoIds, date);
//	}
    
    

}
