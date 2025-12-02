package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepDetailDto {
    private String idStepDet;
    private String idStep;
    private String namaStep;
    private String idClient;
    private String namaClient;
    private Integer orderStep;
}
