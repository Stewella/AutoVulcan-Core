package com.prosigmaka.catra.diglett.service;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.prosigmaka.catra.diglett.assembler.ServicesAssembler;
import com.prosigmaka.catra.diglett.model.dto.ServicesDto;
import com.prosigmaka.catra.diglett.model.entity.Services;
import com.prosigmaka.catra.diglett.repository.ServicesRepo;

@Service
@Transactional
public class ServServiceImpl implements ServService {
	private final ServicesRepo servicesRepo;
	private final ServicesAssembler serviceAssembler;

	public ServServiceImpl(ServicesRepo servicesRepo, ServicesAssembler serviceAssembler) {
		this.servicesRepo = servicesRepo;
		this.serviceAssembler = serviceAssembler;
	}

	@Override
	public ServicesDto insertService(ServicesDto dto) {
		Services entity = servicesRepo.save(serviceAssembler.fromDto(dto));
		servicesRepo.save(entity);
		return serviceAssembler.fromEntity(entity);
	}

}
