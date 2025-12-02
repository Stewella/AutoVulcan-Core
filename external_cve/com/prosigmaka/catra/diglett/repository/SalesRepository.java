package com.prosigmaka.catra.diglett.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prosigmaka.catra.diglett.model.entity.Sales;

import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Integer> {
	Optional<Sales> findByNama(String Nama);

	
//	@Query(value = "SELECT indnm FROM t_industry", nativeQuery = true)
//	List<Object> getIndustry();

}
