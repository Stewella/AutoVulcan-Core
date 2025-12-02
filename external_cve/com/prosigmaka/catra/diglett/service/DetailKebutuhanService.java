package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.dto.DetailKebutuhanDto;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;

public interface DetailKebutuhanService {
	DetailKebutuhanDto saveKode(String id);
	DetailKebutuhan updateJumlah(String kode);
	
}
