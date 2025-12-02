package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.assembler.PosisiAssembler;
import com.prosigmaka.catra.diglett.model.dto.PosisiDto;
import com.prosigmaka.catra.diglett.model.entity.Posisi;
import com.prosigmaka.catra.diglett.repository.PosisiRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PosisiServiceImpl implements PosisiService {
    private final PosisiRepository posisiRepository;
    private final PosisiAssembler posisiAssembler;

    public PosisiServiceImpl(PosisiRepository posisiRepository, PosisiAssembler posisiAssembler) {
        this.posisiRepository = posisiRepository;
        this.posisiAssembler = posisiAssembler;
    }

    @Override
    public PosisiDto insertPosisi(PosisiDto dto) {
        Posisi entity = posisiRepository.save(posisiAssembler.fromDto(dto));
		posisiRepository.save(entity);
		return posisiAssembler.fromEntity(entity);
    }
    
}
