package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.CandidateAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.CandidateDto;
import com.prosigmaka.catra.diglett.model.dto.CandidateStatusHistoryDto;
import com.prosigmaka.catra.diglett.model.dto.TableCandidateDto;
import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.entity.CandidateStatusHistory;
import com.prosigmaka.catra.diglett.model.projection.AllCandidateByStatus;
import com.prosigmaka.catra.diglett.model.projection.AvailableCandidateProjectionLock;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.CandidateStatusHistoryRepository;
import com.prosigmaka.catra.diglett.service.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
	private final CandidateRepository candidateRepository;
	private final CandidateAssembler candidateAssembler;
	private final CandidateService candidateService;
	private final CandidateStatusHistoryRepository candidateStatusHistoryRepository;

	public CandidateController(CandidateRepository candidateRepository, CandidateAssembler candidateAssembler, CandidateService candidateService, CandidateStatusHistoryRepository candidateStatusHistoryRepository) {
		this.candidateRepository = candidateRepository;
		this.candidateAssembler = candidateAssembler;
		this.candidateService = candidateService;
		this.candidateStatusHistoryRepository = candidateStatusHistoryRepository;
	}

	@GetMapping
	public DefaultResponse<List<CandidateDto>> get() {
		List<Candidate> candidateList = candidateRepository.findAll();
		List<CandidateDto> candidateDtoList = candidateList.stream()
				.map(candidate -> candidateAssembler.fromEntity(candidate)).collect(Collectors.toList());
		return DefaultResponse.ok(candidateDtoList);
	}

	@GetMapping("/table")
	public DefaultResponse<List<TableCandidateDto>> getTable() {
		List<Candidate> candidateList = candidateRepository.findAll();
		List<TableCandidateDto> candidateDtoList = candidateList.stream()
				.map(candidate -> candidateAssembler.getTable(candidate)).collect(Collectors.toList());
		return DefaultResponse.ok(candidateDtoList);
	}

	@GetMapping("/{id}")
	public DefaultResponse<CandidateDto> get(@PathVariable String id) {
		CandidateDto candidateDto = candidateAssembler.fromEntity(candidateRepository.findById(id).get());
		return DefaultResponse.ok(candidateDto);
	}

//	@GetMapping("/availability/{status}")
//	public DefaultResponse<List<CandidateDto>> getByStatus(@PathVariable String status) {
//		List<Candidate> candidateList = candidateRepository.findByAvailKandidat(status);
//		List<CandidateDto> candidateDtoList = candidateList.stream()
//				.map(candidate -> candidateAssembler.fromEntity(candidate)).collect(Collectors.toList());
//		return DefaultResponse.ok(candidateDtoList);
//	}

	@GetMapping("/availability/{status}")
	public DefaultResponse<List<AllCandidateByStatus>> getByStatus(@PathVariable String status) {
		List<AllCandidateByStatus> availableCandidateList = candidateRepository.findAllCandidateByStatus(status);
		return DefaultResponse.ok(availableCandidateList);
	}

	@GetMapping("/available/lock")
	public DefaultResponse<List<AvailableCandidateProjectionLock>> getAvailableLock() {
		List<AvailableCandidateProjectionLock> availableCandidateList = candidateRepository
				.findAllAvailableCandidateLock();
		return DefaultResponse.ok(availableCandidateList);
	}

	/* Insert Data */
	@PostMapping
	public DefaultResponse<CandidateDto> insert(@RequestBody CandidateDto dto) {
		return DefaultResponse.ok(candidateService.insertCandidate(dto));
	}

	@DeleteMapping("/{id}")
	public DefaultResponse<Candidate> delete(@PathVariable String id) {
		Candidate candidate = candidateRepository.findById(id).get();
		candidateRepository.deleteById(id);
		return DefaultResponse.ok(candidate);
	}

	@PutMapping("/availability/{idCandidate}")
	public DefaultResponse<Candidate> update(@PathVariable String idCandidate, @RequestBody CandidateStatusHistoryDto dto) {
		Candidate entity = candidateRepository.findById(idCandidate).get();
		if (entity.getAvailKandidat() != dto.getCndAvail()) {
			CandidateStatusHistory cndhist = new CandidateStatusHistory();
			cndhist.setCndid(idCandidate);
			cndhist.setAvail(dto.getCndAvail());
			cndhist.setTanggalProses(dto.getTanggalProses());
			cndhist.setCreatedBy(dto.getCreatedBy());
			cndhist.setKeterangan(dto.getKeterangan());
			candidateStatusHistoryRepository.save(cndhist);
		}
		entity.setAvailKandidat(dto.getCndAvail());
		candidateRepository.save(entity);
		return DefaultResponse.ok(entity);
	}

}
