package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicesDto {
    private String id;
    private String service;
    private String shortService;

}
