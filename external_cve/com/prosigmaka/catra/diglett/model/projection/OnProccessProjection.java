package com.prosigmaka.catra.diglett.model.projection;

import java.sql.Date;

public interface OnProccessProjection {
	String getId();
	String getPltId();
	String getCndId();
	String getDkbId();
	String getPltHistId();
	String getNama();
	String getResult();
	Date getTanggal();
}
