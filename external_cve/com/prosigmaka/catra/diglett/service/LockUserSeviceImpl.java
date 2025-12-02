package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.assembler.LockUserAssembler;
import com.prosigmaka.catra.diglett.model.dto.LockUserDto;
import com.prosigmaka.catra.diglett.model.entity.LockUser;
import com.prosigmaka.catra.diglett.repository.LockUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/*
 * Copyright (c) 2021. All rights reserved.
 * Rosa Nur Pranita
 */

@Service
@Transactional
public class LockUserSeviceImpl implements LockUserService {

    private final LockUserRepository repository;
    private final LockUserAssembler assembler;

    public LockUserSeviceImpl(LockUserRepository repository, LockUserAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public LockUserDto create(LockUserDto LockUserDto) {
    	LockUser entity = repository.save(assembler.fromDto(LockUserDto));
    	repository.save(entity);
        return assembler.fromEntity(entity);
    }

}
