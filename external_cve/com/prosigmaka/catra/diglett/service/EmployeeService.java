package com.prosigmaka.catra.diglett.service;

import com.prosigmaka.catra.diglett.model.entity.Candidate;
import com.prosigmaka.catra.diglett.model.entity.Employee;

public interface EmployeeService {
    Candidate insertData(Candidate entity, Integer isDelete);
}
