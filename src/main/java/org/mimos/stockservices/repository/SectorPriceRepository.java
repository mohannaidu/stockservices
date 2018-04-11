package org.mimos.stockservices.repository;

import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.domain.SectorPrice;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the SectorPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectorPriceRepository extends JpaRepository<SectorPrice, Long> {
	
	public List<SectorPrice> findBySectorInfoId(long sectorInfoId);
	
	@Query("SELECT s FROM SectorPrice s WHERE s.sectorInfo.id = :sectorInfoId AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	public SectorPrice findBySectorInfoIdAndPublishDate(@Param("sectorInfoId") long sectorInfoId, @Param("date")  LocalDate publishDate);
	
	@Query("SELECT s FROM SectorPrice s WHERE DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	public List<SectorPrice> findAllByPublishDate(@Param("date") LocalDate publishDate);
	
	@Query("SELECT s FROM SectorPrice s WHERE s.sectorInfo.id = :sectorInfoId AND ((:startDate is null AND :endDate is null) OR s.publishDate BETWEEN :startDate AND :endDate)")
	public List<SectorPrice> findAllBySectorInfoIdAndPublishDateBetween(@Param("sectorInfoId") long sectorInfoId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
//	@Query("SELECT s.prices FROM SectorPrice s WHERE s.sectorInfo.id = :sectorInfoId AND ((:startDate is null AND :endDate is null) OR s.publishDate BETWEEN :startDate AND :endDate) ORDER BY publishDate ASC")
//	public List<Long> findPriceBySectorInfoIdAndPublishDateBetween(@Param("sectorInfoId") long sectorInfoId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//	
//	@Query("SELECT s.percentageChange FROM SectorPrice s WHERE s.sectorInfo.id = :sectorInfoId AND ((:startDate is null AND :endDate is null) OR s.publishDate BETWEEN :startDate AND :endDate) ORDER BY publishDate ASC")
//	public List<Long> findPercentageChangeBySectorInfoIdAndPublishDateBetween(@Param("sectorInfoId") long sectorInfoId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
}
