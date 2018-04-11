package org.mimos.stockservices.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.mimos.stockservices.service.IndiceInfoService;
import org.mimos.stockservices.domain.IndiceInfo;
import org.mimos.stockservices.repository.IndiceInfoRepository;
import org.mimos.stockservices.repository.search.IndiceInfoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IndiceInfo.
 */
@Service
@Transactional
public class IndiceInfoServiceImpl implements IndiceInfoService {

    private final Logger log = LoggerFactory.getLogger(IndiceInfoServiceImpl.class);

    private final IndiceInfoRepository indiceInfoRepository;

    private final IndiceInfoSearchRepository indiceInfoSearchRepository;

    public IndiceInfoServiceImpl(IndiceInfoRepository indiceInfoRepository, IndiceInfoSearchRepository indiceInfoSearchRepository) {
        this.indiceInfoRepository = indiceInfoRepository;
        this.indiceInfoSearchRepository = indiceInfoSearchRepository;
    }

    /**
     * Save a indiceInfo.
     *
     * @param indiceInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public IndiceInfo save(IndiceInfo indiceInfo) {
        log.debug("Request to save IndiceInfo : {}", indiceInfo);
        IndiceInfo result = indiceInfoRepository.save(indiceInfo);
        indiceInfoSearchRepository.save(result);
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<IndiceInfo> findAll() {
    	return indiceInfoRepository.findAll();
    }

    /**
     * Get all the indiceInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IndiceInfo> findAll(Pageable pageable) {
        log.debug("Request to get all IndiceInfos");
        return indiceInfoRepository.findAll(pageable);
    }

    /**
     * Get one indiceInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IndiceInfo findOne(Long id) {
        log.debug("Request to get IndiceInfo : {}", id);
        return indiceInfoRepository.findOne(id);
    }

    /**
     * Delete the indiceInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IndiceInfo : {}", id);
        indiceInfoRepository.delete(id);
        indiceInfoSearchRepository.delete(id);
    }

    /**
     * Search for the indiceInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IndiceInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IndiceInfos for query {}", query);
        Page<IndiceInfo> result = indiceInfoSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
    
    @Override
    public IndiceInfo findBySymbol(String symbol) {
    	return indiceInfoRepository.findBySymbol(symbol);
    }
}
