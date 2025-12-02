package com.prosigmaka.catra.diglett.model.projection;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface PlottingDetailProjection {
    String getId();
    Integer getOrderStep();
    String getKegiatan();
    String getResult();
    String getKeterangan();
    String getPltid();
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Asia/Jakarta")
    Date getTanggal();
    String getKebutuhan();
    String getPicName();
    String getClient();
}
