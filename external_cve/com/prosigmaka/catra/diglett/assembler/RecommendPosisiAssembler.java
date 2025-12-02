package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.RecommendPosisiDto;
import com.prosigmaka.catra.diglett.model.entity.RecommendPosisi;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.PosisiRepository;

@Component
public class RecommendPosisiAssembler implements InterfaceAssembler<RecommendPosisi, RecommendPosisiDto> {
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private PosisiRepository posisiRepository;

	@Override
	public RecommendPosisi fromDto(RecommendPosisiDto dto) {
		if (dto == null)
			return null;
		RecommendPosisi entity = new RecommendPosisi();
		if (dto.getIdCandidate() != null)
			entity.setIdCandidate(dto.getIdCandidate());
		if (dto.getId() != null)
			entity.setId(dto.getId());
		if (dto.getPosisi() != null)
			entity.setIdPosisi(posisiRepository.findByPosisi(dto.getPosisi()).getId());
		if (dto.getLamapglmn() != null)
			entity.setLamapglmn(dto.getLamapglmn());

		return entity;
	}

	@Override
	public RecommendPosisiDto fromEntity(RecommendPosisi entity) {
		if (entity == null)
			return null;
		String namaCandidate = null;
		String namaPosisi = null;
		if (entity.getIdCandidate() != null)
			namaCandidate = candidateRepository.findById(entity.getIdCandidate()).get().getNama();
		if (entity.getIdPosisi() != null)
			namaPosisi = posisiRepository.findById(entity.getIdPosisi()).get().getPosisi();
		return RecommendPosisiDto.builder().id(entity.getId()).idCandidate(entity.getIdCandidate())
				.candidate(namaCandidate).posisi(namaPosisi).lamapglmn(entity.getLamapglmn()).build();
	}

}
