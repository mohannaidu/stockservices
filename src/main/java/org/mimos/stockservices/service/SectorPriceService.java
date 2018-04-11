package org.mimos.stockservices.service;

import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.domain.SectorInfo;
import org.mimos.stockservices.domain.SectorPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing SectorPrice.
 */
public interface SectorPriceService {

    /**
     * Save a sectorPrice.
     *
     * @param sectorPrice the entity to save
     * @return the persisted entity
     */
    SectorPrice save(SectorPrice sectorPrice);

    /**
     * Get all the sectorPrices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SectorPrice> findAll(Pageable pageable);
    
    
    /**
     * Get the "id" sectorPrice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SectorPrice findOne(Long id);

    /**
     * Delete the "id" sectorPrice.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sectorPrice corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SectorPrice> search(String query, Pageable pageable);
    
    
    /**
     * Get all the sectors prices.
     *
     * @return the list of entities
     */
    List<SectorPrice> findAllByPublishDate(LocalDate publishDate);
    
    /**
     * 
     * @param sectorInfoId
     * @return
     */
    List<SectorPrice> findAllBySectorInfoId(long sectorInfoId);
    
    /**
     * 
     * @param sectorInfoId
     * @param publishDate
     * @return
     */
    SectorPrice findAllBySectorInfoIdAndPublishDate(long sectorInfoId, LocalDate publishDate);
    
    
}
