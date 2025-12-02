package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.PicClientDto;
import com.prosigmaka.catra.diglett.model.entity.PicClient;
import com.prosigmaka.catra.diglett.model.enummodel.PicClientEnum;
import com.prosigmaka.catra.diglett.repository.ClientRepository;

@Component
public class PicClientAssembler implements InterfaceAssembler<PicClient, PicClientDto> {
	@Autowired
	private ClientRepository clientRepo;

	@Override
	public PicClient fromDto(PicClientDto dto) {
		if (dto == null)
			return null;
		PicClient entity = new PicClient();

		if (dto.getClient() != null)
			entity.setIdClient(clientRepo.findByNama(dto.getClient()).get().getId());
		if (dto.getPicEmail() != null)
			entity.setPicEmail(dto.getPicEmail());
		if (dto.getPicName() != null)
			entity.setPicName(dto.getPicName());
		if (dto.getPicNoHp() != null)
			entity.setPicNoHp(dto.getPicNoHp());
		entity.setIsOpen(PicClientEnum.TRUE.getValue());

		return entity;
	}

	@Override
	public PicClientDto fromEntity(PicClient entity) {
		if (entity == null)
			return null;
		String client = clientRepo.findById(entity.getIdClient()).get().getNama();
		return PicClientDto.builder()
				.id(entity.getId())
				.picName(entity.getPicName())
				.picEmail(entity.getPicEmail())
				.picNoHp(entity.getPicNoHp())
				.isOpen(entity.getIsOpen())
				.client(client)	
				.build();
	}

}
