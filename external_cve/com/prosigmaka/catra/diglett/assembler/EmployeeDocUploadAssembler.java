package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.EmployeeDocUploadDto;
import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.entity.DocumentType;
import com.prosigmaka.catra.diglett.model.entity.EmployeeDocUpload;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.DocumentTypeRepository;
import com.prosigmaka.catra.diglett.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDocUploadAssembler implements InterfaceAssembler<EmployeeDocUpload, EmployeeDocUploadDto> {

    private final ModelMapper modelMapper;
    private final DocumentTypeRepository documentTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final CandidateRepository candidateRepository;

    public EmployeeDocUploadAssembler(ModelMapper modelMapper,
                                      DocumentTypeRepository documentTypeRepository,
                                      EmployeeRepository employeeRepository,
                                      CandidateRepository candidateRepository) {
        this.modelMapper = modelMapper;
        this.documentTypeRepository = documentTypeRepository;
        this.employeeRepository = employeeRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public EmployeeDocUpload fromDto(EmployeeDocUploadDto dto) {
        if (dto == null) return null;

        Candidate candidate = candidateRepository.findByIdEquals(dto.getId());
        DocumentType docType = documentTypeRepository.findById(dto.getIdDocType()).get();
        EmployeeDocUpload entity = modelMapper.map(dto, EmployeeDocUpload.class);
        entity.setCandidate(candidate);
        entity.setDocumentType(docType);

        return entity;
    }

    @Override
    public EmployeeDocUploadDto fromEntity(EmployeeDocUpload entity) {
        if (entity == null) return null;

        EmployeeDocUploadDto dto = new EmployeeDocUploadDto();
        modelMapper.map(entity, dto);
        modelMapper.map(entity.getDocumentType(), dto);
        modelMapper.map(entity.getCandidate(), dto);

        return dto;
    }
}
