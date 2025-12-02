package com.prosigmaka.catra.diglett.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prosigmaka.catra.diglett.assembler.PlottingHistoryAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.PlottingHistoryDto;
import com.prosigmaka.catra.diglett.model.entity.PlottingHistory;
import com.prosigmaka.catra.diglett.model.projection.OnProccessProjection;
import com.prosigmaka.catra.diglett.model.projection.WinProjection;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.PlottingHistoryRepository;

@RestController
@RequestMapping("/plottinghist")
public class PlottingHistoryController {
	private final PlottingHistoryAssembler plthistAssembler;
	private final PlottingHistoryRepository plthistRepository;
	private final CandidateRepository candidateRepository;

	public PlottingHistoryController(PlottingHistoryAssembler plthistAssembler, PlottingHistoryRepository plthistRepository, CandidateRepository candidateRepository) {
		this.plthistAssembler = plthistAssembler;
		this.plthistRepository = plthistRepository;
		this.candidateRepository = candidateRepository;
	}

	/* Insert Data */
	@PostMapping("/insert/{idCandidate}")
	public DefaultResponse<PlottingHistory> insert(@PathVariable String idCandidate,
			@RequestBody PlottingHistoryDto dto) {
		if (candidateRepository.findById(idCandidate).isPresent() == false)
			return DefaultResponse.error("Id Kandidat Tidak Ditemukan");
		else {
			dto.setIdCandidate(idCandidate);
			PlottingHistory entity = plthistAssembler.fromDto(dto);
			plthistRepository.save(entity);
			return DefaultResponse.ok(entity);
		}

	}

	@GetMapping("/getAll")
	public DefaultResponse<List<PlottingHistoryDto>> getAll() {
		List<PlottingHistory> plthistList = plthistRepository.findAll();
		List<PlottingHistoryDto> plthistDtoList = plthistList.stream()
				.map(plthist -> plthistAssembler.fromEntity(plthist)).collect(Collectors.toList());
		return DefaultResponse.ok(plthistDtoList);
	}
	
	@GetMapping("/{dkbId}")
	public DefaultResponse<List<WinProjection>> getByDkbId(@PathVariable String dkbId){
		List<WinProjection> candidate = plthistRepository.findNameListByDkbId(dkbId);
		return DefaultResponse.ok(candidate);
	}
	
	@GetMapping("/proccess/{dkbId}")
	public DefaultResponse<List<OnProccessProjection>> getProccess(@PathVariable String dkbId){
		List<OnProccessProjection> proccess = plthistRepository.getAll(dkbId);
		return DefaultResponse.ok(proccess);
	}

}
