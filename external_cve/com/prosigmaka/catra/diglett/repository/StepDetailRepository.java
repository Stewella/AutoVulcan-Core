package com.prosigmaka.catra.diglett.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.prosigmaka.catra.diglett.model.entity.StepDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface StepDetailRepository extends JpaRepository<StepDetail, String> {

	List<StepDetail> findAllByClientId(String cliid);

	@Query(value = "SELECT stepdetid FROM t_step_detail	WHERE stepid= :stepid", nativeQuery = true)
	List<String> findStepDetailIdByStepId(String stepid);
}