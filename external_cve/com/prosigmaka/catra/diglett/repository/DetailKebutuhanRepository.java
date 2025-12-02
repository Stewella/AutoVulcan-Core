package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prosigmaka.catra.diglett.model.dto.OptiDto;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;
import com.prosigmaka.catra.diglett.model.projection.KebutuhanHistoryProjection;
import com.prosigmaka.catra.diglett.model.projection.OptiProjection;

@Repository
public interface DetailKebutuhanRepository extends JpaRepository<DetailKebutuhan, String> {
	DetailKebutuhan findByKode(String kode);

	List<DetailKebutuhan> findByIdPic(String idPic);

	@Query(value = "SELECT cli.clinm client, pic.picname picname, pos.posnm kebutuhan, plt.createdon, plttgl.tgl tgl, plt.keterangan keterangan"
			+ " FROM " + " (SELECT pltdet.pltid id, min(pltdet.tgl) tgl " + " FROM t_plt_detail pltdet "
			+ " GROUP BY id) AS plttgl " + " FULL JOIN t_plotting plt ON plttgl.id=plt.pltid "
			+ " JOIN t_det_keb dkb ON plt.dkbid=dkb.dkbid " + " JOIN t_pic_client pic ON dkb.picid=pic.picid "
			+ " JOIN t_posisi pos ON dkb.posid=pos.posid " + " JOIN t_client cli ON pic.cliid=cli.cliid "
			+ " WHERE plt.cndid= :cndid" + " ORDER BY client, picname ", nativeQuery = true)

	List<KebutuhanHistoryProjection> findKebutuhanHistoryByCandidate(String cndid);

	@Query(value = "SELECT detkeb.dkbid, detkeb.picid, detkeb.posid, detkeb.servid, detkeb.jumlah, detkeb.kode, detkeb.level"
			+ " FROM t_det_keb detkeb" + " JOIN t_plotting plt ON detkeb.dkbid=plt.dkbid"
			+ " WHERE jumlah > 0 AND plt.cndid= :cndid", nativeQuery = true)

	List<DetailKebutuhan> findAllKeb(String cndid);

	@Query(value = "SELECT * FROM t_det_keb WHERE jumlah > 0 "

			, nativeQuery = true)

	List<DetailKebutuhan> findAllKebAvail();
	
	@Query(value ="select tc.clinm namaClient , tdk.dkbid dkbid, tp2.posnm posisi, tdk.jumlah sisaKebutuhan, \r\n" + 
			"count(1) - count(tpd.temp_res) onProcess,\r\n" + 
			"count(case when tpd.temp_res = 'NO' then 1 else null end) as drop,\r\n" + 
			"b.win\r\n" + 
			"from t_client tc \r\n" + 
			"inner join t_pic_client tpc on tpc.cliid = tc.cliid \r\n" + 
			"inner join t_det_keb tdk  on tdk.picid = tpc.picid \r\n" + 
			"left join t_plotting tp on tdk.dkbid = tp.dkbid \r\n" + 
			"left join t_plt_detail tpd on tpd.pltid = tp.pltid \r\n" + 
			"left join t_posisi tp2 on tp2.posid = tdk.posid\r\n" + 
			"inner join \r\n" + 
			"(select max(tpd2.pltdetid) as pltdetid from t_plt_detail tpd2 \r\n" + 
			"group by tpd2.pltid ) as a on a.pltdetid = tpd.pltdetid \r\n" + 
			"left join \r\n" + 
			"(select tph2.dkbid dkbid,count(tph2.plthistid) as win from t_plt_hist tph2\r\n" + 
			"group by tph2.dkbid) as b on b.dkbid = tdk.dkbid \r\n" + 
			"group by tc.clinm, tdk.dkbid, tp2.posnm, tdk.jumlah,  b.win", nativeQuery = true)
	
	List<OptiProjection> findopti();
	
	
}