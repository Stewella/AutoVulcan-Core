package com.prosigmaka.catra.diglett.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prosigmaka.catra.diglett.assembler.StepAssembler;
import com.prosigmaka.catra.diglett.model.dto.StepDto;
import com.prosigmaka.catra.diglett.model.entity.Step;
import com.prosigmaka.catra.diglett.repository.StepRepository;

@Service
@Transactional
public class StepServiceImpl implements StepService {
	private final StepRepository repository;
	private final StepAssembler assembler;

	public StepServiceImpl(StepRepository repository, StepAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@Override
	public Step insertStep(StepDto dto) {
		Step entity = repository.save(assembler.fromDto(dto));
		repository.save(entity);
		return entity;
	}

}
