package com.prosigmaka.catra.diglett.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prosigmaka.catra.diglett.model.entity.Industry;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Integer>{
	Optional<Industry> findByNama (String nama);
	
	@Query(value = "SELECT indnm FROM t_industry", nativeQuery = true)
	List<Object> getIndustry();
}
