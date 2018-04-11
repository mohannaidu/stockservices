package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mimos.stockservices.domain.MarketInfo;
import org.mimos.stockservices.service.MarketInfoService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MarketInfo.
 */
@RestController
@RequestMapping("/api")
public class MarketInfoResource {

    private final Logger log = LoggerFactory.getLogger(MarketInfoResource.class);

    private static final String ENTITY_NAME = "marketInfo";

    private final MarketInfoService marketInfoService;

    public MarketInfoResource(MarketInfoService marketInfoService) {
        this.marketInfoService = marketInfoService;
    }

    /**
     * POST  /market-infos : Create a new marketInfo.
     *
     * @param marketInfo the marketInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marketInfo, or with status 400 (Bad Request) if the marketInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/market-infos")
    @Timed
    public ResponseEntity<MarketInfo> createMarketInfo(@RequestBody MarketInfo marketInfo) throws URISyntaxException {
        log.debug("REST request to save MarketInfo : {}", marketInfo);
        if (marketInfo.getId() != null) {
            throw new BadRequestAlertException("A new marketInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarketInfo result = marketInfoService.save(marketInfo);
        return ResponseEntity.created(new URI("/api/market-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /market-infos : Updates an existing marketInfo.
     *
     * @param marketInfo the marketInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marketInfo,
     * or with status 400 (Bad Request) if the marketInfo is not valid,
     * or with status 500 (Internal Server Error) if the marketInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/market-infos")
    @Timed
    public ResponseEntity<MarketInfo> updateMarketInfo(@RequestBody MarketInfo marketInfo) throws URISyntaxException {
        log.debug("REST request to update MarketInfo : {}", marketInfo);
        if (marketInfo.getId() == null) {
            return createMarketInfo(marketInfo);
        }
        MarketInfo result = marketInfoService.save(marketInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, marketInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /market-infos : get all the marketInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of marketInfos in body
     */
    @GetMapping("/market-infos")
    @Timed
    public List<MarketInfo> getAllMarketInfos() {
        log.debug("REST request to get all MarketInfos");
        return marketInfoService.findAll();
        }

    /**
     * GET  /market-infos/:id : get the "id" marketInfo.
     *
     * @param id the id of the marketInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marketInfo, or with status 404 (Not Found)
     */
    @GetMapping("/market-infos/{id}")
    @Timed
    public ResponseEntity<MarketInfo> getMarketInfo(@PathVariable Long id) {
        log.debug("REST request to get MarketInfo : {}", id);
        MarketInfo marketInfo = marketInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(marketInfo));
    }

    /**
     * DELETE  /market-infos/:id : delete the "id" marketInfo.
     *
     * @param id the id of the marketInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/market-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarketInfo(@PathVariable Long id) {
        log.debug("REST request to delete MarketInfo : {}", id);
        marketInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/market-infos?query=:query : search for the marketInfo corresponding
     * to the query.
     *
     * @param query the query of the marketInfo search
     * @return the result of the search
     */
    @GetMapping("/_search/market-infos")
    @Timed
    public List<MarketInfo> searchMarketInfos(@RequestParam String query) {
        log.debug("REST request to search MarketInfos for query {}", query);
        return marketInfoService.search(query);
    }

}
