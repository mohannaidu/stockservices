package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mimos.stockservices.domain.SectorInfo;
import org.mimos.stockservices.service.SectorInfoService;
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
 * REST controller for managing SectorInfo.
 */
@RestController
@RequestMapping("/api/sectors")
public class SectorInfoResource {

    private final Logger log = LoggerFactory.getLogger(SectorInfoResource.class);

    private static final String ENTITY_NAME = "sectorInfo";

    private final SectorInfoService sectorInfoService;

    public SectorInfoResource(SectorInfoService sectorInfoService) {
        this.sectorInfoService = sectorInfoService;
    }

    /**
     * POST  /sector-infos : Create a new sectorInfo.
     *
     * @param sectorInfo the sectorInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sectorInfo, or with status 400 (Bad Request) if the sectorInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sector-infos")
    @Timed
    public ResponseEntity<SectorInfo> createSectorInfo(@RequestBody SectorInfo sectorInfo) throws URISyntaxException {
        log.debug("REST request to save SectorInfo : {}", sectorInfo);
        if (sectorInfo.getId() != null) {
            throw new BadRequestAlertException("A new sectorInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SectorInfo result = sectorInfoService.save(sectorInfo);
        return ResponseEntity.created(new URI("/api/sector-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sector-infos : Updates an existing sectorInfo.
     *
     * @param sectorInfo the sectorInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sectorInfo,
     * or with status 400 (Bad Request) if the sectorInfo is not valid,
     * or with status 500 (Internal Server Error) if the sectorInfo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sector-infos")
    @Timed
    public ResponseEntity<SectorInfo> updateSectorInfo(@RequestBody SectorInfo sectorInfo) throws URISyntaxException {
        log.debug("REST request to update SectorInfo : {}", sectorInfo);
        if (sectorInfo.getId() == null) {
            return createSectorInfo(sectorInfo);
        }
        SectorInfo result = sectorInfoService.save(sectorInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sectorInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sector-infos : get all the sectorInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sectorInfos in body
     */
    @GetMapping("/sector-infos")
    @Timed
    public List<SectorInfo> getAllSectorInfos() {
        log.debug("REST request to get all SectorInfos");
        return sectorInfoService.findAll();
        }

    /**
     * GET  /sector-infos/:id : get the "id" sectorInfo.
     *
     * @param id the id of the sectorInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sectorInfo, or with status 404 (Not Found)
     */
    @GetMapping("/sector-infos/{id}")
    @Timed
    public ResponseEntity<SectorInfo> getSectorInfo(@PathVariable Long id) {
        log.debug("REST request to get SectorInfo : {}", id);
        SectorInfo sectorInfo = sectorInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sectorInfo));
    }

    /**
     * DELETE  /sector-infos/:id : delete the "id" sectorInfo.
     *
     * @param id the id of the sectorInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sector-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteSectorInfo(@PathVariable Long id) {
        log.debug("REST request to delete SectorInfo : {}", id);
        sectorInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sector-infos?query=:query : search for the sectorInfo corresponding
     * to the query.
     *
     * @param query the query of the sectorInfo search
     * @return the result of the search
     */
    @GetMapping("/_search/sector-infos")
    @Timed
    public List<SectorInfo> searchSectorInfos(@RequestParam String query) {
        log.debug("REST request to search SectorInfos for query {}", query);
        return sectorInfoService.search(query);
    }

}
