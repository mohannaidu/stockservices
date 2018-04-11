package org.mimos.stockservices.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import io.github.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.mimos.stockservices.service.dto.MetadataDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;

@FeignClient("sentimentservices")
public interface SentimentServiceClient {
	
//	@PostMapping("/api")
//    @Timed
//    public ResponseEntity<MetadataDto> createMetadata(@RequestBody MetadataDto metadata);
//
//    /**
//     * PUT  /metadata : Updates an existing metadata.
//     *
//     * @param metadata the metadata to update
//     * @return the ResponseEntity with status 200 (OK) and with body the updated metadata,
//     * or with status 400 (Bad Request) if the metadata is not valid,
//     * or with status 500 (Internal Server Error) if the metadata couldn't be updated
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PutMapping("/api")
//    @Timed
//    public ResponseEntity<MetadataDto> updateMetadata(@RequestBody MetadataDto metadata);

    /**
     * GET  /metadata : get getTotalArticleBySector
     *
     * @return the ResponseEntity with status 200 (OK) and the list of metadata in body
     */
    @GetMapping("/api/getTotalArticleBySector/{sector}")
    @Timed
    public int getTotalArticleBySector(@PathVariable("sector") String sector);
    
    /**
     * GET  /metadata : get getTotalArticleByStockName
     *
     * @return the ResponseEntity with status 200 (OK) and the list of metadata in body
     */
    @GetMapping("/api/getTotalArticleByStockName/{stockName}")
    @Timed
    public Long getTotalArticleByStockName(@PathVariable("stockName") String stockName);
//    
//    /**
//     * GET  /metadata : get getArticleByStockCode
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of metadata in body
//     */
//    @GetMapping("/metadata/getArticleByStockCode/{stockCode}")
//    @Timed
//    public List<MetadataDto> getArticleByStockCode(@PathVariable("stockCode") String stockCode);
//    
//    /**
//     * GET  /metadata : get getLatestNewsBySector
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of metadata in body
//     */
//    @GetMapping("/metadata/getLatestNewsBySector/{sector}")
//    @Timed
//    public List<MetadataDto> getLatestNewsBySector(@PathVariable("sector") String sector);
//    
//    
//    /**
//     * GET  /metadata : get all the metadata.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of metadata in body
//     */
//    @GetMapping("/metadata")
//    @Timed
//    public List<MetadataDto> getAllMetadata();
//
//    /**
//     * GET  /metadata/:id : get the "id" metadata.
//     *
//     * @param id the id of the metadata to retrieve
//     * @return the ResponseEntity with status 200 (OK) and with body the metadata, or with status 404 (Not Found)
//     */
//    @GetMapping("/metadata/{id}")
//    @Timed
//    public ResponseEntity<MetadataDto> getMetadata(@PathVariable("id") String id);
//
//    /**
//     * DELETE  /metadata/:id : delete the "id" metadata.
//     *
//     * @param id the id of the metadata to delete
//     * @return the ResponseEntity with status 200 (OK)
//     */
//    @DeleteMapping("/metadata/{id}")
//    @Timed
//    public ResponseEntity<Void> deleteMetadata(@PathVariable("id") String id);
//
//    /**
//     * SEARCH  /_search/metadata?query=:query : search for the metadata corresponding
//     * to the query.
//     *
//     * @param query the query of the metadata search
//     * @return the result of the search
//     */
//    @GetMapping("/_search/metadata")
//    @Timed
//    public List<MetadataDto> searchMetadata(@RequestParam("query") String query);
    
}
