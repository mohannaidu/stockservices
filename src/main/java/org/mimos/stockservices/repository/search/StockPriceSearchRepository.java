package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.StockPrice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockPrice entity.
 */
public interface StockPriceSearchRepository extends ElasticsearchRepository<StockPrice, Long> {
}
