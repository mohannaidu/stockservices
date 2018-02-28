package org.mimos.stockservices.repository;

import org.mimos.stockservices.domain.StockPrice;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StockPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

}
