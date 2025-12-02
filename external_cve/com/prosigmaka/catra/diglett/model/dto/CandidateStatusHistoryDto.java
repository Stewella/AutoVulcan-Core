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
public class CandidateStatusHistoryDto {
    private String id;
    private String idCandidate;
    private String cndAvail;
    private String createdBy;
    private Date tanggalProses;
    private String keterangan;
}

