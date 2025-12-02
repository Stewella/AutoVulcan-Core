package com.prosigmaka.catra.diglett.assembler;

import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.StepDto;
import com.prosigmaka.catra.diglett.model.entity.Step;

@Component
public class StepAssembler implements InterfaceAssembler<Step, StepDto> {

	@Override
	public Step fromDto(StepDto dto) {
		if (dto == null)
			return null;
		Step entity = new Step();
		if (dto.getIdStep() != null)
			entity.setIdStep(dto.getIdStep());
		if (dto.getNamaStep() != null)
			entity.setNamaStep(dto.getNamaStep());
		return entity;
	}

	@Override
	public StepDto fromEntity(Step entity) {
		if(entity==null) return null;
		return StepDto.builder()
				.idStep(entity.getIdStep())
				.namaStep(entity.getNamaStep())
				.build();
	}

}
