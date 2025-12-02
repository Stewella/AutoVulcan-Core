package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.StatusAvailableDto;
import com.prosigmaka.catra.diglett.model.entity.StatusAvailable;
import org.springframework.stereotype.Component;

@Component
public class StatusAvailableAssembler implements InterfaceAssembler<StatusAvailable, StatusAvailableDto> {

    @Override
    public StatusAvailable fromDto(StatusAvailableDto dto) {
        if (dto == null) return null;

        StatusAvailable entity = new StatusAvailable();

        if (dto.getId() != null)
            entity.setId(dto.getId());
        if (dto.getGrupAvailable() != null)
            entity.setGrupAvailable(dto.getGrupAvailable());
        if (dto.getStatus() != null)
            entity.setStatus(dto.getStatus());

        return entity;
    }

    @Override
    public StatusAvailableDto fromEntity(StatusAvailable entity) {
        if (entity == null)
            return null;

        return StatusAvailableDto.builder()
                .id(entity.getId())
                .grupAvailable(entity.getGrupAvailable())
                .status(entity.getStatus())
                .build();
    }
}
