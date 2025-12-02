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

import com.prosigmaka.catra.diglett.assembler.IndustryAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.IndustryDto;
import com.prosigmaka.catra.diglett.model.entity.Industry;
import com.prosigmaka.catra.diglett.repository.IndustryRepository;
import com.prosigmaka.catra.diglett.service.IndustryService;

@RestController
@RequestMapping("/industry")
public class IndustryController {
	private final IndustryRepository repository;
	private final IndustryService service;
	private final IndustryAssembler assembler;

	public IndustryController(IndustryRepository repository, IndustryService service, IndustryAssembler assembler) {
		this.repository = repository;
		this.service = service;
		this.assembler = assembler;
	}

	@PostMapping
	public DefaultResponse<IndustryDto> insert(@RequestBody IndustryDto dto) {
		return DefaultResponse.ok(service.insertIndustry(dto));
	}
	
	@GetMapping
	public DefaultResponse<List<IndustryDto>> get() {
		List<Industry> industryList = repository.findAll();
		List<IndustryDto> industryDtoList = industryList.stream().map(industry -> assembler.fromEntity(industry))
				.collect(Collectors.toList());
		return DefaultResponse.ok(industryDtoList);
	}
	
	@GetMapping("/name")
	public DefaultResponse<List<Object>> getIndustry() {
		List<Object> industry = repository.getIndustry();
		return DefaultResponse.ok(industry);
	}
	
	@GetMapping("/{id}")
	public DefaultResponse<IndustryDto> getIndustryById(@PathVariable Integer id) {
		Industry industry = repository.findById(id).get();
		IndustryDto industryDto = assembler.fromEntity(industry);
		return DefaultResponse.ok(industryDto);
	}
	
	@DeleteMapping("/{id}")
	public DefaultResponse<Industry> delete(@PathVariable Integer id) {
		Industry industry = repository.findById(id).get();
		repository.deleteById(id);
		return DefaultResponse.ok(industry);
	}
}
