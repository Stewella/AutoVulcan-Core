package com.prosigmaka.catra.diglett.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendPosisiDto {
    private String id;
    private String idCandidate;
    private String candidate;
    private String posisi;
    private String lamapglmn;

}
