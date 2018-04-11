package org.mimos.stockservices.repository;

import org.mimos.stockservices.domain.IndiceInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the IndiceInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndiceInfoRepository extends JpaRepository<IndiceInfo, Long> {
	
	IndiceInfo findBySymbol(String symbol);
	
}
