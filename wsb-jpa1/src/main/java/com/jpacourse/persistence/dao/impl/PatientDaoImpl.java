package com.jpacourse.persistence.dao.impl;

import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {

    @Autowired
    private DoctorDao doctorDao;

    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitDate, String description) {
        // Find patient and doctor
        PatientEntity patient = findOne(patientId);
        DoctorEntity doctor = doctorDao.findOne(doctorId);

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

    @Override
    public List<PatientEntity> findByLastName(String lastName) {
        return entityManager.createQuery(
                        "SELECT patie FROM PatientEntity patie WHERE patie.lastName = :lastName",
                        PatientEntity.class
                ).setParameter("lastName", lastName)
                .getResultList();
    }

    @Override
    public List<PatientEntity> findByAmountOfX(Long visitCount) {
        return entityManager.createQuery(
                        "SELECT patie FROM PatientEntity patie " +
                                " JOIN patie.visits vis " +
                                "GROUP BY patie " +
                                "HAVING count(vis) >= :visitCount",
                        PatientEntity.class
                ).setParameter("visitCount", visitCount)
                .getResultList();
    }

    @Override
    public List<PatientEntity> findAllWeightLowerThan(Double weight) {
        return entityManager.createQuery(
                        "SELECT patie FROM PatientEntity patie " +
                                "WHERE patie.weight <= :weight",
                        PatientEntity.class
                ).setParameter("weight", weight)
                .getResultList();
    }

    @Override
    public List<PatientEntity> finByWeight(Double weight) {
        return entityManager.createQuery(
                        "SELECT patie FROM PatientEntity patie WHERE patie.weight > :weight",
                        PatientEntity.class
                ).setParameter("weight", weight)
                .getResultList();
    }
}
