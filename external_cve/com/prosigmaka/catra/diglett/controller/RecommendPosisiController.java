package com.prosigmaka.catra.diglett.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prosigmaka.catra.diglett.assembler.RecommendPosisiAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.RecommendPosisiDto;
import com.prosigmaka.catra.diglett.model.entity.RecommendPosisi;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.RecommendPosisiRepository;

@RestController
@RequestMapping("/recommend")
public class RecommendPosisiController {
	private final RecommendPosisiAssembler recposAssembler;
	private final RecommendPosisiRepository recposRepository;
	private final CandidateRepository candidateRepository;

	public RecommendPosisiController(RecommendPosisiAssembler recposAssembler, RecommendPosisiRepository recposRepository, CandidateRepository candidateRepository) {
		this.recposAssembler = recposAssembler;
		this.recposRepository = recposRepository;
		this.candidateRepository = candidateRepository;
	}

	/* Insert Data */
	@PostMapping("/insert/{idCandidate}")
	public DefaultResponse<RecommendPosisi> insert(@PathVariable String idCandidate, @RequestBody RecommendPosisiDto dto) {
		if (candidateRepository.findById(idCandidate).isPresent() == false)
			return DefaultResponse.error("Id Kandidat Tidak Ditemukan");
		else {
			dto.setIdCandidate(idCandidate);
			RecommendPosisi entity = recposAssembler.fromDto(dto);
			recposRepository.save(entity);
			return DefaultResponse.ok(entity);
		}

	}

	@GetMapping("/getAll")
	public DefaultResponse<List<RecommendPosisiDto>> get() {
		List<RecommendPosisi> recommendList = recposRepository.findAll();
		List<RecommendPosisiDto> recommendDtoList = recommendList.stream()
				.map(recommend -> recposAssembler.fromEntity(recommend)).collect(Collectors.toList());
		return DefaultResponse.ok(recommendDtoList);
	}
	
	@GetMapping("/getById/{idCandidate}")
	public DefaultResponse<List<RecommendPosisiDto>> get(@PathVariable String idCandidate) {
		List<RecommendPosisi> recommendList = recposRepository.findRecommendPosisiByIdCandidate(idCandidate);
		List<RecommendPosisiDto> recommendDtoList = recommendList.stream()
				.map(recommend -> recposAssembler.fromEntity(recommend)).collect(Collectors.toList());
		return DefaultResponse.ok(recommendDtoList);
	}

	@DeleteMapping("/{id}")
	public DefaultResponse<RecommendPosisi> delete(@PathVariable String id) {
		RecommendPosisi recommend = recposRepository.findById(id).get();
		recposRepository.deleteById(id);
		return DefaultResponse.ok(recommend);
	}

}
