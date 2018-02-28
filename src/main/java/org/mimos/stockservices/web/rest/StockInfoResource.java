package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mimos.stockservices.domain.StockInfo;

import org.mimos.stockservices.repository.StockInfoRepository;
import org.mimos.stockservices.repository.search.StockInfoSearchRepository;
import org.mimos.stockservices.web.rest.errors.BadRequestAlertException;
import org.mimos.stockservices.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing StockInfo.
 */
@RestController
@RequestMapping("/api")
public class StockInfoResource {

    private final Logger log = LoggerFactory.getLogger(StockInfoResource.class);

    private static final String ENTITY_NAME = "stockInfo";

    private final StockInfoRepository stockInfoRepository;

    private final StockInfoSearchRepository stockInfoSearchRepository;

    public StockInfoResource(StockInfoRepository stockInfoRepository, StockInfoSearchRepository stockInfoSearchRepository) {
        this.stockInfoRepository = stockInfoRepository;
        this.stockInfoSearchRepository = stockInfoSearchRepository;
    }

    /**
     * POST  /stock-infos : Create a new stockInfo.
     *
     * @param stockInfo the stockInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockInfo, or with status 400 (Bad Request) if the stockInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-infos")
    @Timed
    public ResponseEntity<StockInfo> createStockInfo(@RequestBody StockInfo stockInfo) throws URISyntaxException {
        log.debug("REST request to save StockInfo : {}", stockInfo);
        if (stockInfo.getId() != null) {
            throw new BadRequestAlertException("A new stockInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockInfo result = stockInfoRepository.save(stockInfo);
        stockInfoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/stock-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-infos : Updates an existing stockInfo.
     *
     * @param stockInfo the stockInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockInfo,
     * or with status 400 (Bad Request) if the stockInfo is not valid,
     * or with status 500 (Internal Server Error) if the stockInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-infos")
    @Timed
    public ResponseEntity<StockInfo> updateStockInfo(@RequestBody StockInfo stockInfo) throws URISyntaxException {
        log.debug("REST request to update StockInfo : {}", stockInfo);
        if (stockInfo.getId() == null) {
            return createStockInfo(stockInfo);
        }
        StockInfo result = stockInfoRepository.save(stockInfo);
        stockInfoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-infos : get all the stockInfos.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of stockInfos in body
     */
    @GetMapping("/stock-infos")
    @Timed
    public List<StockInfo> getAllStockInfos(@RequestParam(required = false) String filter) {
        if ("price-is-null".equals(filter)) {
            log.debug("REST request to get all StockInfos where price is null");
            return StreamSupport
                .stream(stockInfoRepository.findAll().spliterator(), false)
                .filter(stockInfo -> stockInfo.getPrice() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all StockInfos");
        return stockInfoRepository.findAll();
        }

    /**
     * GET  /stock-infos/:id : get the "id" stockInfo.
     *
     * @param id the id of the stockInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockInfo, or with status 404 (Not Found)
     */
    @GetMapping("/stock-infos/{id}")
    @Timed
    public ResponseEntity<StockInfo> getStockInfo(@PathVariable Long id) {
        log.debug("REST request to get StockInfo : {}", id);
        StockInfo stockInfo = stockInfoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockInfo));
    }

    /**
     * DELETE  /stock-infos/:id : delete the "id" stockInfo.
     *
     * @param id the id of the stockInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockInfo(@PathVariable Long id) {
        log.debug("REST request to delete StockInfo : {}", id);
        stockInfoRepository.delete(id);
        stockInfoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-infos?query=:query : search for the stockInfo corresponding
     * to the query.
     *
     * @param query the query of the stockInfo search
     * @return the result of the search
     */
    @GetMapping("/_search/stock-infos")
    @Timed
    public List<StockInfo> searchStockInfos(@RequestParam String query) {
        log.debug("REST request to search StockInfos for query {}", query);
        return StreamSupport
            .stream(stockInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
