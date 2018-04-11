package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.MarketInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MarketInfo entity.
 */
public interface MarketInfoSearchRepository extends ElasticsearchRepository<MarketInfo, Long> {
}
