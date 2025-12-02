package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.StatusAvailable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusAvailableRepository extends JpaRepository<StatusAvailable, Long> {
    List<StatusAvailable> findAllByGrupAvailableEquals(String grup);
}
