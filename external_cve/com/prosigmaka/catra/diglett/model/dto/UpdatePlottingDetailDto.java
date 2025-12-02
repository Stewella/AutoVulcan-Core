package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlottingDetailDto {
    private String result;
    private String keterangan;
    private Date tanggal;
}
