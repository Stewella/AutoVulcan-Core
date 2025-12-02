package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prosigmaka.catra.diglett.model.entity.RecommendPosisi;
import com.prosigmaka.catra.diglett.model.projection.DashboardTechProjection;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendPosisiRepository extends JpaRepository<RecommendPosisi, String> {
	List<RecommendPosisi> findRecommendPosisiByIdCandidate(String idCandidate);
	
	@Query(value="select count(pos.posid) as jumlah, posnm as posisi\r\n" + 
			"from t_recposisi rec left join t_posisi pos on pos.posid = rec.posid\r\n" + 
			"group by posnm", nativeQuery = true)
	List<DashboardTechProjection> findCountPosisiForDashboardTech();

}
