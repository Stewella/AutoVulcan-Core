package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements  EmployeeService{

    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(CandidateRepository candidateRepository, EmployeeRepository employeeRepository) {
        this.candidateRepository = candidateRepository;
        this.employeeRepository = employeeRepository;
    }


    @Override
    public Candidate insertData(Candidate entity, Integer isDelete) {
        entity.setIsDelete(isDelete);
        employeeRepository.save(entity.getEmployee());
        entity = candidateRepository.save(entity);
        return entity;
    }
}
