package org.mimos.stockservices.service.impl;

import org.mimos.stockservices.service.MarketInfoService;
import org.mimos.stockservices.domain.MarketInfo;
import org.mimos.stockservices.repository.MarketInfoRepository;
import org.mimos.stockservices.repository.search.MarketInfoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MarketInfo.
 */
@Service
@Transactional
public class MarketInfoServiceImpl implements MarketInfoService {

    private final Logger log = LoggerFactory.getLogger(MarketInfoServiceImpl.class);

    private final MarketInfoRepository marketInfoRepository;

    private final MarketInfoSearchRepository marketInfoSearchRepository;

    public MarketInfoServiceImpl(MarketInfoRepository marketInfoRepository, MarketInfoSearchRepository marketInfoSearchRepository) {
        this.marketInfoRepository = marketInfoRepository;
        this.marketInfoSearchRepository = marketInfoSearchRepository;
    }

    /**
     * Save a marketInfo.
     *
     * @param marketInfo the entity to save
     * @return the persisted entity
     */
    @Override
    public MarketInfo save(MarketInfo marketInfo) {
        log.debug("Request to save MarketInfo : {}", marketInfo);
        MarketInfo result = marketInfoRepository.save(marketInfo);
        marketInfoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the marketInfos.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MarketInfo> findAll() {
        log.debug("Request to get all MarketInfos");
        return marketInfoRepository.findAll();
    }

    /**
     * Get one marketInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MarketInfo findOne(Long id) {
        log.debug("Request to get MarketInfo : {}", id);
        return marketInfoRepository.findOne(id);
    }

    /**
     * Delete the marketInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MarketInfo : {}", id);
        marketInfoRepository.delete(id);
        marketInfoSearchRepository.delete(id);
    }

    /**
     * Search for the marketInfo corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<MarketInfo> search(String query) {
        log.debug("Request to search MarketInfos for query {}", query);
        return StreamSupport
            .stream(marketInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
