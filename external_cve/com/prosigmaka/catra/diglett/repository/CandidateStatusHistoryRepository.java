package com.prosigmaka.catra.diglett.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prosigmaka.catra.diglett.model.entity.CandidateStatusHistory;
import com.prosigmaka.catra.diglett.model.projection.DashboardStatusPerMonth;

@Repository
public interface CandidateStatusHistoryRepository extends JpaRepository<CandidateStatusHistory, String> {
//	keperluan dashboard
	@Query(value="select bul.month, av.available, nv.notAvailable, h.hired from t_bulan bul\r\n" + 
			"			left join (select DATE_TRUNC('month',tc.tanggal_proses) as time, count(tc.cnd_avail_hist) as available, \r\n" + 
			"			cast(trim(TO_CHAR(DATE_TRUNC('month',tc.tanggal_proses), 'month'),' ') as varchar)\r\n" + 
			"			AS month from t_cndhist tc\r\n" + 
			"			where tc.cnd_avail_hist = 'Available'\r\n" + 
			"			group by DATE_TRUNC('month',tc.tanggal_proses)) av on av.month = bul.month\r\n" + 
			"			left join (select DATE_TRUNC('month',tc.tanggal_proses) as time, count(tc.cnd_avail_hist) as notAvailable, \r\n" + 
			"			cast(trim(TO_CHAR(DATE_TRUNC('month',tc.tanggal_proses), 'month'),' ') as varchar)\r\n" + 
			"			AS month from t_cndhist tc\r\n" + 
			"			where tc.cnd_avail_hist = 'Not Available'\r\n" + 
			"			group by DATE_TRUNC('month',tc.tanggal_proses)) nv on nv.month = bul.month \r\n" + 
			"			left join (select DATE_TRUNC('month',tc.tanggal_proses) as time, count(tc.cnd_avail_hist) as hired, \r\n" + 
			"			cast(trim(TO_CHAR(DATE_TRUNC('month',tc.tanggal_proses), 'month'),' ') as varchar)\r\n" + 
			"			AS month from t_cndhist tc\r\n" + 
			"			where tc.cnd_avail_hist = 'Hired'\r\n" + 
			"			group by DATE_TRUNC('month',tc.tanggal_proses)) h on h.month = bul.month\r\n" + 
			"			order by bul.id", nativeQuery = true)
	List<DashboardStatusPerMonth> findStatusPerMonth();
}
