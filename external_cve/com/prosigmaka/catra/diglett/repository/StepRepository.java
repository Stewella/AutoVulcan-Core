package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.Step;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<Step, String> {
    Step findByNamaStep(String stepnm);
}
