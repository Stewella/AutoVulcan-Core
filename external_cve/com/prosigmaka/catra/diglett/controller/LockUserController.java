package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.LockUserAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.LockUserDto;
import com.prosigmaka.catra.diglett.model.entity.LockUser;
import com.prosigmaka.catra.diglett.repository.LockUserRepository;
import com.prosigmaka.catra.diglett.service.LockUserService;
import org.springframework.web.bind.annotation.*;


/*
 * Copyright (c) 2021. All rights reserved.
 * Rosa Nur Pranita
 */

@RestController
@RequestMapping(value = "/lock-user")
public class LockUserController {
    private final LockUserService service;
    private final LockUserRepository repository;
    private final LockUserAssembler assembler;

    public LockUserController(LockUserService service, LockUserRepository repository, LockUserAssembler assembler) {
        this.service = service;
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/getBy/{id}")
	public DefaultResponse<LockUserDto> getBy(@PathVariable Integer id) {
		LockUser entity = repository.findById(id).get();
		LockUserDto dto = assembler.fromEntity(entity);
		return DefaultResponse.ok(dto);
	}

    @PostMapping
    public DefaultResponse<LockUserDto> create(@RequestBody LockUserDto formRequest) {

        return DefaultResponse.ok(this.service.create(formRequest));
    }
    
//    @PutMapping("/update/{id}/{tgl}")
//	public DefaultResponse<LockUser> update(@PathVariable String id, @PathVariable Date tgl) {
//    	LockUser entity = repository.findById(kode);
//		entity.setTglSelesai(tgl);
//		repository.save(entity);
//		return DefaultResponse.ok(entity);
//	}

}
