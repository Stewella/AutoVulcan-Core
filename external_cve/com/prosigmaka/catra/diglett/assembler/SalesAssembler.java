package com.prosigmaka.catra.diglett.assembler;

import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.SalesDto;
import com.prosigmaka.catra.diglett.model.entity.Sales;

@Component
public class SalesAssembler  implements InterfaceAssembler<Sales, SalesDto>{
	
	@Override
	public Sales fromDto(SalesDto dto) {
		if(dto == null) return null;
		
		Sales entity = new Sales();
		if(dto.getId()!=null) entity.setId(dto.getId());
		if(dto.getNama()!=null) entity.setNama(dto.getNama());
		if(dto.getNoHp()!=null) entity.setNoHp(dto.getNoHp());
		if(dto.getEmail()!=null) entity.setEmail(dto.getEmail());
		return entity;
	}

	@Override
	public SalesDto fromEntity(Sales entity) {
		if(entity == null) return null;
		
		return SalesDto.builder()
				.id(entity.getId())
				.nama(entity.getNama())
				.noHp(entity.getNoHp())
				.email(entity.getEmail())
				.build();
	}

}
