package org.mimos.stockservices.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.mimos.stockservices.service.SectorPriceService;
import org.mimos.stockservices.domain.SectorInfo;
import org.mimos.stockservices.domain.SectorPrice;
import org.mimos.stockservices.repository.SectorInfoRepository;
import org.mimos.stockservices.repository.SectorPriceRepository;
import org.mimos.stockservices.repository.search.SectorPriceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SectorPrice.
 */
@Service
@Transactional
public class SectorPriceServiceImpl implements SectorPriceService {

    private final Logger log = LoggerFactory.getLogger(SectorPriceServiceImpl.class);
    
    private final SectorInfoRepository sectorInfoRepository;

    private final SectorPriceRepository sectorPriceRepository;

    private final SectorPriceSearchRepository sectorPriceSearchRepository;

    public SectorPriceServiceImpl(SectorPriceRepository sectorPriceRepository, SectorPriceSearchRepository sectorPriceSearchRepository, SectorInfoRepository sectorInfoRepository) {
        this.sectorPriceRepository = sectorPriceRepository;
        this.sectorPriceSearchRepository = sectorPriceSearchRepository;
        this.sectorInfoRepository = sectorInfoRepository;
    }

    /**
     * Save a sectorPrice.
     *
     * @param sectorPrice the entity to save
     * @return the persisted entity
     */
    @Override
    public SectorPrice save(SectorPrice sectorPrice) {
        log.debug("Request to save SectorPrice : {}", sectorPrice);
        SectorPrice result = sectorPriceRepository.save(sectorPrice);
        sectorPriceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sectorPrices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SectorPrice> findAll(Pageable pageable) {
        log.debug("Request to get all SectorPrices");
        return sectorPriceRepository.findAll(pageable);
    }

    /**
     * Get one sectorPrice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SectorPrice findOne(Long id) {
        log.debug("Request to get SectorPrice : {}", id);
        return sectorPriceRepository.findOne(id);
    }

    /**
     * Delete the sectorPrice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SectorPrice : {}", id);
        sectorPriceRepository.delete(id);
        sectorPriceSearchRepository.delete(id);
    }

    /**
     * Search for the sectorPrice corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SectorPrice> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SectorPrices for query {}", query);
        Page<SectorPrice> result = sectorPriceSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

	@Override
	public List<SectorPrice> findAllByPublishDate(LocalDate publishDate) {
		return this.sectorPriceRepository.findAllByPublishDate(publishDate);
	}
	
	@Override
	public List<SectorPrice> findAllBySectorInfoId(long sectorInfoId) {
		
		return this.sectorPriceRepository.findBySectorInfoId(sectorInfoId);
	}

	@Override
	public SectorPrice findAllBySectorInfoIdAndPublishDate(
			long id, LocalDate publishDate) {
		return this.sectorPriceRepository.findBySectorInfoIdAndPublishDate(id, publishDate);
	}

	
	
	
}
