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
public class PlottingHistoryDto {
	private String idPltHist;
	private String idCandidate;
	private String kodeCandidate;
	private String namaCandidate;
	private String idDetKebutuhan;
	private String client;
	private String kode;
	private Date tglKeputusan;
	private String keteranganPltHist;

}
