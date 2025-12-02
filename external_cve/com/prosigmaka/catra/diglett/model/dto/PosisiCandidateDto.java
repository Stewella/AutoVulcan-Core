package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosisiCandidateDto {
    // private String id;
    // private String idCandidate;
    private String posisi;
    private String cluster;
    private Date tglStart;
    private Date tglEnd;
}
