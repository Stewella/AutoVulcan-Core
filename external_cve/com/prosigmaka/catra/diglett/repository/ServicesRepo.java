package com.prosigmaka.catra.diglett.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prosigmaka.catra.diglett.model.entity.Services;

@Repository
public interface ServicesRepo extends JpaRepository<Services, String> {
	
	@Query(value = "select * from t_service ts " + 
			"where ts.servnm = :service", nativeQuery = true)
	Services findService(String service);
	
	Services findByService(String service);

}
