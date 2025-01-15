package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.dto.MedicalTreatmentTO;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.entity.MedicalTreatmentEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class PatientMapper {

    public static PatientTO mapToTO(final PatientEntity patientEntity) {
        if (patientEntity == null) {
            return null;
        }

        PatientTO patientTO = new PatientTO();
        patientTO.setId(patientEntity.getId());
        patientTO.setFirstName(patientEntity.getFirstName());
        patientTO.setLastName(patientEntity.getLastName());
        patientTO.setTelephoneNumber(patientEntity.getTelephoneNumber());
        patientTO.setEmail(patientEntity.getEmail());
        patientTO.setPatientNumber(patientEntity.getPatientNumber());
        patientTO.setDateOfBirth(patientEntity.getDateOfBirth());
        patientTO.setWeight(patientEntity.getWeight());

        // Mapping visits to VisitTO
        if (patientEntity.getVisits() != null) {
            List<VisitTO> visits = patientEntity.getVisits().stream()
                    .map(PatientMapper::mapVisitToTO)
                    .collect(Collectors.toList());
            patientTO.setVisits(visits);
        }

        return patientTO;
    }

    public static PatientEntity mapToEntity(final PatientTO patientTO) {
        if (patientTO == null) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(patientTO.getId());
        patientEntity.setFirstName(patientTO.getFirstName());
        patientEntity.setLastName(patientTO.getLastName());
        patientEntity.setTelephoneNumber(patientTO.getTelephoneNumber());
        patientEntity.setEmail(patientTO.getEmail());
        patientEntity.setPatientNumber(patientTO.getPatientNumber());
        patientEntity.setDateOfBirth(patientTO.getDateOfBirth());
        patientEntity.setWeight(patientTO.getWeight());

        return patientEntity;
    }

    private static VisitTO mapVisitToTO(VisitEntity visitEntity) {
        if (visitEntity == null) {
            return null;
        }

        VisitTO visitTO = new VisitTO();
        visitTO.setId(visitEntity.getId());
        visitTO.setTime(visitEntity.getTime());
        visitTO.setDoctorName(visitEntity.getDoctor().getFirstName() + " " + visitEntity.getDoctor().getLastName());

        // Map MedicalTreatmentEntity to MedicalTreatmentTO
        List<MedicalTreatmentTO> treatments = visitEntity.getMedicalTreatments().stream()
                .map(PatientMapper::mapMedicalTreatmentToTO)
                .collect(Collectors.toList());
        visitTO.setTreatments(treatments);

        return visitTO;
    }

    private static MedicalTreatmentTO mapMedicalTreatmentToTO(MedicalTreatmentEntity medicalTreatmentEntity) {
        if (medicalTreatmentEntity == null) {
            return null;
        }

        MedicalTreatmentTO medicalTreatmentTO = new MedicalTreatmentTO();
        medicalTreatmentTO.setId(medicalTreatmentEntity.getId());
        medicalTreatmentTO.setDescription(medicalTreatmentEntity.getDescription());
        medicalTreatmentTO.setTreatmentType(medicalTreatmentEntity.getType().toString());

        return medicalTreatmentTO;
    }
}
