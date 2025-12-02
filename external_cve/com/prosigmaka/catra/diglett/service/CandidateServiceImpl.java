package com.prosigmaka.catra.diglett.service;

import java.sql.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prosigmaka.catra.diglett.assembler.CandidateAssembler;
import com.prosigmaka.catra.diglett.model.dto.CandidateDto;
import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.entity.CandidateStatusHistory;
import com.prosigmaka.catra.diglett.model.enummodel.AvailKandidatEnum;
import com.prosigmaka.catra.diglett.repository.CandidateRepository;
import com.prosigmaka.catra.diglett.repository.CandidateStatusHistoryRepository;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {
	private final CandidateRepository candidateRepository;
	private final CandidateStatusHistoryRepository candidateStatusHistoryRepository;
	private final CandidateAssembler candidateAssembler;


	public CandidateServiceImpl(CandidateRepository candidateRepository, CandidateStatusHistoryRepository candidateStatusHistoryRepository, CandidateAssembler candidateAssembler) {
		this.candidateRepository = candidateRepository;
		this.candidateStatusHistoryRepository = candidateStatusHistoryRepository;
		this.candidateAssembler = candidateAssembler;
	}

	@Override
	public CandidateDto insertCandidate(CandidateDto dto) {
		if (dto.getId() == null || dto.getId().isEmpty()) {
			Candidate entity = candidateRepository.save(candidateAssembler.fromDto(dto));
			entity.setIsDelete(0);
			saveKode(entity.getId());
			dto.setAvail(AvailKandidatEnum.AVAILABLE.getValue());
			saveHistory(entity.getId(), dto.getAvail(), entity.getAvailKandidat());
			System.out.println();
			return candidateAssembler.fromEntity(entity);
		} else {
			Candidate entitylama = candidateRepository.findById(dto.getId()).get();
			if (!entitylama.getAvailKandidat().equalsIgnoreCase(dto.getAvail())) {
				Candidate entity = candidateRepository.save(candidateAssembler.fromDto(dto));
				candidateRepository.save(entity);
				saveHistory(entitylama.getId(), dto.getAvail(), entitylama.getAvailKandidat());
				return candidateAssembler.fromEntity(entity);
			}
			Candidate entity = candidateRepository.save(candidateAssembler.fromDto(dto));
			candidateRepository.save(entity);
			return candidateAssembler.fromEntity(entity);
		}
	}

	@Override
	public CandidateDto saveKode(String id) {
		Candidate entity = candidateRepository.findById(id).get();
		String kode = entity.getId();
		entity.setCndkode(kode);
		candidateRepository.save(entity);
		return candidateAssembler.fromEntity(entity);
	}

	@Override
	public CandidateStatusHistory saveHistory(String id, String avail, String availLama) {
		Candidate entity = candidateRepository.findById(id).get();
//		if (availLama == null) {
//			CandidateStatusHistory cndhist = new CandidateStatusHistory();
//			cndhist.setCndid(entity.getId());
//			cndhist.setAvail(AvailKandidatEnum.AVAILABLE.getValue());
//			cndhist.setTanggalProses(entity.getTanggalProses());
//			candidateStatusHistoryRepository.save(cndhist);
//			return cndhist;
//		}
		CandidateStatusHistory cndhist = new CandidateStatusHistory();
		long millis = System.currentTimeMillis();
		Date date = new Date(millis);
		cndhist.setCndid(entity.getId());
		cndhist.setAvail(avail);
		cndhist.setTanggalProses(date);
		candidateStatusHistoryRepository.save(cndhist);
		return cndhist;

	}
}
