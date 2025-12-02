package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.EmployeeAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.EmployeeDto;
import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.entity.Employee;
import com.prosigmaka.catra.diglett.model.enummodel.AvailKandidatEnum;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.EmployeeRepository;
import com.prosigmaka.catra.diglett.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final EmployeeAssembler employeeAssembler;
    private final CandidateRepository candidateRepository;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeService, EmployeeAssembler employeeAssembler, CandidateRepository candidateRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.employeeAssembler = employeeAssembler;
        this.candidateRepository = candidateRepository;
    }

    //get all data
    @GetMapping("/get-all")
    public DefaultResponse<List<EmployeeDto>> getAll() {
        List<Candidate> candidateList = candidateRepository.findAllByAvailKandidatEqualsAndIsDeleteEquals(AvailKandidatEnum.HIRED.getValue(), 0);
        List<EmployeeDto> employeeDtos = candidateList.stream()
                                                     .map(employee -> employeeAssembler.fromEntity(employee))
                                                     .collect(Collectors.toList());
        if (employeeDtos == null)
            return DefaultResponse.error("Data gagal ditampilkan");

        return DefaultResponse.ok(employeeDtos) ;
    }

    //insert data
    @PostMapping("/insert")
    public DefaultResponse<Candidate> insertData(@RequestBody EmployeeDto dto) {
        Candidate entity = employeeAssembler.fromDto(dto);

        entity = employeeService.insertData(entity, 0);
        if (entity == null)
            return DefaultResponse.error("Gagal menyimpan");

        return DefaultResponse.ok(entity, "Berhasil menyimpan");
    }

    //delete data
    @PostMapping("/delete/{id}")
    public DefaultResponse<Candidate> deleteData(@PathVariable String id) {
        Candidate entity = candidateRepository.findByIdEquals(id);
        entity = employeeService.insertData(entity, 1);
        if (entity == null)
            return DefaultResponse.error("Gagal menghapus");

        return DefaultResponse.ok(entity, "Berhasil menghapus");
    }
}
