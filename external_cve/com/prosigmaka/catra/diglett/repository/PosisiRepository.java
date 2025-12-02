package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.Posisi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosisiRepository extends JpaRepository<Posisi,String>{
    Posisi findByPosisi(String Posisi);
}
