package com.prosigmaka.catra.diglett.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.prosigmaka.catra.diglett.model.entity.Plotting;
import org.springframework.stereotype.Repository;

@Repository
public interface PlottingRepository extends JpaRepository<Plotting, String> {
    
    List<Plotting> findAllByCandidateId(String idCandidate);
    
    @Query(value = "select count(tp.pltid) from t_plotting tp \r\n" + 
    		"where tp.dkbid = :detBid", nativeQuery = true)
    Integer plotting(String detBid);
    
    @Query(value = "select count(tp.pltid) as jumlah  \r\n" + 
    		"from t_plotting tp \r\n" + 
    		"join t_candidate tc \r\n" + 
    		"on tp.cndid = tc.cndid \r\n" + 
    		"where tp.dkbid = :detBid and tc.cndavail = 'Hired'", nativeQuery = true)
    Integer winPlot(String detBid);
    
    @Query(value = "select count(tpd.pltid) \r\n" + 
    		"from t_plt_detail tpd \r\n" + 
    		"join t_plotting tp \r\n" + 
    		"on tpd.pltid = tp.pltid \r\n" + 
    		"join t_candidate tc \r\n" + 
    		"on tp.cndid = tc.cndid \r\n" + 
    		"join (select max(tpd.pltdetid) as id\r\n" + 
    		"from t_plt_detail tpd\r\n" + 
    		"group by tpd.pltid \r\n" + 
    		") as maks\r\n" + 
    		"on tpd.pltdetid = maks .id\r\n" + 
    		"where \r\n" + 
    		"tp.dkbid  = :detBid \r\n" + 
    		"and \r\n" + 
    		"tpd.temp_res = 'NO' ", nativeQuery = true)
    Integer dropPlot(String detBid);
    
    
    

}
