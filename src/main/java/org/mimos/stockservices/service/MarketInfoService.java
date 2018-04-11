package org.mimos.stockservices.service;

import org.mimos.stockservices.domain.MarketInfo;
import java.util.List;

/**
 * Service Interface for managing MarketInfo.
 */
public interface MarketInfoService {

    /**
     * Save a marketInfo.
     *
     * @param marketInfo the entity to save
     * @return the persisted entity
     */
    MarketInfo save(MarketInfo marketInfo);

    /**
     * Get all the marketInfos.
     *
     * @return the list of entities
     */
    List<MarketInfo> findAll();

    /**
     * Get the "id" marketInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MarketInfo findOne(Long id);

    /**
     * Delete the "id" marketInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the marketInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<MarketInfo> search(String query);
}
