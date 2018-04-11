package org.mimos.stockservices.service;

import org.mimos.stockservices.domain.SectorInfo;
import java.util.List;

/**
 * Service Interface for managing SectorInfo.
 */
public interface SectorInfoService {

    /**
     * Save a sectorInfo.
     *
     * @param sectorInfo the entity to save
     * @return the persisted entity
     */
    SectorInfo save(SectorInfo sectorInfo);

    /**
     * Get all the sectorInfos.
     *
     * @return the list of entities
     */
    List<SectorInfo> findAll();
    
    /**
     * Get the "id" sectorInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SectorInfo findOne(Long id);

    /**
     * Delete the "id" sectorInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sectorInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<SectorInfo> search(String query);
}
