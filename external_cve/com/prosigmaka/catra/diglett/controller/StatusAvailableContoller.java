package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.StatusAvailableAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.StatusAvailableDto;
import com.prosigmaka.catra.diglett.model.entity.StatusAvailable;
import com.prosigmaka.catra.diglett.repository.StatusAvailableRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/status")
public class StatusAvailableContoller {

    private final StatusAvailableAssembler assembler;
    private final StatusAvailableRepository repository;

    public StatusAvailableContoller(StatusAvailableAssembler assembler, StatusAvailableRepository repository) {
        this.assembler = assembler;
        this.repository = repository;
    }

    @GetMapping("/available")
    public DefaultResponse<List<StatusAvailableDto>> getAll() {
        List<StatusAvailable> statusAvailableList = repository.findAllByGrupAvailableEquals("available");
        List<StatusAvailableDto> statusAvailableDtoList = statusAvailableList.stream()
                                                           .map(status -> assembler.fromEntity(status))
                                                           .collect(Collectors.toList());
        if (statusAvailableDtoList == null) {
            return DefaultResponse.error("Gagal menampilkan data");
        }

        return DefaultResponse.ok(statusAvailableDtoList);
    }
}
