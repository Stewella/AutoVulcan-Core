package com.prosigmaka.catra.diglett.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.model.projection.DashboardStatusPerMonth;
import com.prosigmaka.catra.diglett.model.projection.DashboardTechProjection;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.CandidateStatusHistoryRepository;
import com.prosigmaka.catra.diglett.repository.RecommendPosisiRepository;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	private final CandidateRepository repository;
	private final RecommendPosisiRepository recommendRepository;
	private final CandidateStatusHistoryRepository candstatrepository;

	public DashboardController(CandidateRepository repository, RecommendPosisiRepository recommendRepository, CandidateStatusHistoryRepository candstatrepository) {
		this.repository = repository;
		this.recommendRepository = recommendRepository;
		this.candstatrepository = candstatrepository;
	}

	@GetMapping("/total")
	public DefaultResponse<Integer> getTotal() {
		Integer total = repository.totalCandidate();
		return DefaultResponse.ok(total);
	}

	@GetMapping("/male")
	public DefaultResponse<List<Object>> getMale() {
		List<Object> male = new ArrayList<>();
		Integer totalMale = repository.MaleCandidate();
		Integer total = repository.totalCandidate();
		male.add(totalMale);
		Double persen = (totalMale.doubleValue() / total.doubleValue()) * 100;
		male.add(String.format("%.1f", persen));
		return DefaultResponse.ok(male);
	}

	@GetMapping("/female")
	public DefaultResponse<List<Object>> getFemale() {
		List<Object> female = new ArrayList<>();
		Integer totalFemale = repository.FemaleCandidate();
		Integer total = repository.totalCandidate();
		female.add(totalFemale);
		Double persen = (totalFemale.doubleValue() / total.doubleValue()) * 100;
		female.add(String.format("%.1f", persen));
		return DefaultResponse.ok(female);
	}
	
	@GetMapping("/cndByPosition")
	public DefaultResponse<List<Object>> candidateByPosition() {
		List<Object> cndByPos=repository.candidateByPosisi();
		return DefaultResponse.ok(cndByPos);
	}
	
	@GetMapping("/labelCluster")
	public DefaultResponse<List<Object>> labelCluster() {
		List<Object> label = repository.nameCluster();
		return DefaultResponse.ok(label);
	}
	
	@GetMapping("/dataCluster")
	public DefaultResponse<List<Integer>> dataCluster() {
		List<Integer> data = repository.countCluster();
//		Integer total = repository.totalCandidate();
//		List<Object> persen = new ArrayList<>();
//		data.forEach(item->{
//			persen.add(String.format("%.1f", (item.doubleValue()/total.doubleValue())*100));
//		});
		return DefaultResponse.ok(data);
	}
	
	@GetMapping("/labelSkill")
	public DefaultResponse<List<String>> labelSkill() {
		List<String> label = repository.nameSkill();
		return DefaultResponse.ok(label);
	}
	
	@GetMapping("/countSkill")
	public DefaultResponse<List<Integer>> countSkill() {
		List<Integer> count = repository.countSkill();
		return DefaultResponse.ok(count);
	}
	
	@PostMapping("/dashboardTech")
	public DefaultResponse<List<DashboardTechProjection>> getCountTech(){
		List<DashboardTechProjection> techCount = recommendRepository.findCountPosisiForDashboardTech();
		return DefaultResponse.ok(techCount);
	}
	@PostMapping("/dashboardStatusPerMonth")
	public DefaultResponse<List<DashboardStatusPerMonth>> getStatusPerMonth(){
		List<DashboardStatusPerMonth> count = candstatrepository.findStatusPerMonth();
		return DefaultResponse.ok(count);
	}
}
