package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.LockUserDto;
import com.prosigmaka.catra.diglett.model.entity.LockUser;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;

@Component
public class LockUserAssembler implements InterfaceAssembler<LockUser, LockUserDto> {
	@Autowired
	private DetailKebutuhanRepository detailKebutuhanRepository;

	@Override
	public LockUser fromDto(LockUserDto dto) {
		if (dto == null)
			return null;
		LockUser entity = new LockUser();
		if (dto.getId() != null)
			entity.setId(dto.getId());
		if (dto.getCandidateId() != null)
			entity.setCandidateId(dto.getCandidateId());
		if (dto.getTglMulai() != null)
			entity.setTglMulai(dto.getTglMulai());
		if (dto.getTglSelesai() != null)
			entity.setTglSelesai(dto.getTglSelesai());
		if (dto.getKodeDetkeb() != null)
			entity.setDetkebId(detailKebutuhanRepository.findByKode(dto.getKodeDetkeb()).getId());
		if (dto.getKeterangan() != null)
			entity.setKeterangan(dto.getKeterangan());

		return entity;
	}

	@Override
	public LockUserDto fromEntity(LockUser entity) {
		if (entity == null)
			return null;
		String kodeDetkeb = detailKebutuhanRepository.findById(entity.getDetkebId()).get().getKode();
		return LockUserDto.builder().candidateId(entity.getCandidateId()).tglMulai(entity.getTglMulai())
				.kodeDetkeb(kodeDetkeb).tglSelesai(entity.getTglSelesai()).keterangan(entity.getKeterangan()).build();
	}

}
