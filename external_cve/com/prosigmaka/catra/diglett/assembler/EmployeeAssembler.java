package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.EmployeeDto;
import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.entity.Employee;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeAssembler implements InterfaceAssembler<Candidate, EmployeeDto>{

    private final ModelMapper modelMapper;
    private final CandidateRepository candidateRepository;

    public EmployeeAssembler(ModelMapper modelMapper, CandidateRepository candidateRepository) {
        this.modelMapper = modelMapper;
        this.candidateRepository = candidateRepository;
    }


    @Override
    public Candidate fromDto(EmployeeDto dto) {
        if (dto == null)
            return null;

        Employee employee = modelMapper.map(dto, Employee.class);
        Candidate entity = new Candidate();

        if (dto.getId() != null)
            entity.setId(dto.getId());
        if (dto.getKode() != null)
            entity.setCndkode(dto.getKode());
        if (dto.getNama() != null)
            entity.setNama(dto.getNama());
        if (dto.getJenisKelamin() != null)
            entity.setJenisKelamin(dto.getJenisKelamin());
        if (dto.getTempatLahir() != null)
            entity.setTempatLahir(dto.getTempatLahir());
        if (dto.getTanggalLahir() != null)
            entity.setTanggalLahir(dto.getTanggalLahir());
        if (dto.getAlamat() != null)
            entity.setAlamat(dto.getAlamat());
        if (dto.getEmail() != null)
            entity.setEmailPrimary(dto.getEmail());
        if (dto.getNoHp() != null)
            entity.setNoHpPrimary(dto.getNoHp());
        if(dto.getEkspektasiGaji() !=null)
            entity.setEkspektasiGaji(dto.getEkspektasiGaji());
        if(dto.getWaktuAvailable() !=null)
            entity.setWaktuAvailable(dto.getWaktuAvailable());
        if(dto.getTanggalProses() !=null)
            entity.setTanggalProses(dto.getTanggalProses());
        if(dto.getAvail()!=null)
            entity.setAvailKandidat(dto.getAvail());
        if(dto.getTanggalProses() == null)
            entity.setTanggalProses(candidateRepository.findByIdEquals(dto.getId()).getTanggalProses());
        if(dto.getWaktuAvailable() == null)
            entity.setWaktuAvailable(candidateRepository.findByIdEquals(dto.getId()).getWaktuAvailable());


        entity.setEmployee(employee);

        return entity;
    }

    @Override
    public EmployeeDto fromEntity(Candidate entity) {
        if (entity == null)
            return null;

        EmployeeDto dto = new EmployeeDto();
        modelMapper.map(entity, dto);
        dto.setAvail(entity.getAvailKandidat());
        dto.setKode(entity.getCndkode());
        dto.setEmail(entity.getEmailPrimary());
        dto.setNoHp(entity.getNoHpPrimary());

        if (entity.getEmployee() != null)
            modelMapper.map(entity.getEmployee(), dto);

        return dto;
    }
}
