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

import com.prosigmaka.catra.diglett.assembler.SalesAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.SalesDto;
import com.prosigmaka.catra.diglett.model.entity.Sales;
import com.prosigmaka.catra.diglett.repository.SalesRepository;
import com.prosigmaka.catra.diglett.service.SalesService;

@RestController
@RequestMapping("/sales")
public class SalesController {
    private final SalesRepository salesRepository;
    private final SalesAssembler salesAssembler;
    private final SalesService salesService;

    public SalesController(SalesRepository salesRepository, SalesAssembler salesAssembler, SalesService salesService) {
        this.salesRepository = salesRepository;
        this.salesAssembler = salesAssembler;
        this.salesService = salesService;
    }

    @GetMapping
    public DefaultResponse<List<SalesDto>> get() {
        List<Sales> salesList = salesRepository.findAll();
        List<SalesDto> salesDtoList = salesList.stream().map(sales -> salesAssembler.fromEntity(sales))
                .collect(Collectors.toList());
        return DefaultResponse.ok(salesDtoList);
    }

    @PostMapping
    public DefaultResponse<SalesDto> insert(@RequestBody SalesDto dto) {
        if (salesRepository.findByNama(dto.getNama()) != null)
            return DefaultResponse.error("Nama yang dimasukan sudah terdaftar");
        // else if (rumpunRepository.findByNama(dto.getCluster()) == null)
        // return DefaultResponse.error("Rumpun Posisi yang dimasukan tidak terdaftar");
        else
            return DefaultResponse.ok(salesService.insertSales(dto));        
    }

	@GetMapping("/{id}")
	public DefaultResponse<SalesDto> getSalesById(@PathVariable Integer id) {
		Sales sales = salesRepository.findById(id).get();
		SalesDto salesDto = salesAssembler.fromEntity(sales);
		return DefaultResponse.ok(salesDto);
	}

	@DeleteMapping("/{id}")
	public DefaultResponse<Sales> delete(@PathVariable Integer id) {
		Sales sales = salesRepository.findById(id).get();
		salesRepository.deleteById(id);
		return DefaultResponse.ok(sales);
	}
	
}
