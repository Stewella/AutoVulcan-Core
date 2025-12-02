package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.EmployeeDocUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeDocUploadRepository extends JpaRepository<EmployeeDocUpload, Integer> {
    List<EmployeeDocUpload> findAllByIdEqualsAndIsDeleteEquals(String idEmployee, Integer isDelete);
    EmployeeDocUpload findByIdUpload(Integer id);
}
