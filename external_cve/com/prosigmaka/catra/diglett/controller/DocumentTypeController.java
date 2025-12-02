package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.DocumentTypeAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.DocumentTypeDto;
import com.prosigmaka.catra.diglett.model.entity.DocumentType;
import com.prosigmaka.catra.diglett.repository.DocumentTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/document-type")
public class DocumentTypeController {

    private final DocumentTypeAssembler assembler;
    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeController(DocumentTypeRepository documentTypeRepository, DocumentTypeAssembler assembler) {
        this.documentTypeRepository = documentTypeRepository;
        this.assembler = assembler;
    }

    @GetMapping("/get-all")
    public DefaultResponse<List<DocumentTypeDto>> getAll() {
        List<DocumentType> documentTypeList = documentTypeRepository.findAll();
        List<DocumentTypeDto> documentTypeDtos = documentTypeList.stream()
                                                 .map(docType -> assembler.fromEntity(docType))
                                                 .collect(Collectors.toList());
        if (documentTypeDtos == null)
            return DefaultResponse.error("Data gagal ditampilkan");

        return DefaultResponse.ok(documentTypeDtos);
    }
}
