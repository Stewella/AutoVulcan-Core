package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.DetailKebutuhanHistoryDto;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhanHistory;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;

@Component
public class DetailKebutuhanHistoryAssembler implements InterfaceAssembler<DetailKebutuhanHistory, DetailKebutuhanHistoryDto>{
	@Autowired
	DetailKebutuhanRepository detailKebutuhanRepository;
	
	@Override
	public DetailKebutuhanHistory fromDto(DetailKebutuhanHistoryDto dto) {
		if (dto == null)
			return null;
		DetailKebutuhanHistory entity = new DetailKebutuhanHistory();

		if (dto.getCreatedBy() != null)
			entity.setCreatedBy(dto.getCreatedBy());
		if (dto.getStatus() != null)
			entity.setStatus(dto.getStatus());
		if (dto.getJumlahPerubahan()!= null)
			entity.setJumlahPerubahan(dto.getJumlahPerubahan());
		if (dto.getIdDetKeb()!= null)
			entity.setIdDetKeb(dto.getIdDetKeb());
			entity.setDetailKebutuhan(detailKebutuhanRepository.findById(dto.getIdDetKeb()).get());
		if (dto.getCurrent()!= null)
			entity.setCurrent(dto.getCurrent());
		if (dto.getBefore()!= null)
			entity.setBefore(dto.getBefore());
		if (dto.getKeterangan()!= null)
			entity.setKeterangan(dto.getKeterangan());
		if (dto.getTanggalProses()!= null)
			entity.setTanggalProses(dto.getTanggalProses());
		return entity;
	}

	@Override
	public DetailKebutuhanHistoryDto fromEntity(DetailKebutuhanHistory entity) {
		if (entity == null)
			return null;

		return DetailKebutuhanHistoryDto.builder()
				.id(entity.getId())
				.idDetKeb(entity.getIdDetKeb())
				.kodeDetKeb(entity.getDetailKebutuhan().getKode())
				.createdDate(entity.getCreatedDate())
				.createdBy(entity.getCreatedBy())
				.status(entity.getStatus())
				.jumlahPerubahan(entity.getJumlahPerubahan())
				.current(entity.getCurrent())
				.before(entity.getBefore())
				.keterangan(entity.getKeterangan())
				.tanggalProses(entity.getTanggalProses())
				.build();
				
	
	}

}
