package com.prosigmaka.catra.diglett.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosigmaka.catra.diglett.assembler.SalesAssembler;
import com.prosigmaka.catra.diglett.model.dto.SalesDto;
import com.prosigmaka.catra.diglett.model.entity.Sales;
import com.prosigmaka.catra.diglett.repository.SalesRepository;

@Service
@Transactional
public class SalesServiceImpl implements SalesService {
    private final SalesRepository salesRepository;
    private final SalesAssembler salesAssembler;

    public SalesServiceImpl(SalesRepository salesRepository, SalesAssembler salesAssembler) {
        this.salesRepository = salesRepository;
        this.salesAssembler = salesAssembler;
    }

    @Override
    public SalesDto insertSales(SalesDto dto) {
        Sales entity = salesRepository.save(salesAssembler.fromDto(dto));
        salesRepository.save(entity);
        return salesAssembler.fromEntity(entity);
    }
}
