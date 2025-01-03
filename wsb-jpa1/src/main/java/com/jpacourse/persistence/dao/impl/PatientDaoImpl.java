package com.jpacourse.persistence.dao.impl;

import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitDate, String description) {
        // Find patient and doctor
        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
        }

        // Create new visit
        VisitEntity visit = new VisitEntity();
        visit.setDescription(description);
        visit.setTime(visitDate);
        visit.setDoctor(doctor);
        visit.setPatient(patient);

        // Add visit to patient's visits list
        patient.getVisits().add(visit);

        // Merge updated patient with cascading save for visit
        entityManager.merge(patient);
    }
}
