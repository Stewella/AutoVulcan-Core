package com.prosigmaka.catra.diglett.assembler;

import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.IndustryDto;
import com.prosigmaka.catra.diglett.model.entity.Industry;

@Component
public class IndustryAssembler implements InterfaceAssembler<Industry, IndustryDto>{

	@Override
	public Industry fromDto(IndustryDto dto) {
		if(dto == null) return null;
		
		Industry entity = new Industry();
		if(dto.getId()!=null) entity.setId(dto.getId());
		if(dto.getNama()!=null) entity.setNama(dto.getNama());
		return entity;
	}

	@Override
	public IndustryDto fromEntity(Industry entity) {
		if(entity == null) return null;
		
		return IndustryDto.builder()
				.id(entity.getId())
				.nama(entity.getNama())
				.build();
	}
	
}
