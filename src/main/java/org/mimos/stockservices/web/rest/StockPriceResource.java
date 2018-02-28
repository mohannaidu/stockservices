package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mimos.stockservices.domain.StockPrice;

import org.mimos.stockservices.repository.StockPriceRepository;
import org.mimos.stockservices.repository.search.StockPriceSearchRepository;
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
 * REST controller for managing StockPrice.
 */
@RestController
@RequestMapping("/api")
public class StockPriceResource {

    private final Logger log = LoggerFactory.getLogger(StockPriceResource.class);

    private static final String ENTITY_NAME = "stockPrice";

    private final StockPriceRepository stockPriceRepository;

    private final StockPriceSearchRepository stockPriceSearchRepository;

    public StockPriceResource(StockPriceRepository stockPriceRepository, StockPriceSearchRepository stockPriceSearchRepository) {
        this.stockPriceRepository = stockPriceRepository;
        this.stockPriceSearchRepository = stockPriceSearchRepository;
    }

    /**
     * POST  /stock-prices : Create a new stockPrice.
     *
     * @param stockPrice the stockPrice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockPrice, or with status 400 (Bad Request) if the stockPrice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-prices")
    @Timed
    public ResponseEntity<StockPrice> createStockPrice(@RequestBody StockPrice stockPrice) throws URISyntaxException {
        log.debug("REST request to save StockPrice : {}", stockPrice);
        if (stockPrice.getId() != null) {
            throw new BadRequestAlertException("A new stockPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockPrice result = stockPriceRepository.save(stockPrice);
        stockPriceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/stock-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-prices : Updates an existing stockPrice.
     *
     * @param stockPrice the stockPrice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockPrice,
     * or with status 400 (Bad Request) if the stockPrice is not valid,
     * or with status 500 (Internal Server Error) if the stockPrice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-prices")
    @Timed
    public ResponseEntity<StockPrice> updateStockPrice(@RequestBody StockPrice stockPrice) throws URISyntaxException {
        log.debug("REST request to update StockPrice : {}", stockPrice);
        if (stockPrice.getId() == null) {
            return createStockPrice(stockPrice);
        }
        StockPrice result = stockPriceRepository.save(stockPrice);
        stockPriceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockPrice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-prices : get all the stockPrices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stockPrices in body
     */
    @GetMapping("/stock-prices")
    @Timed
    public List<StockPrice> getAllStockPrices() {
        log.debug("REST request to get all StockPrices");
        return stockPriceRepository.findAll();
        }

    /**
     * GET  /stock-prices/:id : get the "id" stockPrice.
     *
     * @param id the id of the stockPrice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockPrice, or with status 404 (Not Found)
     */
    @GetMapping("/stock-prices/{id}")
    @Timed
    public ResponseEntity<StockPrice> getStockPrice(@PathVariable Long id) {
        log.debug("REST request to get StockPrice : {}", id);
        StockPrice stockPrice = stockPriceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stockPrice));
    }

    /**
     * DELETE  /stock-prices/:id : delete the "id" stockPrice.
     *
     * @param id the id of the stockPrice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-prices/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockPrice(@PathVariable Long id) {
        log.debug("REST request to delete StockPrice : {}", id);
        stockPriceRepository.delete(id);
        stockPriceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-prices?query=:query : search for the stockPrice corresponding
     * to the query.
     *
     * @param query the query of the stockPrice search
     * @return the result of the search
     */
    @GetMapping("/_search/stock-prices")
    @Timed
    public List<StockPrice> searchStockPrices(@RequestParam String query) {
        log.debug("REST request to search StockPrices for query {}", query);
        return StreamSupport
            .stream(stockPriceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
