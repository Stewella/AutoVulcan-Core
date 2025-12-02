package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.DocumentTypeDto;
import com.prosigmaka.catra.diglett.model.entity.DocumentType;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeAssembler implements InterfaceAssembler<DocumentType, DocumentTypeDto> {

    @Override
    public DocumentType fromDto(DocumentTypeDto dto) {
        if (dto == null) return null;

        DocumentType entity = new DocumentType();

        if (dto.getIdDocType() != null)
            entity.setIdDocType(dto.getIdDocType());
        if(dto.getNamaDocument() != null)
            entity.setNamaDocument(dto.getNamaDocument());

        return entity;
    }

    @Override
    public DocumentTypeDto fromEntity(DocumentType entity) {
        if (entity == null)
            return null;

        return DocumentTypeDto.builder()
                .idDocType(entity.getIdDocType())
                .namaDocument(entity.getNamaDocument())
                .build();
    }
}
