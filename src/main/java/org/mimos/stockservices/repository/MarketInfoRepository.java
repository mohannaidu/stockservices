package org.mimos.stockservices.repository;

import org.mimos.stockservices.domain.MarketInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MarketInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketInfoRepository extends JpaRepository<MarketInfo, Long> {

}
