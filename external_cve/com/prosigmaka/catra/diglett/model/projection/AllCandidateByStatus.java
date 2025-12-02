package com.prosigmaka.catra.diglett.model.projection;

import java.sql.Date;

public interface AllCandidateByStatus {
     String getId();
     String getKode();
     String getNama();
     String getJenisKelamin();
     String getTempatLahir();
     Date getTanggalLahir();
     String getAlamat();
     String getEmail();
     String getNoHp();
     String getAvail();
     String getEkspektasiGaji();
     String getPosisi();

}
