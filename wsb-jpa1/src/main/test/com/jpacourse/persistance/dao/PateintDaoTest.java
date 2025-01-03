package com.jpacourse.persistance.dao;

import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class PatientDaoImplTest {

    @Autowired
    private EntityManager entityManager;

    private PatientDaoImpl patientDao;

    @BeforeEach
    void setUp() {
        patientDao = new PatientDaoImpl();
        patientDao.setEntityManager(entityManager);

        // Dodanie testowego pacjenta
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Jan");
        patient.setLastName("Duda");
        patient.setTelephoneNumber("123456789");
        patient.setEmail("jaduda@gamil.com");
        patient.setPatientNumber("P0004");
        patient.setDateOfBirth(LocalDateTime.now().minusYears(30).toLocalDate());
        patient.setWeight(70.0);
        entityManager.persist(patient);

        // Dodanie testowego doktora
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Marek");
        doctor.setLastName("Kowalczyk");
        doctor.setTelephoneNumber("987654321");
        doctor.setEmail("mkowalczyk@gmail.com");
        doctor.setDoctorNumber("D123");
        doctor.setSpecialization(Specialization.GENERAL);
        entityManager.persist(doctor);

        entityManager.flush();
    }

    @Test
    void shouldAddVisitToPatient() {
        // Arrange: przygotowanie danych
        Long patientId = 1L; // ID testowego pacjenta
        Long doctorId = 1L; // ID testowego doktora
        LocalDateTime visitDate = LocalDateTime.now().plusDays(1);
        String description = "Routine check-up";

        // Act: wykonanie metody
        patientDao.addVisitToPatient(patientId, doctorId, visitDate, description);

        // Assert: sprawdzenie efektów
        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        assertThat(patient).isNotNull();
        assertThat(patient.getVisits()).hasSize(1);

        VisitEntity visit = patient.getVisits().get(0);
        assertThat(visit.getDescription()).isEqualTo(description);
        assertThat(visit.getTime()).isEqualTo(visitDate);
        assertThat(visit.getDoctor().getId()).isEqualTo(doctorId);
    }
}
