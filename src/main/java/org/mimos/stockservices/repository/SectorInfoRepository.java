package org.mimos.stockservices.repository;

import org.mimos.stockservices.domain.SectorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the SectorInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectorInfoRepository extends JpaRepository<SectorInfo, Long> {

	public SectorInfo findOneByCode(String code);	
	

}
