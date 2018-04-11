package org.mimos.stockservices.service;

import java.util.List;

import org.mimos.stockservices.domain.IndiceInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing IndiceInfo.
 */
public interface IndiceInfoService {

    /**
     * Save a indiceInfo.
     *
     * @param indiceInfo the entity to save
     * @return the persisted entity
     */
    IndiceInfo save(IndiceInfo indiceInfo);
    
    /**
     * Get all the indiceInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    List<IndiceInfo> findAll();

    /**
     * Get all the indiceInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IndiceInfo> findAll(Pageable pageable);

    /**
     * Get the "id" indiceInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    IndiceInfo findOne(Long id);

    /**
     * Delete the "id" indiceInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the indiceInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IndiceInfo> search(String query, Pageable pageable);
    
    IndiceInfo findBySymbol(String symbol);
    
}
