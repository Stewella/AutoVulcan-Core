package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.ServicesAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.ServicesDto;
import com.prosigmaka.catra.diglett.model.entity.Services;
import com.prosigmaka.catra.diglett.repository.ServicesRepo;
import com.prosigmaka.catra.diglett.service.ServService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/service")
public class ServController {
	private final ServicesRepo serviceRepo;
	private final ServService servService;
	private final ServicesAssembler servicesAssembler;

	public ServController(ServicesRepo serviceRepo, ServService servService, ServicesAssembler servicesAssembler) {
		this.serviceRepo = serviceRepo;
		this.servService = servService;
		this.servicesAssembler = servicesAssembler;
	}

	@PostMapping
	public DefaultResponse<ServicesDto> insert(@RequestBody ServicesDto dto){
		if(serviceRepo.findByService(dto.getService()) != null)
			return DefaultResponse.error("Service yang dimasukan sudah terdaftar");
		else {
			return DefaultResponse.ok(servService.insertService(dto));
		}
		
	}
	
	@GetMapping("/{id}")
	public DefaultResponse<ServicesDto> getById(@PathVariable String id) {
		Services service = serviceRepo.findById(id).get();
		ServicesDto serviceDto = servicesAssembler.fromEntity(service);
		return DefaultResponse.ok(serviceDto);
	}
	
	@DeleteMapping("/{id}")
	public DefaultResponse<Services> delete(@PathVariable String id) {
		Services service = serviceRepo.findById(id).get();
		serviceRepo.deleteById(id);
		return DefaultResponse.ok(service);
	}
	
	@GetMapping
	public DefaultResponse<List<ServicesDto>> get() {
		List<Services> serviceList = serviceRepo.findAll();
		List<ServicesDto> servicesDtoList = serviceList.stream().map(service -> servicesAssembler.fromEntity(service)).collect(Collectors.toList());
		return DefaultResponse.ok(servicesDtoList);
	}

}
