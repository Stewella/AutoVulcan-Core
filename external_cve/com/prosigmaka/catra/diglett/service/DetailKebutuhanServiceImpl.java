package com.prosigmaka.catra.diglett.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.prosigmaka.catra.diglett.assembler.DetailKebutuhanAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.DetailKebutuhanDto;
import com.prosigmaka.catra.diglett.model.dto.OptiDto;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;
import com.prosigmaka.catra.diglett.model.projection.OptiProjection;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;
import com.prosigmaka.catra.diglett.repository.PicClientRepository;
import com.prosigmaka.catra.diglett.repository.ServicesRepo;

@Service
@Transactional
public class DetailKebutuhanServiceImpl implements DetailKebutuhanService {
	private final DetailKebutuhanRepository detkebRepo;
	private final ServicesRepo serviceRepo;
	private final PicClientRepository picRepo;
	private final ClientRepository clientRepo;
	private final DetailKebutuhanAssembler detkebAssembler;

	public DetailKebutuhanServiceImpl(DetailKebutuhanRepository detkebRepo, ServicesRepo serviceRepo, PicClientRepository picRepo, ClientRepository clientRepo, DetailKebutuhanAssembler detkebAssembler) {
		this.detkebRepo = detkebRepo;
		this.serviceRepo = serviceRepo;
		this.picRepo = picRepo;
		this.clientRepo = clientRepo;
		this.detkebAssembler = detkebAssembler;
	}

	@Override
	public DetailKebutuhanDto saveKode(String id) {
		DetailKebutuhan entity = detkebRepo.findById(id).get();
		
		String nickServ = serviceRepo.findById(entity.getIdService()).get().getShortService();
		String idClient = (picRepo.findById(entity.getIdPic()).get().getIdClient());
		String nameClient = clientRepo.findById(idClient).get().getNickname();
		String inc = entity.getId();
		String kode = nickServ + "-" + nameClient + "-" + inc;
		
		entity.setKode(kode);
		detkebRepo.save(entity);
		return detkebAssembler.fromEntity(entity);
	}

	public List<OptiProjection> listByOptiProjection(){
		return detkebRepo.findopti();
	}

	@Override
	public DetailKebutuhan updateJumlah(String kode) {
		DetailKebutuhan entity = detkebRepo.findByKode(kode);
		Integer minus = entity.getJumlah() - 1;
		entity.setJumlah(minus);
		detkebRepo.save(entity);
		return entity;
	}
}
