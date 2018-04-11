package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.apache.commons.lang.StringUtils;
import org.mimos.stockservices.domain.IndiceInfo;
import org.mimos.stockservices.domain.IndicePrice;
import org.mimos.stockservices.service.IndiceInfoService;
import org.mimos.stockservices.service.IndicePriceService;
import org.mimos.stockservices.service.dto.IndicePriceDto;
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

import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing IndicePrice.
 */
@RestController
@RequestMapping("/api/indices")
public class IndicePriceResource {

    private final Logger log = LoggerFactory.getLogger(IndicePriceResource.class);

    private static final String ENTITY_NAME = "indicePrice";
   
    private final IndicePriceService indicePriceService;
    
    private final IndiceInfoService indiceInfoService;

    public IndicePriceResource(IndicePriceService indicePriceService, IndiceInfoService indiceInfoService) {
        this.indicePriceService = indicePriceService;
        this.indiceInfoService = indiceInfoService;
    }

    /**
     * POST  /indice-prices : Create a new indicePrice.
     *
     * @param indicePrice the indicePrice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new indicePrice, or with status 400 (Bad Request) if the indicePrice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/indice-prices")
    @Timed
    public ResponseEntity<IndicePrice> createIndicePrice(@Valid @RequestBody IndicePrice indicePrice) throws URISyntaxException {
        log.debug("REST request to save IndicePrice : {}", indicePrice);
        if (indicePrice.getId() != null) {
            throw new BadRequestAlertException("A new indicePrice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndicePrice result = indicePriceService.save(indicePrice);
        return ResponseEntity.created(new URI("/api/indice-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /indice-prices : Updates an existing indicePrice.
     *
     * @param indicePrice the indicePrice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated indicePrice,
     * or with status 400 (Bad Request) if the indicePrice is not valid,
     * or with status 500 (Internal Server Error) if the indicePrice couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/indice-prices")
    @Timed
    public ResponseEntity<IndicePrice> updateIndicePrice(@Valid @RequestBody IndicePrice indicePrice) throws URISyntaxException {
        log.debug("REST request to update IndicePrice : {}", indicePrice);
        if (indicePrice.getId() == null) {
            return createIndicePrice(indicePrice);
        }
        IndicePrice result = indicePriceService.save(indicePrice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, indicePrice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /indice-prices : get all the indicePrices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of indicePrices in body
     */
    @GetMapping("/indice-prices")
    @Timed
    public ResponseEntity<List<IndicePrice>> getAllIndicePrices(Pageable pageable) {
        log.debug("REST request to get a page of IndicePrices");
        Page<IndicePrice> page = indicePriceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/indices/indice-prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("{indiceInfoId}/indice-prices/history")
    @Timed
    public ResponseEntity<List<IndicePriceDto>> getAllIndicePricesByIndiceInfoId(@PathVariable Long indiceInfoId) {
        log.debug("REST request to get historical data of IndicePrices");
        List<IndicePriceDto> indiceHistory = indicePriceService.findAllByIndiceInfoId(indiceInfoId).stream().map(IndicePriceDto::new).collect(Collectors.toList());;
        return new ResponseEntity<>(indiceHistory, HttpStatus.OK);
    }
    
    @GetMapping("/indice-prices/history")
    @Timed
    public ResponseEntity<Map> getAllIndicePricesByIndiceInfoSymbol(@RequestParam(required=true) String indiceSymbol, @RequestParam(required=false) LocalDate startDate, @RequestParam(required=false) LocalDate endDate) {
        log.debug("REST request to get historical data of IndicePrices");
        IndiceInfo info = indiceInfoService.findBySymbol(indiceSymbol);
        Map r = new HashMap();
        
        if (info != null) {
        	r.put("id", info.getId());
        	r.put("name", info.getName());
        	r.put("code", info.getSymbol());
        	
        	List<IndicePriceDto> indiceHistory = indicePriceService.findAllByIndiceInfoSymbolAndPublishDateBetween(indiceSymbol, startDate, endDate).stream().map(IndicePriceDto::new).collect(Collectors.toList());;
        	r.put("values", indiceHistory);
        }
        
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    /**
     * GET  /indice-prices/:id : get the "id" indicePrice.
     *
     * @param id the id of the indicePrice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the indicePrice, or with status 404 (Not Found)
     */
    @GetMapping("/indice-prices/{id}")
    @Timed
    public ResponseEntity<IndicePrice> getIndicePrice(@PathVariable Long id) {
        log.debug("REST request to get IndicePrice : {}", id);
        IndicePrice indicePrice = indicePriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(indicePrice));
    }

    /**
     * DELETE  /indice-prices/:id : delete the "id" indicePrice.
     *
     * @param id the id of the indicePrice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/indice-prices/{id}")
    @Timed
    public ResponseEntity<Void> deleteIndicePrice(@PathVariable Long id) {
        log.debug("REST request to delete IndicePrice : {}", id);
        indicePriceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/indice-prices?query=:query : search for the indicePrice corresponding
     * to the query.
     *
     * @param query the query of the indicePrice search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/indice-prices")
    @Timed
    public ResponseEntity<List<IndicePrice>> searchIndicePrices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IndicePrices for query {}", query);
        Page<IndicePrice> page = indicePriceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/indice-prices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
