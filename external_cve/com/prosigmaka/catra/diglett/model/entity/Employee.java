package com.prosigmaka.catra.diglett.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;


@Entity
@Data
@Table(name = "t_employee")
public class Employee {

    @Id
    @GeneratedValue(generator = "employee-generator")
    @GenericGenerator(name = "employee-generator",
            parameters = @Parameter(name="prefix", value="EMP"),
            strategy = "com.prosigmaka.catra.diglett.model.generator.Generator")
    private String idEmployee;

    @Column(name = "statusMenikah", nullable = false)
    private String statusMenikah;

    @Column(name = "agama", nullable = false)
    private String agama;

    @Column(name = "alamatDomisili", nullable = false)
    private String alamatDomisili;


    //data pendidikan
    @Column(name = "jenjangPend")
    private String jenjangPendidikan;

    @Column(name = "sekolah")
    private String sekolah;

    @Column(name = "jurusan")
    private String jurusan;


    //data kepegawaian
    @Column(name = "penempatan")
    private String penempatanProyek;

    @Column(name = "posisiHired")
    private String posisiHired;

    @Column(name = "statusKontrak")
    private String statusKontrak;

    @Column(name = "startJoin")
    private String startJoin;

    @Column(name = "endKontrak")
    private String endKontrak;


    //data dokumen
    @Column(name = "nik")
    private String nik;

    @Column(name = "noKk")
    private String noKk;

    @Column(name = "noNpwp")
    private String noNpwp;

    @Column(name = "noBpjsTk")
    private String noBpjsTk;

    @Column(name = "noBpjsKes")
    private String noBpjsKesehatan;


    //data kontak
    @Column(name = "telp2")
    private String telepon2;

    @Column(name = "namaEmerg")
    private String namaEmergencyContact;

    @Column(name = "noEmerg")
    private String nomorEmergencyContact;


    //data keluarga
    @Column(name = "namaIstriSuami")
    private String namaIstriSuami;

    @Column(name = "tglLahirIstriSuami")
    private String tanggalLahirIstriSuami;

    @Column(name = "namaAnak1")
    private String namaAnak1;

    @Column(name = "tglLahirAnak1")
    private String tanggalLahirAnak1;

    @Column(name = "namaAnak2")
    private String namaAnak2;

    @Column(name = "tglLahirAnak2")
    private String tanggalLahirAnak2;

    @Column(name = "namaAnak3")
    private String namaAnak3;

    @Column(name = "tglLahirAnak3")
    private String tanggalLahirAnak3;


    //data lain
    @Column(name = "periodeKontrak1")
    private String periodeKontrak1;

    @Column(name = "periodeKontrak2")
    private String periodeKontrak2;

    @Column(name = "periodeKontrak3")
    private String periodeKontrak3;

    @Column(name = "statusPermanen")
    private String statusPermanen;

    @Column(name = "mutasi")
    private String mutasi;

    @Column(name = "namaBank")
    private String namaBank;

    @Column(name = "noRek")
    private String noRekening;

    @Column(name = "namaRek")
    private String namaRekening;

    @Column(name = "namaRefer")
    private String namaReferensi;

    @Column(name = "noRefer")
    private String noReferensi;

}
