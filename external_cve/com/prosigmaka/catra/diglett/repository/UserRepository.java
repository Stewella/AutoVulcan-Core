package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByIsDeleteEquals(Integer isDelete);

    User findByIdUserEquals(String id);
}

