package com.prosigmaka.catra.diglett.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailKebutuhanDto {
    String id;
    String kode;
    String idPic;
    String picName;
    String client;
    String idPosisi;
    String posisi;
    String idService;
    String service;
    String level;
    Integer jumlah;
}
