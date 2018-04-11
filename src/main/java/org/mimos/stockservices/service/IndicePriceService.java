package org.mimos.stockservices.service;

import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.domain.IndicePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing IndicePrice.
 */
public interface IndicePriceService {

    /**
     * Save a indicePrice.
     *
     * @param indicePrice the entity to save
     * @return the persisted entity
     */
    IndicePrice save(IndicePrice indicePrice);

    /**
     * Get all the indicePrices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IndicePrice> findAll(Pageable pageable);

    /**
     * Get the "id" indicePrice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    IndicePrice findOne(Long id);

    /**
     * Delete the "id" indicePrice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the indicePrice corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IndicePrice> search(String query, Pageable pageable);
    
    List<IndicePrice> findAllByIndiceInfoId(Long indiceInfoId);
    
    List<IndicePrice> findAllByIndiceInfoSymbol(String symbol);
    
    List<IndicePrice> findAllByIndiceInfoSymbolAndPublishDateBetween(String symbol, LocalDate startDate, LocalDate endDate);
    
}
