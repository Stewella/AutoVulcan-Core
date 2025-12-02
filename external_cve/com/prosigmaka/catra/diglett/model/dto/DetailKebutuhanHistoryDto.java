package com.prosigmaka.catra.diglett.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DetailKebutuhanHistoryDto {
	Integer id;
	String idDetKeb;
	String kodeDetKeb;
	Date createdDate;
	String createdBy;
	String status;
	Integer jumlahPerubahan;
	Integer current;
	Integer before;
	String keterangan;
	Date tanggalProses;

}
