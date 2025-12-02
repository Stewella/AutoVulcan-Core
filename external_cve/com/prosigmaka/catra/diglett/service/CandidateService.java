package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.dto.CandidateDto;
import com.prosigmaka.catra.diglett.model.entity.CandidateStatusHistory;


public interface CandidateService {
    CandidateDto insertCandidate(CandidateDto dto);
    CandidateDto saveKode(String id);
    CandidateStatusHistory saveHistory(String id, String avail, String availLama);
}
