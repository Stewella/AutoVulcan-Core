package com.prosigmaka.catra.diglett.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prosigmaka.catra.diglett.assembler.PicClientAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.PicClientDto;
import com.prosigmaka.catra.diglett.model.entity.PicClient;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.PicClientRepository;

@RestController
@RequestMapping("/pic")
public class PicClientController {
	private final PicClientRepository picRepo;
	private final PicClientAssembler picAssembler;
	private final ClientRepository clientRepository;

	public PicClientController(PicClientRepository picRepo, PicClientAssembler picAssembler, ClientRepository clientRepository) {
		this.picRepo = picRepo;
		this.picAssembler = picAssembler;
		this.clientRepository = clientRepository;
	}

	@PostMapping("/insert")
	public DefaultResponse<PicClient> insert(@RequestBody PicClientDto dto) {
		PicClient entity = picAssembler.fromDto(dto);
		picRepo.save(entity);
		return DefaultResponse.ok(entity);
	}

	@GetMapping("/getAll")
	public DefaultResponse<List<PicClientDto>> getAll() {
		List<PicClient> entityList = picRepo.findAll();
		List<PicClientDto> dtoList = entityList.stream().map(pic -> picAssembler.fromEntity(pic))
				.collect(Collectors.toList());
		return DefaultResponse.ok(dtoList);
	}

	@GetMapping("/byclient/{nmClient}")
	public DefaultResponse<List<PicClientDto>> getByPic(@PathVariable String nmClient) {
		String idClient = clientRepository.findIdCli(nmClient);
		List<PicClient> entityList = picRepo.findPicClientByIdClient(idClient);
		List<PicClientDto> dtoList = entityList.stream().map(pic -> picAssembler.fromEntity(pic))
				.collect(Collectors.toList());
		return DefaultResponse.ok(dtoList);
	}
	
	@GetMapping("/byid/{id}")
	public DefaultResponse<PicClientDto> getById(@PathVariable String id) {
		PicClient entity = picRepo.findById(id).get();
		PicClientDto dto = picAssembler.fromEntity(entity);
		return DefaultResponse.ok(dto);
	}

	@DeleteMapping("/{id}")
	public DefaultResponse<PicClient> delete(@PathVariable String id) {
		PicClient pic = picRepo.findById(id).get();
		picRepo.deleteById(id);
		return DefaultResponse.ok(pic);
	}

	@PutMapping("/isOpen/{id}")
	public DefaultResponse<PicClient> update(@PathVariable String id, @RequestBody PicClientDto dto) {
		PicClient entity = picRepo.findById(id).get();
		entity.setIsOpen(dto.getIsOpen());
		picRepo.save(entity);
		return DefaultResponse.ok(entity);
	}

	@PutMapping("/edit/{id}")
	public DefaultResponse<PicClient> edit(@PathVariable String id, @RequestBody PicClientDto dto) {
		PicClient entity = picRepo.findById(id).get();
		if (dto.getPicName() != null)
			entity.setPicName(dto.getPicName());
		if (dto.getPicEmail() != null)
			entity.setPicEmail(dto.getPicEmail());
		if (dto.getPicNoHp() != null)
			entity.setPicNoHp(dto.getPicNoHp());
		picRepo.save(entity);
		return DefaultResponse.ok(entity);
	}

}
