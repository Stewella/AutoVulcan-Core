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

import com.prosigmaka.catra.diglett.assembler.StepDetailAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.StepDetailDto;
import com.prosigmaka.catra.diglett.model.entity.StepDetail;
import com.prosigmaka.catra.diglett.repository.StepDetailRepository;
import com.prosigmaka.catra.diglett.service.StepDetailService;

@RestController
@RequestMapping("/step-detail")
public class StepDetailController {
	private final StepDetailRepository repository;
	private final StepDetailAssembler assembler;
	private final StepDetailService service;

	public StepDetailController(StepDetailRepository repository, StepDetailAssembler assembler, StepDetailService service) {
		this.repository = repository;
		this.assembler = assembler;
		this.service = service;
	}

	@PostMapping
	public DefaultResponse<StepDetail> insert(@RequestBody StepDetailDto dto) {
		return DefaultResponse.ok(service.insertStepDetail(dto));
	}

	@GetMapping
	public DefaultResponse<List<StepDetailDto>> get() {
		List<StepDetail> stepDetailList = repository.findAll();
		List<StepDetailDto> stepDetailDtoList = stepDetailList.stream().map(stepdet -> assembler.fromEntity(stepdet))
			.collect(Collectors.toList());
		return DefaultResponse.ok(stepDetailDtoList);
	}

	@GetMapping("/{cliid}")
	public DefaultResponse<List<StepDetailDto>> getStepDetailByClient(@PathVariable String cliid) {
		List<StepDetail> stepDetailList = repository.findAllByClientId(cliid);
		List<StepDetailDto> stepDetailDtoList = stepDetailList.stream().map(stepdet->assembler.fromEntity(stepdet))
			.collect(Collectors.toList());
		return DefaultResponse.ok(stepDetailDtoList);
	}
	
	// @GetMapping("/tahapan")
	// public List<TahapanKandidat> getTahapan(@RequestParam String cndid){
	// 	List<TahapanKandidat> step=trackRepo.findTahapan(cndid);
	// 	return step;
	// }
	
	@DeleteMapping("/{id}")
	public DefaultResponse<StepDetail> delete(@PathVariable String id) {
		StepDetail stepdet = repository.findById(id).get();
		repository.deleteById(id);
		return DefaultResponse.ok(stepdet);
	}
	
	@PostMapping("/changeOrder/{id}")
	public DefaultResponse<StepDetail>orderChange(@PathVariable String id, @RequestBody StepDetail stepDetail ){
		StepDetail entity = repository.findById(id).get();
		entity.setOrderStep(stepDetail.getOrderStep());
		repository.save(entity);
		return DefaultResponse.ok(entity);
		
	}
}
