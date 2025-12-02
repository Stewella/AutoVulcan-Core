package com.prosigmaka.catra.diglett.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

import com.prosigmaka.catra.diglett.model.entity.PlottingDetail;
import com.prosigmaka.catra.diglett.model.projection.PlottingDetailProjection;
import org.springframework.stereotype.Repository;

@Repository
public interface PlottingDetailRepository extends JpaRepository<PlottingDetail, String> {

    @Query(value = " SELECT pltdet.tgl FROM t_plt_detail pltdet " + " JOIN t_plotting plt ON pltdet.pltid = plt.pltid "
            + " WHERE pltdet.pltid= :pltid AND plt.dkbid= :dkbid "
            + " ORDER BY pltdet.tgl LIMIT 1 ", nativeQuery = true)
    Date findFirstTestDate(String pltid, String dkbid);

    @Query
    (value ="SELECT pltdum.id id, pltdum.pltid pltid, stpdet.steporder orderstep, stp.stepnm kegiatan, pltdum.result result, "
    + " pltdum.keterangan keterangan, pltdum.tgl tanggal, pos.posnm kebutuhan, pic.picname picname, cli.clinm client "
    + " FROM "
    + " (SELECT pltdet.pltdetid id, pltdet.pltid pltid, pltdet.tgl tgl, "
    + " pltdet.stepid stepid, pltdet.temp_ket keterangan, pltdet.temp_res result, "
    + " row_number() over (partition by pltid order by pltdetid) as rownum "
    + " FROM t_plt_detail pltdet "
    + " WHERE pltdet.temp_res IS NULL) AS pltdum "
    + " JOIN t_plotting plt ON pltdum.pltid=plt.pltid "
    + " JOIN t_step stp ON pltdum.stepid=stp.stepid "
    + " JOIN t_step_detail stpdet ON stp.stepid=stpdet.stepid "
    + " JOIN t_det_keb dkb ON plt.dkbid=dkb.dkbid "
    + " JOIN t_pic_client pic ON dkb.picid=pic.picid "
    + " JOIN t_client cli ON pic.cliid=cli.cliid "
    + " JOIN t_posisi pos ON dkb.posid=pos.posid "
    + " WHERE plt.cndid= :cndid AND cli.cliid=stpdet.cliid AND rownum=1 "
    + " ORDER BY client, orderstep ", nativeQuery = true)
    List<PlottingDetailProjection> findPlottingDetailByCandidate(String cndid);

    @Query(value = "SELECT stepdetid FROM t_step_detail	WHERE stepid= :stepid", nativeQuery = true)
    List<String> findStepDetailIdByStepId(String stepid);

    @Query(value = "SELECT pltdetid FROM t_plt_detail WHERE stepid= :stepid", nativeQuery = true)
    List<String> findPlotDetailIdByStepId(String stepid);
    
    @Query(value = "SELECT * FROM t_plt_detail WHERE pltid= :pltid AND temp_res IS NULL", nativeQuery = true)
    List<PlottingDetail> findPlotDetailIdByPltId(String pltid);
    
    @Query(value = 
    		"SELECT pltdum.id id, pltdum.pltid pltid, stpdet.steporder orderstep, stp.stepnm kegiatan, pltdum.result result,"
    				+ " pltdum.keterangan keterangan, pltdum.tgl tanggal, pos.posnm kebutuhan, pic.picname picname, cli.clinm client"
    				+ " FROM"
    				+ " ("
    				+ " SELECT *, row_number() over (partition by dummy.pltid order by dummy.pltid) as rownum"
    				+ " FROM"
    				+ " (SELECT pltdet.pltdetid id, pltdet.pltid pltid, pltdet.tgl tgl,"
    				+ " pltdet.stepid stepid, pltdet.temp_ket keterangan, pltdet.temp_res result"
    		+ " FROM t_plt_detail pltdet"
    		+ " INNER JOIN"
    		+ " (SELECT  pltdet.pltid pltid, max(pltdet.tgl) tgl"
    		+ " FROM t_plt_detail pltdet"
    		+ " GROUP BY pltid) AS dumdum"
    		+ " ON pltdet.pltid = dumdum.pltid AND pltdet.tgl = dumdum.tgl"
    		+ " ORDER BY pltid) AS dummy"
    		+ " ) AS pltdum"
    		+ " JOIN t_plotting plt ON pltdum.pltid=plt.pltid"
    		+ " JOIN t_step stp ON pltdum.stepid=stp.stepid"
    		+ " JOIN t_step_detail stpdet ON stp.stepid=stpdet.stepid"
    		+ " JOIN t_det_keb dkb ON plt.dkbid=dkb.dkbid"
    		+ " JOIN t_pic_client pic ON dkb.picid=pic.picid"
    		+ " JOIN t_client cli ON pic.cliid=cli.cliid"
    		+ " JOIN t_posisi pos ON dkb.posid=pos.posid"
    		+ " WHERE plt.cndid= :cndid  AND cli.cliid=stpdet.cliid AND rownum=1"
    		+ " ORDER BY pltid"
    		, nativeQuery = true)
    List<PlottingDetailProjection> findLastPlottingDetailByCandidate(String cndid);
}
