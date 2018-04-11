package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mimos.stockservices.domain.IndiceInfo;
import org.mimos.stockservices.service.IndiceInfoService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing IndiceInfo.
 */
@RestController
@RequestMapping("/api/indices")
public class IndiceInfoResource {

    private final Logger log = LoggerFactory.getLogger(IndiceInfoResource.class);

    private static final String ENTITY_NAME = "indiceInfo";

    private final IndiceInfoService indiceInfoService;

    public IndiceInfoResource(IndiceInfoService indiceInfoService) {
        this.indiceInfoService = indiceInfoService;
    }

    /**
     * POST  /indice-infos : Create a new indiceInfo.
     *
     * @param indiceInfo the indiceInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new indiceInfo, or with status 400 (Bad Request) if the indiceInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/indice-infos")
    @Timed
    public ResponseEntity<IndiceInfo> createIndiceInfo(@RequestBody IndiceInfo indiceInfo) throws URISyntaxException {
        log.debug("REST request to save IndiceInfo : {}", indiceInfo);
        if (indiceInfo.getId() != null) {
            throw new BadRequestAlertException("A new indiceInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndiceInfo result = indiceInfoService.save(indiceInfo);
        return ResponseEntity.created(new URI("/api/indice-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /indice-infos : Updates an existing indiceInfo.
     *
     * @param indiceInfo the indiceInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated indiceInfo,
     * or with status 400 (Bad Request) if the indiceInfo is not valid,
     * or with status 500 (Internal Server Error) if the indiceInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/indice-infos")
    @Timed
    public ResponseEntity<IndiceInfo> updateIndiceInfo(@RequestBody IndiceInfo indiceInfo) throws URISyntaxException {
        log.debug("REST request to update IndiceInfo : {}", indiceInfo);
        if (indiceInfo.getId() == null) {
            return createIndiceInfo(indiceInfo);
        }
        IndiceInfo result = indiceInfoService.save(indiceInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, indiceInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /indice-infos : get all the indiceInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of indiceInfos in body
     */
    @GetMapping("/indice-infos/page")
    @Timed
    public ResponseEntity<List<IndiceInfo>> getAllIndiceInfosPageable(Pageable pageable) {
        log.debug("REST request to get a page of IndiceInfos");
        Page<IndiceInfo> page = indiceInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/indice-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/indice-infos")
    @Timed
    public ResponseEntity<List<IndiceInfo>> getAllIndiceInfos() {
        log.debug("REST request to get a page of IndiceInfos");
        List<IndiceInfo> indiceInfos = indiceInfoService.findAll();
        return new ResponseEntity<>(indiceInfos, HttpStatus.OK);
    }

    /**
     * GET  /indice-infos/:id : get the "id" indiceInfo.
     *
     * @param id the id of the indiceInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the indiceInfo, or with status 404 (Not Found)
     */
    @GetMapping("/indice-infos/{id}")
    @Timed
    public ResponseEntity<IndiceInfo> getIndiceInfo(@PathVariable Long id) {
        log.debug("REST request to get IndiceInfo : {}", id);
        IndiceInfo indiceInfo = indiceInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(indiceInfo));
    }

    /**
     * DELETE  /indice-infos/:id : delete the "id" indiceInfo.
     *
     * @param id the id of the indiceInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/indice-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteIndiceInfo(@PathVariable Long id) {
        log.debug("REST request to delete IndiceInfo : {}", id);
        indiceInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/indice-infos?query=:query : search for the indiceInfo corresponding
     * to the query.
     *
     * @param query the query of the indiceInfo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/indice-infos")
    @Timed
    public ResponseEntity<List<IndiceInfo>> searchIndiceInfos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IndiceInfos for query {}", query);
        Page<IndiceInfo> page = indiceInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/indice-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
