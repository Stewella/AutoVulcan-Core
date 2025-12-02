package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import com.prosigmaka.catra.diglett.model.entity.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    Project findByNameProject(String nameProject);

    @Query(value = "SELECT proid FROM t_project WHERE proname= :proname", 
			  nativeQuery = true)
    String findIdByName(String proname);

    @Query(value = "SELECT * FROM t_project WHERE cliid= :cliid", 
			  nativeQuery = true)
    List<Project> findProjectNameByIdClient(String cliid);

}