package com.prosigmaka.catra.diglett.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

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

import com.prosigmaka.catra.diglett.ExcelExporter;
import com.prosigmaka.catra.diglett.assembler.DetailKebutuhanAssembler;
import com.prosigmaka.catra.diglett.assembler.OptiAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.DetailKebutuhanDto;
//import com.prosigmaka.catra.diglett.model.dto.OptiDto;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;
//import com.prosigmaka.catra.diglett.model.projection.AvailableCandidateProjection;
import com.prosigmaka.catra.diglett.model.projection.KebutuhanHistoryProjection;
import com.prosigmaka.catra.diglett.model.projection.OptiProjection;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;
import com.prosigmaka.catra.diglett.service.DetailKebutuhanHistoryService;
import com.prosigmaka.catra.diglett.service.DetailKebutuhanServiceImpl;

@RestController
@RequestMapping("/detailkeb")
public class DetailKebutuhanController {
	private final DetailKebutuhanRepository detkebRepo;
	private final DetailKebutuhanAssembler detkebAssembler;
	private final DetailKebutuhanServiceImpl service;
	
	private final DetailKebutuhanHistoryService detKebHistService;
	
	@SuppressWarnings("unused")
	private final OptiAssembler optiAssembler;

	public DetailKebutuhanController(DetailKebutuhanRepository detkebRepo, DetailKebutuhanAssembler detkebAssembler, DetailKebutuhanServiceImpl service, DetailKebutuhanHistoryService detKebHistService, OptiAssembler optiAssembler) {
		this.detkebRepo = detkebRepo;
		this.detkebAssembler = detkebAssembler;
		this.service = service;
		this.detKebHistService = detKebHistService;
		this.optiAssembler = optiAssembler;
	}

	@PostMapping("/insert")
	public DefaultResponse<DetailKebutuhan> insert(@RequestBody DetailKebutuhanDto dto) {
		DetailKebutuhan entity = detkebAssembler.fromDto(dto);
		detkebRepo.save(entity);
		String id = entity.getId();
		service.saveKode(id);
		return DefaultResponse.ok(entity);
	}

	@GetMapping("/getAll")
	public DefaultResponse<List<DetailKebutuhanDto>> getAll() {
		List<DetailKebutuhan> entityList = detkebRepo.findAll();
		List<DetailKebutuhanDto> dtoList = entityList.stream().map(detkeb -> detkebAssembler.fromEntity(detkeb))
				.collect(Collectors.toList());
		return DefaultResponse.ok(dtoList);
	}

	@GetMapping("/getAllKeb/{cndid}")
	public DefaultResponse<List<DetailKebutuhanDto>> getAllKeb(@PathVariable String cndid) {
		List<DetailKebutuhan> entityList = detkebRepo.findAllKeb(cndid);
		List<DetailKebutuhanDto> dtoList = entityList.stream().map(detkeb -> detkebAssembler.fromEntity(detkeb))
				.collect(Collectors.toList());
		return DefaultResponse.ok(dtoList);
	}

	@GetMapping("/getAllKebAvail")
	public DefaultResponse<List<DetailKebutuhanDto>> getAllKebAvail() {
		List<DetailKebutuhan> entityList = detkebRepo.findAllKebAvail();
		List<DetailKebutuhanDto> dtoList = entityList.stream().map(detkeb -> detkebAssembler.fromEntity(detkeb))
				.collect(Collectors.toList());
		return DefaultResponse.ok(dtoList);
	}

	// opti
//	@GetMapping("/getOpti")
//	public DefaultResponse<List<OptiDto>> getOpti(){
//		List<DetailKebutuhan> entityList = detkebRepo.findAllKeb();
//		List<OptiDto> dtoList = entityList.stream().map(opti -> optiAssembler.fromEntity(opti))
//				.collect(Collectors.toList());
//		return DefaultResponse.ok(dtoList);
//	}

	@GetMapping("/getOpti")
	public DefaultResponse<List<OptiProjection>> getOpti() {
		List<OptiProjection> optiList = detkebRepo.findopti();
//		List<OptiDto> dtos= service.listAll();
		return DefaultResponse.ok(optiList);
	}

	@GetMapping("/exportExcel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		response.getContentType();
		DateFormat dateFormat = new SimpleDateFormat("dd MMMMM yyyy", new Locale("id"));
		String currentDate = dateFormat.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=OptiTable - " + currentDate + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<OptiProjection> dtos = service.listByOptiProjection();
//		List<OptiDto> dtos= service.listAll();

		ExcelExporter excelExporter = new ExcelExporter(dtos);

		excelExporter.export(response);

	}

	@GetMapping("/bykode/{kode}")
	public DefaultResponse<DetailKebutuhanDto> getByKode(@PathVariable String kode) {
		DetailKebutuhan entity = detkebRepo.findByKode(kode);
		DetailKebutuhanDto dto = detkebAssembler.fromEntity(entity);
		return DefaultResponse.ok(dto);
	}

	@GetMapping("/bypic/{idPic}")
	public DefaultResponse<List<DetailKebutuhanDto>> getByPic(@PathVariable String idPic) {
		List<DetailKebutuhan> entityList = detkebRepo.findByIdPic(idPic);
		List<DetailKebutuhanDto> dtoList = entityList.stream().map(detkeb -> detkebAssembler.fromEntity(detkeb))
				.collect(Collectors.toList());
		return DefaultResponse.ok(dtoList);
	}

	@GetMapping("/bycnd/{idCnd}")
	public DefaultResponse<List<KebutuhanHistoryProjection>> getKebutuhanHistoryByCandidate(
			@PathVariable String idCnd) {
		List<KebutuhanHistoryProjection> kebutuhanHistList = detkebRepo.findKebutuhanHistoryByCandidate(idCnd);
		return DefaultResponse.ok(kebutuhanHistList);
	}

	@PutMapping("/hired/{kode}")
	public DefaultResponse<DetailKebutuhan> update(@PathVariable String kode) {
		detKebHistService.insertJumlah(detkebRepo.findByKode(kode).getId());
		DetailKebutuhan entity = service.updateJumlah(kode);
		return DefaultResponse.ok(entity);
	}
	
	@PutMapping("/updatejumlah/{kode}/{jumlah}")
	public DefaultResponse<DetailKebutuhan> updateJumlah(@PathVariable String kode, @PathVariable Integer jumlah) {
		DetailKebutuhan entity = detkebRepo.findByKode(kode);
		entity.setJumlah(jumlah);
		detkebRepo.save(entity);
		return DefaultResponse.ok(entity);
	}

	@DeleteMapping("/{id}")
	public DefaultResponse<DetailKebutuhan> delete(@PathVariable String id) {
		DetailKebutuhan detkeb = detkebRepo.findById(id).get();
		detkebRepo.deleteById(id);
		return DefaultResponse.ok(detkeb);
	}
}
