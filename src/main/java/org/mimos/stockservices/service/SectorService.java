package org.mimos.stockservices.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SectorService {
	
	public List<Map> getAllSectorsSummary(LocalDate publishDate);
	
	public Map getSectorSummary(long sectorId, LocalDate publishDate);
	
	//public Map getSectorOverview(long sectorId, LocalDate publishDate, long duration);
	
	public Map getSectorPriceHistory(long sectorInfoId, LocalDate publishDate, long duration);
	
	public Map getSectorPercentageChangeHistory(long sectorInfoId, LocalDate publishDate, long duration);
	
}
