package org.mimos.stockservices.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mimos.stockservices.domain.Sector;

import org.mimos.stockservices.repository.SectorRepository;
import org.mimos.stockservices.repository.search.SectorSearchRepository;
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
 * REST controller for managing Sector.
 */
@RestController
@RequestMapping("/api")
public class SectorResource {

    private final Logger log = LoggerFactory.getLogger(SectorResource.class);

    private static final String ENTITY_NAME = "sector";

    private final SectorRepository sectorRepository;

    private final SectorSearchRepository sectorSearchRepository;

    public SectorResource(SectorRepository sectorRepository, SectorSearchRepository sectorSearchRepository) {
        this.sectorRepository = sectorRepository;
        this.sectorSearchRepository = sectorSearchRepository;
    }

    /**
     * POST  /sectors : Create a new sector.
     *
     * @param sector the sector to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sector, or with status 400 (Bad Request) if the sector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sectors")
    @Timed
    public ResponseEntity<Sector> createSector(@RequestBody Sector sector) throws URISyntaxException {
        log.debug("REST request to save Sector : {}", sector);
        if (sector.getId() != null) {
            throw new BadRequestAlertException("A new sector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sector result = sectorRepository.save(sector);
        sectorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sectors : Updates an existing sector.
     *
     * @param sector the sector to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sector,
     * or with status 400 (Bad Request) if the sector is not valid,
     * or with status 500 (Internal Server Error) if the sector couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sectors")
    @Timed
    public ResponseEntity<Sector> updateSector(@RequestBody Sector sector) throws URISyntaxException {
        log.debug("REST request to update Sector : {}", sector);
        if (sector.getId() == null) {
            return createSector(sector);
        }
        Sector result = sectorRepository.save(sector);
        sectorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sector.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sectors : get all the sectors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sectors in body
     */
    @GetMapping("/sectors")
    @Timed
    public List<Sector> getAllSectors() {
        log.debug("REST request to get all Sectors");
        return sectorRepository.findAll();
        }

    /**
     * GET  /sectors/:id : get the "id" sector.
     *
     * @param id the id of the sector to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sector, or with status 404 (Not Found)
     */
    @GetMapping("/sectors/{id}")
    @Timed
    public ResponseEntity<Sector> getSector(@PathVariable Long id) {
        log.debug("REST request to get Sector : {}", id);
        Sector sector = sectorRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sector));
    }

    /**
     * DELETE  /sectors/:id : delete the "id" sector.
     *
     * @param id the id of the sector to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sectors/{id}")
    @Timed
    public ResponseEntity<Void> deleteSector(@PathVariable Long id) {
        log.debug("REST request to delete Sector : {}", id);
        sectorRepository.delete(id);
        sectorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sectors?query=:query : search for the sector corresponding
     * to the query.
     *
     * @param query the query of the sector search
     * @return the result of the search
     */
    @GetMapping("/_search/sectors")
    @Timed
    public List<Sector> searchSectors(@RequestParam String query) {
        log.debug("REST request to search Sectors for query {}", query);
        return StreamSupport
            .stream(sectorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
