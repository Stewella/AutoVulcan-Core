package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.StepDetailDto;
import com.prosigmaka.catra.diglett.model.entity.StepDetail;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.StepRepository;

@Component
public class StepDetailAssembler implements InterfaceAssembler<StepDetail, StepDetailDto> {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private StepRepository stepRepository;

    @Override
    public StepDetail fromDto(StepDetailDto dto) {
        if (dto == null)
            return null;

        StepDetail entity = new StepDetail();
        if (dto.getNamaClient() != null)
            entity.setClient(clientRepository.findByNama(dto.getNamaClient()).get());
        if (dto.getNamaStep() != null)
            entity.setStep(stepRepository.findByNamaStep(dto.getNamaStep()));
        if (dto.getOrderStep() != null)
            entity.setOrderStep(dto.getOrderStep());
        return entity;
    }

    @Override
    public StepDetailDto fromEntity(StepDetail entity) {
        if (entity == null)
            return null;
        return StepDetailDto.builder()
        		.idStepDet(entity.getIdStepDet())
        		.idClient(entity.getClient().getId())
        		.idStep(entity.getStep().getIdStep())
                .namaStep(entity.getStep().getNamaStep())
                .namaClient(entity.getClient().getNama())
                .orderStep(entity.getOrderStep())
                .build();
    }

}
