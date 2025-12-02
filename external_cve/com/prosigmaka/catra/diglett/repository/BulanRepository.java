package com.prosigmaka.catra.diglett.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prosigmaka.catra.diglett.model.entity.Bulan;

@Repository
public interface BulanRepository extends JpaRepository<Bulan, Integer> {

}
