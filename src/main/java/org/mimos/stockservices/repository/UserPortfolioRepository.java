package org.mimos.stockservices.repository;

import org.mimos.stockservices.domain.UserPortfolio;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the UserPortfolio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, Long> {
    
//	@Query("select u from UserPortfolio u where u.userid=:userid")
//	UserPortfolio findByUserid(String userid);
//	
//	@Query("select distinct user_portfolio from UserPortfolio user_portfolio left join fetch user_portfolio.favStocks")
//    List<UserPortfolio> findAllWithEagerRelationships();
//
//    @Query("select user_portfolio from UserPortfolio user_portfolio left join fetch user_portfolio.favStocks where user_portfolio.id =:id")
//    UserPortfolio findOneWithEagerRelationships(@Param("id") Long id);
	
	@Query("select up from UserPortfolio up where up.username = :username")
	List<UserPortfolio> findAllByUsername(@Param("username") String username);
	
}
