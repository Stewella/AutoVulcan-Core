package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesDto {
    private Integer id;
    private String nama;
    private String noHp;
    private String email; 
}