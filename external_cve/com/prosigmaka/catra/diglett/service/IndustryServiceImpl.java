package com.prosigmaka.catra.diglett.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosigmaka.catra.diglett.assembler.IndustryAssembler;
import com.prosigmaka.catra.diglett.model.dto.IndustryDto;
import com.prosigmaka.catra.diglett.model.entity.Industry;
import com.prosigmaka.catra.diglett.repository.IndustryRepository;

@Service
@Transactional
public class IndustryServiceImpl implements IndustryService{
	private final IndustryRepository repository;
	private final IndustryAssembler assembler;

	public IndustryServiceImpl(IndustryRepository repository, IndustryAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@Override
	public IndustryDto insertIndustry(IndustryDto dto) {
		Industry entity = repository.save(assembler.fromDto(dto));
        repository.save(entity);
        return assembler.fromEntity(entity);
	}
	
}
