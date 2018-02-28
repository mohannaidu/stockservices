package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.StockInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockInfo entity.
 */
public interface StockInfoSearchRepository extends ElasticsearchRepository<StockInfo, Long> {
}
