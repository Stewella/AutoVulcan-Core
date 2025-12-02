package com.prosigmaka.catra.diglett.controller;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prosigmaka.catra.diglett.assembler.DetailKebutuhanHistoryAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.DetailKebutuhanHistoryDto;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhan;
import com.prosigmaka.catra.diglett.model.entity.DetailKebutuhanHistory;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanHistoryRepository;
import com.prosigmaka.catra.diglett.repository.DetailKebutuhanRepository;

@RestController
@RequestMapping("/detailkebhist")
public class DetailKebutuhanHistoryController {
    private final DetailKebutuhanHistoryRepository detailKebutuhanHistoryRepository;
    private final DetailKebutuhanRepository detailKebutuhanRepository;
    private final DetailKebutuhanHistoryAssembler detailKebutuhanHistoryAssembler;

    public DetailKebutuhanHistoryController(DetailKebutuhanHistoryRepository detailKebutuhanHistoryRepository, DetailKebutuhanRepository detailKebutuhanRepository, DetailKebutuhanHistoryAssembler detailKebutuhanHistoryAssembler) {
        this.detailKebutuhanHistoryRepository = detailKebutuhanHistoryRepository;
        this.detailKebutuhanRepository = detailKebutuhanRepository;
        this.detailKebutuhanHistoryAssembler = detailKebutuhanHistoryAssembler;
    }

    @PostMapping("/getAll")
    public DefaultResponse<List<DetailKebutuhanHistoryDto>> getAll() {
        List<DetailKebutuhanHistory> entityList = detailKebutuhanHistoryRepository.findAll();
        List<DetailKebutuhanHistoryDto> dtoList = entityList.stream().map(detkeb -> detailKebutuhanHistoryAssembler.fromEntity(detkeb))
                .collect(Collectors.toList());
        return DefaultResponse.ok(dtoList);
    }

    @PostMapping("/insert")
    public DefaultResponse<DetailKebutuhanHistory> insert(@RequestBody DetailKebutuhanHistoryDto dto) {
        DetailKebutuhanHistory entity = detailKebutuhanHistoryAssembler.fromDto(dto);
        DetailKebutuhan detkeb = detailKebutuhanRepository.findById(entity.getIdDetKeb()).get();
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        detkeb.setJumlah(entity.getCurrent());
        entity.setCreatedDate(date);
        detailKebutuhanRepository.save(detkeb);
        detailKebutuhanHistoryRepository.save(entity);
        return DefaultResponse.ok(entity);
    }
}
