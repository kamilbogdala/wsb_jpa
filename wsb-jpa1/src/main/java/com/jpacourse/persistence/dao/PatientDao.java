package com.jpacourse.persistence.dao;

import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientDao extends Dao<PatientEntity, Long> {
    VisitEntity addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String visitDescription);
    List<PatientEntity> findByLastName(String lastName);
    List<PatientEntity> findByAmountOfX(Long visitCount);
    List<PatientEntity> finByWeight(Double weight);
}