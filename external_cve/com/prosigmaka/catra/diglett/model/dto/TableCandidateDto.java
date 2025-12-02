package com.prosigmaka.catra.diglett.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableCandidateDto {
    private String id;
    private String kode;
    private String nama;
    private String rumpunName;
    private String posisiName;
    private String pengalaman;
    private String email;
    private String noHp;

}
