package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.LockUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockUserRepository extends JpaRepository<LockUser, Integer> {
}
