package com.prosigmaka.catra.diglett.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prosigmaka.catra.diglett.assembler.StepDetailAssembler;
import com.prosigmaka.catra.diglett.model.dto.StepDetailDto;
import com.prosigmaka.catra.diglett.model.entity.StepDetail;
import com.prosigmaka.catra.diglett.repository.StepDetailRepository;

@Service
@Transactional
public class StepDetailServiceImpl implements StepDetailService{

	private final StepDetailRepository repository;
	private final StepDetailAssembler assembler;

	public StepDetailServiceImpl(StepDetailRepository repository, StepDetailAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@Override
	public StepDetail insertStepDetail(StepDetailDto dto) {
		StepDetail entity = repository.save(assembler.fromDto(dto));
		repository.save(entity);
		return entity;
	}

}
