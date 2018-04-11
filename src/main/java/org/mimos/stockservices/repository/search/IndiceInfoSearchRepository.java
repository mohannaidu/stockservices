package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.IndiceInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IndiceInfo entity.
 */
public interface IndiceInfoSearchRepository extends ElasticsearchRepository<IndiceInfo, Long> {
}
