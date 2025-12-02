package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prosigmaka.catra.diglett.model.entity.PicClient;
import org.springframework.stereotype.Repository;

@Repository
public interface PicClientRepository extends JpaRepository<PicClient, String> {
	PicClient findByPicName(String picName);
	
	List<PicClient> findPicClientByIdClient(String idClient);

	
}
