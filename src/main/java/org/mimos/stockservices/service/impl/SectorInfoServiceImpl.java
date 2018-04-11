package org.mimos.stockservices.service.impl;

import org.mimos.stockservices.service.SectorInfoService;
import org.mimos.stockservices.domain.SectorInfo;
import org.mimos.stockservices.repository.SectorInfoRepository;
import org.mimos.stockservices.repository.search.SectorInfoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SectorInfo.
 */
@Service
@Transactional
public class SectorInfoServiceImpl implements SectorInfoService {

    private final Logger log = LoggerFactory.getLogger(SectorInfoServiceImpl.class);

    private final SectorInfoRepository sectorInfoRepository;

    private final SectorInfoSearchRepository sectorInfoSearchRepository;

    public SectorInfoServiceImpl(SectorInfoRepository sectorInfoRepository, SectorInfoSearchRepository sectorInfoSearchRepository) {
        this.sectorInfoRepository = sectorInfoRepository;
        this.sectorInfoSearchRepository = sectorInfoSearchRepository;
    }

    /**
     * Save a sectorInfo.
     *
     * @param sectorInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public SectorInfo save(SectorInfo sectorInfo) {
        log.debug("Request to save SectorInfo : {}", sectorInfo);
        SectorInfo result = sectorInfoRepository.save(sectorInfo);
        sectorInfoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sectorInfos.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SectorInfo> findAll() {
        log.debug("Request to get all SectorInfos");
        return sectorInfoRepository.findAll();
    }

    /**
     * Get one sectorInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SectorInfo findOne(Long id) {
        log.debug("Request to get SectorInfo : {}", id);
        return sectorInfoRepository.findOne(id);
    }

    /**
     * Delete the sectorInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SectorInfo : {}", id);
        sectorInfoRepository.delete(id);
        sectorInfoSearchRepository.delete(id);
    }

    /**
     * Search for the sectorInfo corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SectorInfo> search(String query) {
        log.debug("Request to search SectorInfos for query {}", query);
        return StreamSupport
            .stream(sectorInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
