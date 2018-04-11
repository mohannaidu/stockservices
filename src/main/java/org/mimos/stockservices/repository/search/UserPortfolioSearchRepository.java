package org.mimos.stockservices.repository.search;

import org.mimos.stockservices.domain.UserPortfolio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserPortfolio entity.
 */
public interface UserPortfolioSearchRepository extends ElasticsearchRepository<UserPortfolio, Long> {
}
