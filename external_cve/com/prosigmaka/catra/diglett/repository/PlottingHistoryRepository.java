package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prosigmaka.catra.diglett.model.entity.PlottingHistory;
import com.prosigmaka.catra.diglett.model.projection.OnProccessProjection;
import com.prosigmaka.catra.diglett.model.projection.WinProjection;

@Repository
public interface PlottingHistoryRepository extends JpaRepository<PlottingHistory, String>{
	List<PlottingHistory> findPlottingHistoryByIdCandidate(String IdCandidate);
	
	@Query(value = "select t_candidate.cndid as id, t_candidate.cndnm as nama, t_plt_hist.plthistid as idPltHist, t_plt_hist.dkbid as idDetKebutuhan, t_plt_hist.tgl_plthist as tglKeputusan from t_plt_hist left join t_candidate ON t_plt_hist.cndid = t_candidate.cndid where dkbid = ?", nativeQuery = true)
	List<WinProjection> findNameListByDkbId(String dkbId);
	
	@Query(value="SELECT pltdum.id id, pltdum.pltid pltId, plt.cndid cndId, plt.dkbid dkbId, hist.plthistid pltHistId, cnd.cndnm nama, pltdum.result result, pltdum.tgl tanggal\r\n" + 
			"             FROM\r\n" + 
			"             (SELECT *, row_number() over (partition by dummy.pltid order by dummy.pltid desc) as rownum\r\n" + 
			"             FROM\r\n" + 
			"             (SELECT pltdet.pltdetid id, pltdet.pltid pltid, pltdet.tgl tgl,\r\n" + 
			"             pltdet.stepid stepid, pltdet.temp_ket keterangan, pltdet.temp_res result\r\n" + 
			"         FROM t_plt_detail pltdet\r\n" + 
			"        INNER JOIN\r\n" + 
			"         (SELECT  pltdet.pltid pltid, max(pltdet.tgl) tgl\r\n" + 
			"         FROM t_plt_detail pltdet\r\n" + 
			"         GROUP BY pltid) AS dumdum\r\n" + 
			"         ON pltdet.pltid = dumdum.pltid AND pltdet.tgl = dumdum.tgl\r\n" + 
			"         ORDER BY pltid) AS dummy) AS pltdum\r\n" + 
			"         JOIN t_plotting plt ON pltdum.pltid=plt.pltid\r\n" + 
			"         JOIN t_step stp ON pltdum.stepid=stp.stepid\r\n" + 
			"         JOIN t_step_detail stpdet ON stp.stepid=stpdet.stepid\r\n" + 
			"         JOIN t_det_keb dkb ON plt.dkbid=dkb.dkbid\r\n" + 
			"         JOIN t_pic_client pic ON dkb.picid=pic.picid\r\n" + 
			"         JOIN t_client cli ON pic.cliid=cli.cliid\r\n" + 
			"         JOIN t_posisi pos ON dkb.posid=pos.posid\r\n" + 
			"		 left join t_plt_hist hist on plt.cndid = hist.cndid\r\n" + 
			"		 left join t_candidate cnd on plt.cndid = cnd.cndid\r\n" + 
			"         WHERE cli.cliid=stpdet.cliid AND rownum=1 AND plt.dkbid = ? \r\n" + 
			"		 AND\r\n" + 
			"		 (\r\n" + 
			"			 pltdum.result != 'NO'\r\n" + 
			"			 or \r\n" + 
			"			 pltdum.result is null)\r\n" + 
			"		AND hist.plthistid is null\r\n" + 
			"         ORDER BY pltid\r\n" + 
			"", nativeQuery = true)
	List<OnProccessProjection> getAll(String dkbId);
	
	
	
}
