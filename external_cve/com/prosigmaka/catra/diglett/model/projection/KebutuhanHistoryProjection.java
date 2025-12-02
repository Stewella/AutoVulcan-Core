package com.prosigmaka.catra.diglett.model.projection;

import java.sql.Date;

public interface KebutuhanHistoryProjection {
    String getClient();
    String getPicName();
    String getKebutuhan();
    Date getTgl();
    Date getCreatedOn();
    String getKeterangan();

}
