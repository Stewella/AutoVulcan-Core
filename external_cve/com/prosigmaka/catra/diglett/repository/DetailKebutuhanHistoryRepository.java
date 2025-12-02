package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhanHistory;

@Repository
public interface DetailKebutuhanHistoryRepository extends JpaRepository<DetailKebutuhanHistory, Integer> {
	Optional<DetailKebutuhanHistory> findByIdDetKeb(String id);
	
	
}
