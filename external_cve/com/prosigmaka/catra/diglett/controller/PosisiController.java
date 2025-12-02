package com.prosigmaka.catra.diglett.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.prosigmaka.catra.diglett.assembler.PosisiAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.PosisiDto;
import com.prosigmaka.catra.diglett.model.entity.Posisi;
import com.prosigmaka.catra.diglett.repository.PosisiRepository;
import com.prosigmaka.catra.diglett.service.PosisiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posisi")
public class PosisiController {
    private final PosisiRepository posisiRepository;
    private final PosisiAssembler posisiAssembler;
    private final PosisiService posisiService;

    public PosisiController(PosisiRepository posisiRepository, PosisiAssembler posisiAssembler, PosisiService posisiService) {
        this.posisiRepository = posisiRepository;
        this.posisiAssembler = posisiAssembler;
        this.posisiService = posisiService;
    }

    @GetMapping
    public DefaultResponse<List<PosisiDto>> get() {
        List<Posisi> posisiList = posisiRepository.findAll();
        List<PosisiDto> posisiDtoList = posisiList.stream().map(posisi -> posisiAssembler.fromEntity(posisi))
                .collect(Collectors.toList());
        return DefaultResponse.ok(posisiDtoList);
    }

    @PostMapping
    public DefaultResponse<PosisiDto> insert(@RequestBody PosisiDto dto) {
        if (posisiRepository.findByPosisi(dto.getPosisi()) != null)
            return DefaultResponse.error("Posisi yang dimasukan sudah terdaftar");
        // else if (rumpunRepository.findByNama(dto.getCluster()) == null)
        // return DefaultResponse.error("Rumpun Posisi yang dimasukan tidak terdaftar");
        else
            return DefaultResponse.ok(posisiService.insertPosisi(dto));
    }

    @GetMapping("/{id}")
    public DefaultResponse<PosisiDto> getById(@PathVariable String id) {
        Posisi posisi = posisiRepository.findById(id).get();
        PosisiDto posisiDto = posisiAssembler.fromEntity(posisi);
        return DefaultResponse.ok(posisiDto);
    }

    @DeleteMapping("/{id}")
    public DefaultResponse<Posisi> delete(@PathVariable String id) {
        Posisi posisi = posisiRepository.findById(id).get();
        posisiRepository.deleteById(id);
        return DefaultResponse.ok(posisi);
    }
}
