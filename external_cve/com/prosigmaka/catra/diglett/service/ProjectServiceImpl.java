package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.assembler.ProjectAssembler;
import com.prosigmaka.catra.diglett.model.dto.ProjectDto;
import com.prosigmaka.catra.diglett.model.entity.Project;
import com.prosigmaka.catra.diglett.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{
	private final ProjectRepository repository;
    private final ProjectAssembler assembler;

    public ProjectServiceImpl(ProjectRepository repository, ProjectAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
	public ProjectDto insertProject(ProjectDto dto) {
		Project entity = repository.save(assembler.fromDto(dto));
        repository.save(entity);
        return assembler.fromEntity(entity);
	}

}
