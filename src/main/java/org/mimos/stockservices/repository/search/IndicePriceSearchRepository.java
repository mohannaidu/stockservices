package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.IndicePrice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IndicePrice entity.
 */
public interface IndicePriceSearchRepository extends ElasticsearchRepository<IndicePrice, Long> {
}
