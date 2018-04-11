package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.mimos.stockservices.domain.SectorPrice;
import org.mimos.stockservices.service.SectorPriceService;
import org.mimos.stockservices.web.rest.errors.BadRequestAlertException;
import org.mimos.stockservices.web.rest.util.HeaderUtil;
import org.mimos.stockservices.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SectorPrice.
 */
@RestController
@RequestMapping("/api/sectors")
public class SectorPriceResource {

    private final Logger log = LoggerFactory.getLogger(SectorPriceResource.class);

    private static final String ENTITY_NAME = "sectorPrice";

    private final SectorPriceService sectorPriceService;

    public SectorPriceResource(SectorPriceService sectorPriceService) {
        this.sectorPriceService = sectorPriceService;
    }

    /**
     * POST  /sector-prices : Create a new sectorPrice.
     *
     * @param sectorPrice the sectorPrice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sectorPrice, or with status 400 (Bad Request) if the sectorPrice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sector-prices")
    @Timed
    public ResponseEntity<SectorPrice> createSectorPrice(@RequestBody SectorPrice sectorPrice) throws URISyntaxException {
        log.debug("REST request to save SectorPrice : {}", sectorPrice);
        if (sectorPrice.getId() != null) {
            throw new BadRequestAlertException("A new sectorPrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SectorPrice result = sectorPriceService.save(sectorPrice);
        return ResponseEntity.created(new URI("/api/sector-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sector-prices : Updates an existing sectorPrice.
     *
     * @param sectorPrice the sectorPrice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sectorPrice,
     * or with status 400 (Bad Request) if the sectorPrice is not valid,
     * or with status 500 (Internal Server Error) if the sectorPrice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sector-prices")
    @Timed
    public ResponseEntity<SectorPrice> updateSectorPrice(@RequestBody SectorPrice sectorPrice) throws URISyntaxException {
        log.debug("REST request to update SectorPrice : {}", sectorPrice);
        if (sectorPrice.getId() == null) {
            return createSectorPrice(sectorPrice);
        }
        SectorPrice result = sectorPriceService.save(sectorPrice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sectorPrice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sector-prices : get all the sectorPrices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sectorPrices in body
     */
    @GetMapping("/sector-prices")
    @Timed
    public ResponseEntity<List<SectorPrice>> getAllSectorPrices(Pageable pageable) {
        log.debug("REST request to get a page of SectorPrices");
        Page<SectorPrice> page = sectorPriceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sector-prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sector-prices/:id : get the "id" sectorPrice.
     *
     * @param id the id of the sectorPrice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sectorPrice, or with status 404 (Not Found)
     */
    @GetMapping("/sector-prices/{id}")
    @Timed
    public ResponseEntity<SectorPrice> getSectorPrice(@PathVariable Long id) {
        log.debug("REST request to get SectorPrice : {}", id);
        SectorPrice sectorPrice = sectorPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sectorPrice));
    }

    /**
     * DELETE  /sector-prices/:id : delete the "id" sectorPrice.
     *
     * @param id the id of the sectorPrice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sector-prices/{id}")
    @Timed
    public ResponseEntity<Void> deleteSectorPrice(@PathVariable Long id) {
        log.debug("REST request to delete SectorPrice : {}", id);
        sectorPriceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sector-prices?query=:query : search for the sectorPrice corresponding
     * to the query.
     *
     * @param query the query of the sectorPrice search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sector-prices")
    @Timed
    public ResponseEntity<List<SectorPrice>> searchSectorPrices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SectorPrices for query {}", query);
        Page<SectorPrice> page = sectorPriceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sector-prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    /**
     * GET  /sector-prices/daily : return all sector's prices for the date given
     *
     * @param id the id of the sectorPrice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sectorPrice, or with status 404 (Not Found)
     */
//    @GetMapping("/sector-prices/daily")
//    @Timed
//    public ResponseEntity<List<SectorPrice>> getAllSectorPrices(@RequestParam LocalDate publishDate) {
//        log.debug("REST request to get SectorPrice : {}", publishDate);
//        List<SectorPrice> sectorPrices = sectorPriceService.findAllByPublishDate(publishDate);
//        
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sectorPrices));
//    }
    
    /**
     * GET  /:sectorInfoId/sector-prices/daily : get the "sectorInfoId" sectorPrice for given date.
     *
     * @param id the id of the sectorPrice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sectorPrice, or with status 404 (Not Found)
     */
    @GetMapping("/{sectorInfoId}/sector-prices/daily")
    @Timed
    public ResponseEntity<SectorPrice> getSectorPrice(@PathVariable Long sectorInfoId, @RequestParam LocalDate publishDate) {
        log.debug("REST request to get SectorPrice : {}", sectorInfoId);
        //SectorPrice sectorPrice = sectorPriceService.findOne(id);
        SectorPrice sectorPrice = sectorPriceService.findAllBySectorInfoIdAndPublishDate(sectorInfoId, publishDate);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sectorPrice));
    }
    
    
//    @GetMapping("/{sectorInfoId}/sector-prices/history")
//    @Timed
//    public ResponseEntity<List<SectorPrice>> getSectorPriceHistory(@PathVariable Long sectorInfoId, @RequestParam(required=false) LocalDate startDate, @RequestParam(required=false) LocalDate endDate) {
//        log.debug("REST request to get SectorPrice : {}", sectorInfoId);
//        List<SectorPrice> sectorPrices = sectorPriceService.findAllBySectorInfoId(sectorInfoId);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sectorPrices));
//    }
}
