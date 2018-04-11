package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.SectorPrice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SectorPrice entity.
 */
public interface SectorPriceSearchRepository extends ElasticsearchRepository<SectorPrice, Long> {
}
