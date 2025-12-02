package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findByIdEmployeeEquals(String id);
//    List<Employee> findAllByIsDeleteEquals(Integer isDelete);
}
