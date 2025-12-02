package com.prosigmaka.catra.diglett.controller;

import java.util.List;
import java.util.Optional;
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

import com.prosigmaka.catra.diglett.assembler.ClientAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.ClientDto;
import com.prosigmaka.catra.diglett.model.entity.Client;
import com.prosigmaka.catra.diglett.model.entity.Sales;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.SalesRepository;
import com.prosigmaka.catra.diglett.service.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {
	private final ClientRepository clientRepository;
	private final ClientService clientService;
	private final ClientAssembler clientAssembler;
	private final SalesRepository salesRepository;

	public ClientController(ClientRepository clientRepository, ClientService clientService, ClientAssembler clientAssembler, SalesRepository salesRepository) {
		this.clientRepository = clientRepository;
		this.clientService = clientService;
		this.clientAssembler = clientAssembler;
		this.salesRepository = salesRepository;
	}

	@PostMapping
	public DefaultResponse<ClientDto> insert(@RequestBody ClientDto dto) {
		return DefaultResponse.ok(clientService.insertClient(dto));
	}
	
	@GetMapping
	public DefaultResponse<List<ClientDto>> get() {
		List<Client> clientList = clientRepository.findAll();
		List<ClientDto> clientDtoList = clientList.stream().map(client -> clientAssembler.fromEntity(client))
				.collect(Collectors.toList());
		return DefaultResponse.ok(clientDtoList);
	}
	
	@GetMapping("/name")
	public DefaultResponse<List<Object>> getClient() {
		return DefaultResponse.ok(clientRepository.getClient());
	}
	
	@GetMapping("/count")
	public DefaultResponse<Integer> getCount() {
		return DefaultResponse.ok(clientRepository.getCount());
	}
	
	@GetMapping("/countbyind")
	public DefaultResponse<List<Object>> getCountByInd() {
		return DefaultResponse.ok(clientRepository.getCountByInd());
	}
	
	@GetMapping("/industry")
	public DefaultResponse<List<Object>> getIndustry() {
		return DefaultResponse.ok(clientRepository.getIndustry());
	}
	
	@GetMapping("/{id}")
	public DefaultResponse<ClientDto> getClientById(@PathVariable String id) {
		Client client = clientRepository.findById(id).get();
		ClientDto clientDto = clientAssembler.fromEntity(client);
		return DefaultResponse.ok(clientDto);
	}
	
	@GetMapping("/nama/{nama}")
	public DefaultResponse<String> getClientByNama(@PathVariable String nama) {
		Client client = clientRepository.findByNickname(nama).get();
		Optional<Sales> optionalSales = salesRepository.findByNama(client.getSales().getNama());
		if (optionalSales.isPresent()){
			String email = optionalSales.get().getEmail();
			return DefaultResponse.ok(email);
		} else {
			return DefaultResponse.notFound();
		}
	}
	
	@DeleteMapping("/{id}")
	public DefaultResponse<Client> delete(@PathVariable String id) {
		Client client = clientRepository.findById(id).get();
		clientRepository.deleteById(id);
		return DefaultResponse.ok(client);
	}
}
