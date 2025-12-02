package com.prosigmaka.catra.diglett.assembler;

import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.ServicesDto;
import com.prosigmaka.catra.diglett.model.entity.Services;

@Component
public class ServicesAssembler implements InterfaceAssembler<Services, ServicesDto> {
	@Override
	public Services fromDto(ServicesDto dto) {
		if (dto == null)
            return null;
		Services entity = new Services();
		if(dto.getId() != null)
			entity.setId(dto.getId());
		if(dto.getService() != null)
			entity.setService(dto.getService());
		if(dto.getShortService() != null)
			entity.setShortService(dto.getShortService());
		return entity;
	}
	
	@Override
	public ServicesDto fromEntity(Services entity) {
		if (entity == null)
            return null;
		return ServicesDto.builder().id(entity.getId()).service(entity.getService()).shortService(entity.getShortService()).build();
	}

}
