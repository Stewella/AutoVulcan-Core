package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.dto.StepDto;
import com.prosigmaka.catra.diglett.model.entity.Step;

public interface StepService {
	Step insertStep(StepDto dto);
}
