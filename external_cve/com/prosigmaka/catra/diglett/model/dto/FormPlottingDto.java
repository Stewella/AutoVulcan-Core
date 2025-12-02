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
public class FormPlottingDto {
    private String namaKandidat;
    private String kode;
    private String namaClient;
    private String namaPic;
    private String kebutuhan;
    private Date tgl;
    private String keterangan;
    // private String projectId;
}
