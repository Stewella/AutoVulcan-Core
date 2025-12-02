package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prosigmaka.catra.diglett.model.dto.PlottingHistoryDto;
import com.prosigmaka.catra.diglett.model.entity.PlottingHistory;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;
import com.prosigmaka.catra.diglett.repository.PicClientRepository;

@Component
public class PlottingHistoryAssembler implements InterfaceAssembler<PlottingHistory, PlottingHistoryDto> {
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private DetailKebutuhanRepository detailKebutuhanRepository;
	@Autowired
	private PicClientRepository picRepo;
	@Autowired
	private ClientRepository clientRepo;

	@Override
	public PlottingHistory fromDto(PlottingHistoryDto dto) {
		if (dto == null)
			return null;
		PlottingHistory entity = new PlottingHistory();
		if (dto.getIdCandidate() != null)
			entity.setIdCandidate(dto.getIdCandidate());
		if (dto.getKode() != null)
			entity.setIdDetKebutuhan(detailKebutuhanRepository.findByKode(dto.getKode()).getId());
		if (dto.getTglKeputusan() != null)
			entity.setTglKeputusan(dto.getTglKeputusan());
		if (dto.getKeteranganPltHist() != null)
			entity.setKeteranganPltHist(dto.getKeteranganPltHist());

		return entity;
	}

	@Override
	public PlottingHistoryDto fromEntity(PlottingHistory entity) {
		if (entity == null)
			return null;
		String namaCandidate = null;
		String kode = null;
		String idPic = null;
		if (entity.getIdCandidate() != null)
			namaCandidate = candidateRepository.findById(entity.getIdCandidate()).get().getNama();
			String kodeCandidate= candidateRepository.findById(entity.getIdCandidate()).get().getCndkode();
		if (entity.getIdDetKebutuhan() != null)
			kode = detailKebutuhanRepository.findById(entity.getIdDetKebutuhan()).get().getKode();
			idPic = detailKebutuhanRepository.findById(entity.getIdDetKebutuhan()).get().getIdPic();
			String idClient = (picRepo.findById(idPic).get().getIdClient());
			String nameClient = clientRepo.findById(idClient).get().getNickname();
		return PlottingHistoryDto.builder()
				.idPltHist(entity.getIdPltHist())
				.idCandidate(entity.getIdCandidate())
				.kodeCandidate(kodeCandidate)
				.namaCandidate(namaCandidate)
				.kode(kode)
				.idDetKebutuhan(entity.getIdDetKebutuhan())
				.client(nameClient)
				.tglKeputusan(entity.getTglKeputusan())
				.keteranganPltHist(entity.getKeteranganPltHist())
				.build();

	}

}
