package org.mimos.stockservices.repository;

import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.domain.IndicePrice;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the IndicePrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndicePriceRepository extends JpaRepository<IndicePrice, Long> {
	
	public List<IndicePrice> findAllByIndiceInfoId(Long id);
	
	public List<IndicePrice> findAllByIndiceInfoSymbol(String symbol);
	
	@Query("SELECT ip FROM IndicePrice ip WHERE ip.indiceInfo.symbol=:symbol AND ((:startDate is null AND :endDate is null) OR ip.publishDate BETWEEN :startDate AND :endDate)")
	public List<IndicePrice> findAllByIndiceInfoSymbolAndPublishDateBetween(@Param("symbol") String symbol, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
	
}
