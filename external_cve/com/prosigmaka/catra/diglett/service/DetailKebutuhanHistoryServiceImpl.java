package com.prosigmaka.catra.diglett.service;

import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhanHistory;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanHistoryRepository;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;

@Service
@Transactional
public class DetailKebutuhanHistoryServiceImpl implements DetailKebutuhanHistoryService {
	
	private final DetailKebutuhanHistoryRepository detailKebutuhanHistoryRepository;
	
	private final DetailKebutuhanRepository detailKebutuhanRepository;

	public DetailKebutuhanHistoryServiceImpl(DetailKebutuhanHistoryRepository detailKebutuhanHistoryRepository, DetailKebutuhanRepository detailKebutuhanRepository) {
		this.detailKebutuhanHistoryRepository = detailKebutuhanHistoryRepository;
		this.detailKebutuhanRepository = detailKebutuhanRepository;
	}

	@Override
	public DetailKebutuhanHistory insertJumlah(String dkbId) {
		DetailKebutuhan entity = detailKebutuhanRepository.findById(dkbId).get();
		DetailKebutuhanHistory detkebhist = new DetailKebutuhanHistory();
		long millis=System.currentTimeMillis();  
		Date date=new Date(millis);   
		
		detkebhist.setBefore(entity.getJumlah());
		detkebhist.setCurrent(entity.getJumlah() - 1);
		detkebhist.setCreatedBy(null);
		detkebhist.setCreatedDate(date);
		detkebhist.setIdDetKeb(entity.getId());
		detkebhist.setJumlahPerubahan(1);
		detkebhist.setStatus("win");
		detkebhist.setCreatedDate(date);

		detailKebutuhanHistoryRepository.save(detkebhist);
		return detkebhist;
	}
	

}
