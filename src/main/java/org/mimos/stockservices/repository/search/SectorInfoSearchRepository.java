package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.SectorInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SectorInfo entity.
 */
public interface SectorInfoSearchRepository extends ElasticsearchRepository<SectorInfo, Long> {
}
