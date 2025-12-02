package com.prosigmaka.catra.diglett.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

import com.prosigmaka.catra.diglett.model.dto.FormPlottingDto;
import com.prosigmaka.catra.diglett.model.entity.Plotting;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;
import com.prosigmaka.catra.diglett.repository.PlottingDetailRepository;

@Component
public class PlottingAssembler implements InterfaceAssembler<Plotting, FormPlottingDto> {

	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private DetailKebutuhanRepository detailKebutuhanRepository;
	@Autowired
	private PlottingDetailRepository plottingDetailRepository;

	@Override
	public Plotting fromDto(FormPlottingDto dto) {
		if (dto == null)
			return null;
		Plotting entity = new Plotting();
		if (dto.getNamaKandidat() != null)
			entity.setCandidate(candidateRepository.findByNama(dto.getNamaKandidat()));
		if (dto.getKode() != null)
			entity.setDetailKebutuhan(detailKebutuhanRepository.findByKode(dto.getKode()));
		if (dto.getKeterangan() != null)
			entity.setKeterangan(dto.getKeterangan());
		Date currentDate = new Date(System.currentTimeMillis());
		entity.setCreatedOn(currentDate);
		return entity;
	}

	@Override
	public FormPlottingDto fromEntity(Plotting entity) {
		if (entity == null)
			return null;
		return FormPlottingDto.builder()
				.namaKandidat(entity.getCandidate().getNama())
				.kode(entity.getDetailKebutuhan().getKode())
				.namaClient(entity.getDetailKebutuhan().getPicClient().getClient().getNama())
				.namaPic(entity.getDetailKebutuhan().getPicClient().getPicName())
				.kebutuhan(entity.getDetailKebutuhan().getPosisi().getPosisi())
				.tgl(plottingDetailRepository.findFirstTestDate(entity.getIdPlotting(), entity.getDetailKebutuhan().getId()))
				.keterangan(entity.getKeterangan()).build();
	}

}
