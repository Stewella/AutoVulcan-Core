package com.prosigmaka.catra.diglett.controller;

import com.prosigmaka.catra.diglett.assembler.ProjectAssembler;
import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.dto.ProjectDto;
import com.prosigmaka.catra.diglett.model.entity.Project;
import com.prosigmaka.catra.diglett.repository.ClientRepository;
import com.prosigmaka.catra.diglett.repository.ProjectRepository;
import com.prosigmaka.catra.diglett.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
public class ProjectController {
	private final ProjectRepository projectRepository;
	private final ClientRepository clientRepository;
	private final ProjectService projectService;
	private final ProjectAssembler projectAssembler;

	public ProjectController(ProjectRepository projectRepository, ClientRepository clientRepository, ProjectService projectService, ProjectAssembler projectAssembler) {
		this.projectRepository = projectRepository;
		this.clientRepository = clientRepository;
		this.projectService = projectService;
		this.projectAssembler = projectAssembler;
	}

	@PostMapping
	public DefaultResponse<ProjectDto> insert(@RequestBody ProjectDto dto) {
		return DefaultResponse.ok(projectService.insertProject(dto));
	}
	
	@GetMapping
	public DefaultResponse<List<ProjectDto>> get() {
		List<Project> userList = projectRepository.findAll();
		List<ProjectDto> userDtoList = userList.stream().map(user -> projectAssembler.fromEntity(user))
				.collect(Collectors.toList());
		return DefaultResponse.ok(userDtoList);
	}
	
	@GetMapping("/{id}")
	public DefaultResponse<ProjectDto> getUserById(@PathVariable String id) {
		Project user = projectRepository.findById(id).get();
		ProjectDto userDto = projectAssembler.fromEntity(user);
		return DefaultResponse.ok(userDto);
	}

	@GetMapping("/client/{nmClient}")
    public DefaultResponse<List<ProjectDto>> getProjectByClient(@PathVariable String nmClient) {
        String cliid = clientRepository.findIdCli(nmClient);
		List<Project> proList = projectRepository.findProjectNameByIdClient(cliid);
		List<ProjectDto> proDtoList = proList.stream().map(pro-> projectAssembler.fromEntity(pro))
				.collect(Collectors.toList());
        return DefaultResponse.ok(proDtoList);
	}
	
	@DeleteMapping("/{id}")
	public DefaultResponse<Project> delete(@PathVariable String id) {
		Project project = projectRepository.findById(id).get();
		projectRepository.deleteById(id);
		return DefaultResponse.ok(project);
	}
}
