package com.prosigmaka.catra.diglett.model.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class EmployeeDto {
    private String idEmployee;
    //id candidate
    private String id;
    private String avail;
    private String kode;
    private String waktuAvailable;
    private Date tanggalProses;
    private String ekspektasiGaji;

    //data diri
    private String nama;
    private String jenisKelamin;
    private String tempatLahir;
    private Date tanggalLahir;
    private String statusMenikah;
    private String agama;
    private String alamat;
    private String alamatDomisili;

    //data pendidikan
    private String jenjangPendidikan;
    private String sekolah;
    private String jurusan;

    //data kepegawaian
    private String penempatanProyek;
    private String posisiHired;
    private String statusKontrak;
    private String startJoin;
    private String endKontrak;

    //data dokumen
    private String nik;
    private String noKk;
    private String noNpwp;
    private String noBpjsTk;
    private String noBpjsKesehatan;

    //data kontak
    private String noHp; //telepon1
    private String telepon2;
    private String email;
    private String namaEmergencyContact;
    private String nomorEmergencyContact;

    //data keluarga
    private String namaIstriSuami;
    private String tanggalLahirIstriSuami;
    private String namaAnak1;
    private String tanggalLahirAnak1;
    private String namaAnak2;
    private String tanggalLahirAnak2;
    private String namaAnak3;
    private String tanggalLahirAnak3;

    //data lain
    private String periodeKontrak1;
    private String periodeKontrak2;
    private String periodeKontrak3;
    private String statusPermanen;
    private String mutasi;
    private String namaBank;
    private String noRekening;
    private String namaRekening;
    private String namaReferensi;
    private String noReferensi;
}
