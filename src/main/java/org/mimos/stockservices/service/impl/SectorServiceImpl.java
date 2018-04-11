package org.mimos.stockservices.service.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mimos.stockservices.domain.SectorInfo;
import org.mimos.stockservices.domain.SectorPrice;
import org.mimos.stockservices.model.SectorPercentageChart;
import org.mimos.stockservices.model.SectorPriceChart;
import org.mimos.stockservices.repository.SectorInfoRepository;
import org.mimos.stockservices.repository.SectorPriceRepository;
import org.mimos.stockservices.repository.StockPriceRepository;
import org.mimos.stockservices.service.SectorService;
import org.mimos.stockservices.service.SentimentServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netflix.discovery.EurekaClient;

@Service
@Transactional
public class SectorServiceImpl implements SectorService {

    private final Logger log = LoggerFactory.getLogger(SectorServiceImpl.class);
    
    private final SectorInfoRepository sectorInfoRepository;
    
    private final SectorPriceRepository sectorPriceRepository;
    
    private final StockPriceRepository stockPriceRepository;
    
    private final EurekaClient eurekaClient;
    
    private final SentimentServiceClient sentimentServiceClient;
    
    
    public SectorServiceImpl(SectorInfoRepository sectorRepository, SectorPriceRepository sectorPriceRepository, StockPriceRepository stockPriceRepository, EurekaClient eurekaClient,  SentimentServiceClient sentimentServiceClient) {
		this.sectorInfoRepository = sectorRepository;
		this.sectorPriceRepository = sectorPriceRepository;
    	this.stockPriceRepository = stockPriceRepository;
    	this.eurekaClient = eurekaClient;
    	this.sentimentServiceClient = sentimentServiceClient;
	}
    
	@Override
	public Map getSectorSummary(long sectorId, LocalDate publishDate) {
		Map result = new HashMap();
		SectorInfo sectorInfo = sectorInfoRepository.getOne(sectorId);
		
		if (sectorInfo == null) {
			return null;
		}
		
		result.put("sectorInfo", sectorInfo);
		
		// get number of stock that have positive changes
		int gainers = stockPriceRepository.findTotalPositiveChangeBySectorId(sectorId, publishDate);
		result.put("gainers", gainers);
		
		// get number of stocks that have negative changes
		int losers = stockPriceRepository.findTotalNegativeChangeBySectorId(sectorId, publishDate);
		result.put("losers", losers);
		
		// get number of stocks that have negative changes
		int unchanged = stockPriceRepository.findTotalUnchangedBySectorId(sectorId, publishDate);
		result.put("unchanged", unchanged);
		
		int totalArticle = sentimentServiceClient.getTotalArticleBySector(sectorInfo.getCode());
		result.put("totalArticle", totalArticle);
		
//		Application application = eurekaClient.getApplication(employeeSearchServiceId);
//        InstanceInfo instanceInfo = application.getInstances().get(0);
		
		return result;
	}

	@Override
	public List<Map> getAllSectorsSummary(final LocalDate publishDate) {
		//List<Sector> sectors = sectorRepository.findAll();
				
		return sectorInfoRepository.findAll().stream().map( s -> getSectorSummary(s.getId(), publishDate)).collect(Collectors.toList());
		
	}

//	@Override
//	public Map getSectorOverview(long sectorId, LocalDate publishDate, long duration) {
//		Map r = new HashMap();
//		SectorInfo sector = sectorInfoRepository.findOne(sectorId);
//		r.put("sectorInfo", sector);
//		LocalDate startDate = publishDate.minusDays(duration);
//		
//		SectorPrice price  = sectorPriceRepository.findBySectorInfoIdAndPublishDate(sectorId, publishDate);
//		r.put("percentageChange", price.getPercentageChange());
//		
//		List<SectorPrice> sectorPrice = sectorPriceRepository.findAllBySectorInfoIdAndPublishDateBetween(sectorId, startDate, publishDate);
//		r.put("percentageChangeHistory", getPecentageChangeHistory(sectorPrice));
//		
//		return r;
//	}
//	
//	private List<Map.Entry<Long, Double>> getPecentageChangeHistory(List<SectorPrice> sectorPrices) {
//		
//		List<Map.Entry<Long, Double>> l1 = sectorPrices.stream().map(p -> new AbstractMap.SimpleEntry<Long, Double>(p.getPublishDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), p.getPercentageChange())).collect(Collectors.toList());
//		
//		return l1;
//	}

	@Override
	public Map getSectorPriceHistory(long sectorInfoId, LocalDate publishDate,
			long duration) {
		Map r = new HashMap();
		SectorInfo sector = sectorInfoRepository.findOne(sectorInfoId);
		
		
		if (sector != null) {
		r.put("id", sector.getId());
		r.put("name", sector.getName());
		r.put("code", sector.getCode());
		
		LocalDate startDate = publishDate.minusDays(duration);
		
		// query data in SectorPrice by sectorInfoId and publishDate between 
		// startDate and publishDate (end date)
//		List<SectorPrice> sectorPrice = sectorPriceRepository.findAllBySectorInfoIdAndPublishDateBetween(sectorInfoId, startDate, publishDate).stream().collect(Collectors.toList());;
//		r.put("values", sectorPrice);
		
		List<SectorPrice> sectorPrices = sectorPriceRepository.findAllBySectorInfoIdAndPublishDateBetween(sectorInfoId, startDate, publishDate);
		List<SectorPriceChart> sectorPriceCharts = new ArrayList<>();
		for (SectorPrice sectorPrice : sectorPrices) {
			sectorPriceCharts.add(new SectorPriceChart(sectorPrice.getPublishDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), sectorPrice.getClosing()));
		}
		

		r.put("values", sectorPriceCharts);

//		List<Map> values = null; // TODO: find closing price history here in repository
	//	r.put("values", values);
		}
		return r;
	}

	@Override
	public Map getSectorPercentageChangeHistory(long sectorInfoId,
			LocalDate publishDate, long duration) {
		Map r = new HashMap();
		SectorInfo sector = sectorInfoRepository.findOne(sectorInfoId);
		r.put("id", sector.getId());
		r.put("name", sector.getName());
		r.put("code", sector.getCode());
		
		LocalDate startDate = publishDate.minusDays(duration);
		
		// query data in SectorPrice by sectorInfoId and publishDate between 
		// startDate and publishDate (end date)
		List<SectorPrice> sectorPrices = sectorPriceRepository.findAllBySectorInfoIdAndPublishDateBetween(sectorInfoId, startDate, publishDate);
		List<SectorPercentageChart> sectorPercentageCharts = new ArrayList<>();
		for (SectorPrice sectorPrice : sectorPrices) {
			sectorPercentageCharts.add(new SectorPercentageChart(sectorPrice.getPublishDate().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), sectorPrice.getPercentageChange()));
			
		}
		r.put("values", sectorPercentageCharts);
//		List<Map> values = null; // TODO: find percentage change history here
//		r.put("values", values);
		
		return r;
	}
}
