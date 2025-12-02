package com.prosigmaka.catra.diglett.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prosigmaka.catra.diglett.assembler.StepAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.StepDto;
import com.prosigmaka.catra.diglett.model.entity.Step;
import com.prosigmaka.catra.diglett.repository.PlottingDetailRepository;
import com.prosigmaka.catra.diglett.repository.StepDetailRepository;
import com.prosigmaka.catra.diglett.repository.StepRepository;
import com.prosigmaka.catra.diglett.service.StepService;

@RestController
@RequestMapping("/step")
public class StepController {
	private final StepRepository repository;
	private final StepDetailRepository stepDetailRepository;
	private final PlottingDetailRepository plottingDetailRepository;
	private final StepAssembler stepAssembler;
	private final StepService stepService;

	public StepController(StepRepository repository, StepDetailRepository stepDetailRepository, PlottingDetailRepository plottingDetailRepository, StepAssembler stepAssembler, StepService stepService) {
		this.repository = repository;
		this.stepDetailRepository = stepDetailRepository;
		this.plottingDetailRepository = plottingDetailRepository;
		this.stepAssembler = stepAssembler;
		this.stepService = stepService;
	}

	@PostMapping
	public DefaultResponse<Step> insert(@RequestBody StepDto dto) {
		return DefaultResponse.ok(stepService.insertStep(dto));
	}
	@GetMapping
	public DefaultResponse<List<StepDto>> get() {
		List<Step> stepList = repository.findAll();
		List<StepDto> stepDtoList = stepList.stream().map(step -> stepAssembler.fromEntity(step))
			.collect(Collectors.toList());
		return DefaultResponse.ok(stepDtoList);
	}
	@DeleteMapping("/{id}")
	public DefaultResponse<Step> delete(@PathVariable String id) {
		Step step = repository.findById(id).get();
		List<String> plotDetId = plottingDetailRepository.findPlotDetailIdByStepId(id);
		List<String> stepDetId = stepDetailRepository.findStepDetailIdByStepId(id);
		plotDetId.stream().forEach(plotid -> plottingDetailRepository.deleteById(plotid));
		stepDetId.stream().forEach(stepdetid -> stepDetailRepository.deleteById(stepdetid));
		repository.deleteById(id);
		return DefaultResponse.ok(step);
	}
}
