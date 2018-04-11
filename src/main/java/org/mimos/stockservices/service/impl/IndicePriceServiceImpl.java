package org.mimos.stockservices.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.mimos.stockservices.service.IndicePriceService;
import org.mimos.stockservices.domain.IndicePrice;
import org.mimos.stockservices.repository.IndicePriceRepository;
import org.mimos.stockservices.repository.search.IndicePriceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IndicePrice.
 */
@Service
@Transactional
public class IndicePriceServiceImpl implements IndicePriceService {

    private final Logger log = LoggerFactory.getLogger(IndicePriceServiceImpl.class);

    private final IndicePriceRepository indicePriceRepository;

    private final IndicePriceSearchRepository indicePriceSearchRepository;

    public IndicePriceServiceImpl(IndicePriceRepository indicePriceRepository, IndicePriceSearchRepository indicePriceSearchRepository) {
        this.indicePriceRepository = indicePriceRepository;
        this.indicePriceSearchRepository = indicePriceSearchRepository;
    }

    /**
     * Save a indicePrice.
     *
     * @param indicePrice the entity to save
     * @return the persisted entity
     */
    @Override
    public IndicePrice save(IndicePrice indicePrice) {
        log.debug("Request to save IndicePrice : {}", indicePrice);
        IndicePrice result = indicePriceRepository.save(indicePrice);
        indicePriceSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the indicePrices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IndicePrice> findAll(Pageable pageable) {
        log.debug("Request to get all IndicePrices");
        return indicePriceRepository.findAll(pageable);
    }

    /**
     * Get one indicePrice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IndicePrice findOne(Long id) {
        log.debug("Request to get IndicePrice : {}", id);
        return indicePriceRepository.findOne(id);
    }

    /**
     * Delete the indicePrice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IndicePrice : {}", id);
        indicePriceRepository.delete(id);
        indicePriceSearchRepository.delete(id);
    }

    /**
     * Search for the indicePrice corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IndicePrice> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IndicePrices for query {}", query);
        Page<IndicePrice> result = indicePriceSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

	@Override
	public List<IndicePrice> findAllByIndiceInfoId(Long indiceInfoId) {
		return indicePriceRepository.findAllByIndiceInfoId(indiceInfoId);
	}

	@Override
	public List<IndicePrice> findAllByIndiceInfoSymbol(String symbol) {
		
		return indicePriceRepository.findAllByIndiceInfoSymbol(symbol);
	}
	
	@Override
	public List<IndicePrice> findAllByIndiceInfoSymbolAndPublishDateBetween(
			String symbol, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return indicePriceRepository.findAllByIndiceInfoSymbolAndPublishDateBetween(symbol, startDate, endDate);
	}
}
