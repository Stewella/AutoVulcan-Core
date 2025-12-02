package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.PosisiDto;
import com.prosigmaka.catra.diglett.model.entity.Posisi;

import org.springframework.stereotype.Component;

@Component
public class PosisiAssembler implements InterfaceAssembler<Posisi, PosisiDto> {

    @Override
    public Posisi fromDto(PosisiDto dto) {
        if (dto == null)
            return null;
        Posisi entity = new Posisi();
        if (dto.getId() != null)
            entity.setId(dto.getId());
        if (dto.getPosisi() != null)
            entity.setPosisi(dto.getPosisi());
        return entity;
    }

    @Override
    public PosisiDto fromEntity(Posisi entity) {
        if (entity == null)
            return null;
        return PosisiDto.builder().id(entity.getId()).posisi(entity.getPosisi()).build();

    }

}
