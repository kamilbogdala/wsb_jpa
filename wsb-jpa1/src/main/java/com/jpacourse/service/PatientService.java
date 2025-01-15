package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;

import java.util.List;

public interface PatientService {
    PatientTO findById(Long id);
    void deleteById(Long id);
    List<VisitTO> findVisitsByPatientId(Long id);
}
