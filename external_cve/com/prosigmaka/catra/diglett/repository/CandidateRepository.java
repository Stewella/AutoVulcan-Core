package com.prosigmaka.catra.diglett.repository;

import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.projection.AllCandidateByStatus;
import com.prosigmaka.catra.diglett.model.projection.AvailableCandidateProjectionLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
	List<Candidate> findAllByAvailKandidatEqualsAndIsDeleteEquals(String availKandidat, Integer isDelete);

	Candidate findByIdEquals(String id);

	Candidate findByNama(String nama);

	@Query(value = "SELECT cndid FROM t_candidate WHERE cndnm= :cndnm",
			nativeQuery=true)
	String findIdCnd(String cndnm);

	@Query(value = "SELECT count(cndid) from t_candidate", nativeQuery = true)
	Integer totalCandidate();

	@Query(value = "SELECT count(cndid) from t_candidate where cndjk='Laki-laki'", nativeQuery = true)
	Integer MaleCandidate();

	@Query(value = "SELECT count(cndid) from t_candidate where cndjk='Perempuan'", nativeQuery = true)
	Integer FemaleCandidate();

	@Query(value = "SELECT p.posnm, COUNT(last.posid) total \r\n" + "	FROM (SELECT latest.cndid, hp.posid \r\n"
			+ "		  	FROM (SELECT cndid, MAX(tglend) max \r\n"
			+ "				  	FROM t_hist_posisi GROUP BY cndid) latest\r\n"
			+ "			JOIN t_hist_posisi hp ON latest.max=hp.tglend AND hp.cndid=latest.cndid) last\r\n"
			+ "JOIN t_posisi p ON p.posid = last.posid \r\n"
			+ "GROUP BY last.posid, p.posnm ORDER BY last.posid", nativeQuery = true)
	List<Object> candidateByPosisi();

	@Query(value = "SELECT cl.clunm\r\n" + "	FROM (SELECT latest.cndid, hp.posid \r\n"
			+ "		  	FROM (SELECT cndid, MAX(tglend) max \r\n"
			+ "				  	FROM t_hist_posisi GROUP BY cndid) latest\r\n"
			+ "			JOIN t_hist_posisi hp ON latest.max=hp.tglend AND hp.cndid=latest.cndid) last\r\n"
			+ "JOIN t_posisi p ON p.posid = last.posid \r\n" + "JOIN t_cluster cl ON p.cluid = cl.cluid\r\n"
			+ "GROUP BY cl.clunm\r\n" + "ORDER BY cl.clunm", nativeQuery = true)
	List<Object> nameCluster();

	@Query(value = "SELECT COUNT(cl.clunm)\r\n" + "	FROM (SELECT latest.cndid, hp.posid \r\n"
			+ "		  	FROM (SELECT cndid, MAX(tglend) max \r\n"
			+ "				  	FROM t_hist_posisi GROUP BY cndid) latest\r\n"
			+ "			JOIN t_hist_posisi hp ON latest.max=hp.tglend AND hp.cndid=latest.cndid) last\r\n"
			+ "JOIN t_posisi p ON p.posid = last.posid \r\n" + "JOIN t_cluster cl ON p.cluid = cl.cluid\r\n"
			+ "GROUP BY cl.clunm\r\n" + "ORDER BY cl.clunm", nativeQuery = true)
	List<Integer> countCluster();
	
	@Query(value = "SELECT s.sklnm FROM t_skill_detail sd\r\n" + 
			"JOIN t_skill s ON s.sklid = sd.sklid\r\n" + 
			"GROUP BY s.sklnm, s.sklid\r\n" + 
			"ORDER BY s.sklid", nativeQuery = true)
	List<String> nameSkill();
	
	@Query(value = "SELECT COUNT(sd.cndid) FROM t_skill_detail sd\r\n" + 
			"JOIN t_skill s ON s.sklid = sd.sklid\r\n" + 
			"GROUP BY s.sklnm, s.sklid\r\n" + 
			"ORDER BY s.sklid;", nativeQuery = true)
	List<Integer> countSkill();

	@Query(value="SELECT cnd.cndid id, cnd.cndkode kode, cnd.cndnm nama, cnd.cndjk jenisKelamin,\n" + 
			"cnd.cndtmplahir tempatLahir, cnd.cndtgllahir tanggalLahir, cnd.cndalamat alamat, \n" + 
			"cnd.cdnemail email, cnd.no_hp nohp, cnd.cndavail avail, cnd.ekspektasi_gaji ekspektasiGaji,\n" + 
			"STRING_AGG(pos.posnm || '(' || rcpos.lamapglmn || ')', ', ' ORDER BY pos.posnm) posisi\n" + 
			"FROM t_candidate cnd FULL JOIN t_recposisi rcpos ON cnd.cndid = rcpos.cndid\n" + 
			"FULL JOIN t_posisi pos ON rcpos.posid = pos.posid\n" + 
			"WHERE cnd.cndavail= :cndavail\n" + 
			"GROUP BY cnd.cndid", nativeQuery=true)
	List<AllCandidateByStatus> findAllCandidateByStatus(String cndavail);
	
	@Query(value="SELECT DISTINCT ON (id)\r\n" + 
			"			cnd.cndid id, cnd.cndkode kode, process, cnd.cndnm nama,cnd.no_hp nohp, cnd.cdnemail email,\r\n" + 
			"			cnd.ekspektasi_gaji ekspektasiGaji, cnd.waktu_available waktuAvailable,\r\n" + 
			"			lockuser.tgl_mulai tglMulai, lockuser.tgl_selesai tglSelesai, lockuser.id lockId,\r\n" + 
			"			recpos.posisi,\r\n" + 
			"			CASE WHEN lockuser.tgl_selesai > current_date\r\n" + 
			"					            THEN 'YES'\r\n" + 
			"					            ELSE 'NO' \r\n" + 
			"					       END AS lockstatus\r\n" + 
			"			FROM t_candidate cnd FULL JOIN t_lock_user lockuser ON cnd.cndid = lockuser.cndid\r\n" + 
			"			INNER JOIN\r\n" + 
			"			(select cnd.cndid id,STRING_AGG(pos.posnm || '(' || rec.lamapglmn || ')', ', ' ORDER BY pos.posnm) posisi, STRING_AGG(cli.shclinm || '(' || pic.picname || ')', ', ' ORDER BY cli.shclinm) process\r\n" + 
			"			FROM t_candidate cnd\r\n" + 
			"			FULL JOIN t_recposisi rec ON cnd.cndid = rec.cndid\r\n" + 
			"			FULL JOIN t_posisi pos ON rec.posid = pos.posid\r\n" + 
			"			       left join t_plotting plot on cnd.cndid=plot.cndid\r\n" + 
			"      left join t_plt_detail det on det.pltid = plot.pltid\r\n" + 
			"      left join t_det_keb dkb on plot.dkbid = dkb.dkbid\r\n" + 
			"      left join t_pic_client pic on dkb.picid = pic.picid\r\n" + 
			"      left join t_client cli on pic.cliid = cli.cliid\r\n" + 
			"			group by cnd.cndid) AS recpos\r\n" + 
			"			ON recpos.id = cnd.cndid\r\n" + 
			"			WHERE cnd.cndavail='Available'\r\n" + 
			"\r\n" + 
			"			ORDER  BY id, lockuser.tgl_mulai DESC", nativeQuery=true)
	List<AvailableCandidateProjectionLock> findAllAvailableCandidateLock();

}

