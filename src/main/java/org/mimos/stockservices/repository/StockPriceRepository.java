package org.mimos.stockservices.repository;

import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.domain.StockInfo;
import org.mimos.stockservices.domain.StockPrice;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the StockPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
	
	
	public List<StockPrice> findAllByPublishDate(LocalDate publishDate);
	
	public List<StockPrice> findAllByStockInfo(StockInfo stockInfo);
	
	public List<StockPrice> findAllByStockInfoId(Long id);
	
	public StockPrice findByStockInfoIdAndPublishDate(Long id, LocalDate publishDate);
	
	@Query("SELECT s FROM StockPrice s WHERE s.stockInfo.id in :ids AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	public List<StockPrice> findAllByInfoIdInAndPublishDate(@Param("ids") List<Long> stockInfoIds, @Param("date") LocalDate publishDate);
	
	@Query("SELECT s FROM StockPrice s WHERE s.stockInfo.sectorInfo.id=:sectorInfoId AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.volume DESC")
	public List<StockPrice> findAllBySectorInfoIdPublishDateOrderByVolumeDesc(@Param("sectorInfoId") long sectorInfoId, @Param("date") LocalDate publishDate);
	
	@Query("SELECT s FROM StockPrice s WHERE s.stockInfo.sectorInfo.id=:sectorInfoId AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.change DESC")
	public List<StockPrice> findAllBySectorInfoIdPublishDateOrderByChangeDesc(@Param("sectorInfoId") long sectorInfoId, @Param("date") LocalDate publishDate);
	
	@Query("SELECT s FROM StockPrice s WHERE s.stockInfo.sectorInfo.id=:sectorInfoId AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.change DESC")
	public List<StockPrice> findAllBySectorInfoIdPublishDateOrderByChangeAsc(@Param("sectorInfoId") long sectorInfoId, @Param("date") LocalDate publishDate);
	
	@Query("SELECT s FROM StockPrice s WHERE s.stockInfo.sectorInfo.id=:sectorInfoId AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.percentageChange DESC")
	public List<StockPrice> findAllBySectorInfoIdPublishDateOrderByPercentageChangeDesc(@Param("sectorInfoId") long sectorInfoId, @Param("date") LocalDate publishDate);
	
	
	@Query("SELECT s FROM StockPrice s WHERE DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.volume DESC")
	public List<StockPrice> findAllByPublishDateOrderByVolumeDesc(@Param("date") LocalDate publishDate);
	
//	@Query("SELECT s FROM StockPrice s WHERE ORDER BY s.volume DESC")
//	public Page<StockPrice> findAllOrderByVolumeDesc(Pageable pageable);
	
	@Query("SELECT s FROM StockPrice s WHERE DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.change DESC")
	public List<StockPrice> findAllByPublishDateOrderByChangeDesc(@Param("date") LocalDate publishDate);
	
	@Query("SELECT s FROM StockPrice s WHERE DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.change DESC")
	public List<StockPrice> findAllByPublishDateOrderByChangeAsc(@Param("date") LocalDate publishDate);
	
	@Query("SELECT s FROM StockPrice s WHERE DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d') ORDER BY s.percentageChange DESC")
	public List<StockPrice> findAllByPublishDateOrderByPercentageChangeDesc(@Param("date") LocalDate publishDate);
	
	
	// Statistics
	@Query("SELECT count(s) FROM StockPrice s WHERE s.stockInfo.sectorInfo.id = :sectorInfoId AND s.change > 0 AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	public int findTotalPositiveChangeBySectorId(@Param("sectorInfoId") long sectorInfoId,  @Param("date") LocalDate publishDate);
	
	@Query("SELECT count(s) FROM StockPrice s WHERE s.stockInfo.sectorInfo.id = :sectorInfoId AND s.change < 0 AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	public int findTotalNegativeChangeBySectorId(@Param("sectorInfoId") long sectorInfoId, @Param("date") LocalDate publishDate);
	
	@Query("SELECT count(s) FROM StockPrice s WHERE s.stockInfo.sectorInfo.id = :sectorInfoId AND s.change = 0 AND DATE_FORMAT(s.publishDate, '%Y-%m-%d') = DATE_FORMAT(:date, '%Y-%m-%d')")
	public int findTotalUnchangedBySectorId(@Param("sectorInfoId") long sectorInfoId, @Param("date") LocalDate publishDate);

	@Query("SELECT s.publishDate FROM StockPrice s GROUP BY s.publishDate ORDER BY s.publishDate DESC")
	public Page<LocalDate> findLatestPublishDate(Pageable pageable);
	
	@Query("SELECT s FROM StockPrice s WHERE s.stockInfo.id = :stockInfoId AND ((:startDate is null AND :endDate is null) OR s.publishDate BETWEEN :startDate AND :endDate)")
	 public List<StockPrice> findAllByStockInfoIdAndPublishDateBetween(@Param("stockInfoId") long sectorInfoId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


}
