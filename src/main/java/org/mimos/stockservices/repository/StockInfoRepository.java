package org.mimos.stockservices.repository;

import java.util.List;

import org.mimos.stockservices.domain.SectorInfo;
import org.mimos.stockservices.domain.SectorPrice;
import org.mimos.stockservices.domain.StockInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StockInfo entity.
 */
@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {

	public List<StockInfo> findAllBySectorInfo(SectorInfo sectorInfo);

}
