package com.prosigmaka.catra.diglett.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosigmaka.catra.diglett.assembler.ClientAssembler;
import com.prosigmaka.catra.diglett.model.dto.ClientDto;
import com.prosigmaka.catra.diglett.model.entity.Client;
import com.prosigmaka.catra.diglett.repository.ClientRepository;

@Service
@Transactional
public class ClientServiceImpl implements ClientService{
	private final ClientRepository repository;
    private final ClientAssembler assembler;

    public ClientServiceImpl(ClientRepository repository, ClientAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public ClientDto insertClient(ClientDto dto) {
        Client entity = repository.save(assembler.fromDto(dto));
        repository.save(entity);
        return assembler.fromEntity(entity);
    }
}
