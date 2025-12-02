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
public class CandidateDto {
    private String id;
    private String kode;
    private String nama;
    private String jenisKelamin;
    private String tempatLahir;
    private Date tanggalLahir;
    private String alamat;
    private String email;
    private String noHp;
    private String avail;
    private String ekspektasiGaji;
    private String waktuAvailable;
    private Date tanggalProses;
}
