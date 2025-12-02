package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.prosigmaka.catra.diglett.model.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String>{
	Optional<Client> findByNama(String nama);
	Optional<Client> findByNickname (String nama);
	
	@Query(value = "SELECT clinm FROM t_client", nativeQuery = true)
	List<Object> getClient();
	
	@Query(value = "SELECT cliid FROM t_client WHERE clinm= :clinm", nativeQuery = true)
	String findIdCli(String clinm);	
	
	@Query(value = "SELECT COUNT(cliid) FROM t_client", nativeQuery = true)
	Integer getCount();
	
	@Query(value = "SELECT COUNT(cliid)" + 
			"	FROM t_client c" + 
			"	JOIN t_industry i" + 
			"	ON c.indid=i.indid" + 
			"	GROUP BY indnm" + 
			"	ORDER BY indnm", nativeQuery = true)
	List<Object> getCountByInd();
	
	@Query(value = "SELECT indnm" + 
			"	FROM t_client c" + 
			"	JOIN t_industry i" + 
			"	ON c.indid=i.indid" + 
			"	GROUP BY indnm" + 
			"	ORDER BY indnm", nativeQuery = true)
	List<Object> getIndustry();
}
