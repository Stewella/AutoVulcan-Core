package com.prosigmaka.catra.diglett.assembler;

import com.prosigmaka.catra.diglett.model.dto.ProjectDto;
import com.prosigmaka.catra.diglett.model.entity.Project;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectAssembler implements InterfaceAssembler<Project, ProjectDto>{
	@Autowired
	private ClientRepository clientRepository;
	@Override
	public Project fromDto(ProjectDto dto) {
		if(dto == null) return null;
		Project entity = new Project();
		if(dto.getIdProject()!=null) entity.setIdProject(dto.getIdProject());
		if(dto.getNameProject()!=null) entity.setNameProject(dto.getNameProject());
		if(dto.getPicProject()!=null) entity.setPicProject(dto.getPicProject());
		if(dto.getCompany()!=null) entity.setIdClient(clientRepository.findByNama(dto.getCompany()).get().getId());
		return entity;
	}

	@Override
	public ProjectDto fromEntity(Project entity) {
		if(entity == null) return null;
		String namaCompany = null;
		if(entity.getIdClient()!=null) namaCompany = clientRepository.findById(entity.getIdClient()).get().getNama();
		return ProjectDto.builder()
				.idProject(entity.getIdProject())
				.nameProject(entity.getNameProject())
				.picProject(entity.getPicProject())
				.company(namaCompany)
				.build();
	}

}
