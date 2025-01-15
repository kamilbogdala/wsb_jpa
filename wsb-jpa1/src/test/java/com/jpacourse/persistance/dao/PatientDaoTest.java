package com.jpacourse.persistance.dao;

import com.jpacourse.dto.AddressTO;
import com.jpacourse.dto.PatientTO;
import com.jpacourse.mapper.AddressMapper;
import com.jpacourse.mapper.PatientMapper;
import com.jpacourse.persistence.dao.AddressDao;
import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.dao.VisitDao;
import com.jpacourse.persistence.entity.AddressEntity;
import com.jpacourse.persistence.entity.DoctorEntity;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import com.jpacourse.service.PatientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private VisitDao visitDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private PatientService patientService;

    private AddressEntity savedAddress;
    private DoctorEntity savedDoctor;
    private PatientTO savedPatient;

    @BeforeEach
    void setUp() {
        savedAddress = createAndSaveAddress();
        savedDoctor = createAndSaveDoctor(savedAddress);
        savedPatient = createAndSavePatient(savedAddress);
    }

    @AfterEach
    void clearOut() {
        patientService.deleteById(savedPatient.getId());
    }

    @Test
    void addVisitToPatient_Should_AddVisitToPatient() {
        // given
        LocalDateTime visitDate = LocalDateTime.now().plusDays(1);
        String description = "Routine check-up";

        // when
        patientDao.addVisitToPatient(savedPatient.getId(), savedDoctor.getId(), visitDate, description);

        // then
        PatientEntity patient = patientDao.findOne(savedPatient.getId());
        assertThat(patient).isNotNull();
        assertThat(patient.getVisits()).hasSize(1);

        VisitEntity visit = patient.getVisits().get(0);
        assertThat(visit.getDescription()).isEqualTo(description);
        assertThat(visit.getTime()).isEqualTo(visitDate);
        assertThat(visit.getDoctor().getId()).isEqualTo(savedDoctor.getId());
    }

    @Test
    void findByAmountOfX_Should_FindPatientWithAmountOfVisitsHigherThanX() {
        // given
        createAndSaveVisits(savedDoctor, savedPatient);

        // when
        List<PatientEntity> patientList = patientDao.findByAmountOfX(2L);

        // then;
        assertThat(patientList).isNotNull();
        assertThat(patientList.get(0).getVisits()).hasSize(2);

        VisitEntity visit = patientList.get(0).getVisits().get(0);
        assertThat(visit.getDescription()).isEqualTo("Badanie krwi");
        assertThat(visit.getDoctor().getId()).isEqualTo(savedDoctor.getId());
    }

    @Test
    void findAllWeightLowerThan_Should_FindPatientsWithWeightLowerThan() {
        // when
        List<PatientEntity> patientList = patientDao.findAllWeightLowerThan(8.0);

        // then;
        assertThat(patientList.size()).isGreaterThan(0);
    }

    private AddressEntity createAndSaveAddress() {
        AddressTO addressTO = new AddressTO();
        addressTO.setCity("Wielun");
        addressTO.setAddressLine1("ul. Wodna 1");
        addressTO.setPostalCode("98-300");
        AddressEntity addressEntity = AddressMapper.mapToEntity(addressTO);
        return addressDao.save(addressEntity);
    }

    private DoctorEntity createAndSaveDoctor(AddressEntity address) {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Marek");
        doctor.setLastName("Kowalczyk");
        doctor.setTelephoneNumber("888555444");
        doctor.setEmail("marek.kowalczyk@gmail.com");
        doctor.setDoctorNumber("D0040");
        doctor.setSpecialization(Specialization.DERMATOLOGIST);
        doctor.setAddress(address);
        return doctorDao.save(doctor);
    }

    private PatientTO createAndSavePatient(AddressEntity address) {
        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setFirstName("Dawid");
        patientEntity.setLastName("Kubacki");
        patientEntity.setTelephoneNumber("112233445");
        patientEntity.setEmail("dkubacki@gmail.com");
        patientEntity.setPatientNumber("P0004");
        patientEntity.setDateOfBirth(LocalDate.of(1990, 3, 15));
        patientEntity.setWeight(8.0);
        patientEntity.setAddress(address);
        return PatientMapper.mapToTO(patientDao.save(patientEntity));
    }

    private void createAndSaveVisits(DoctorEntity doctor, PatientTO patient) {
        VisitEntity visit = new VisitEntity();
        visit.setDoctor(doctor);
        visit.setPatient(PatientMapper.mapToEntity(patient));
        visit.setDescription("Badanie krwi");
        visit.setTime(LocalDateTime.of(2025, 4, 20, 15, 30));
        visitDao.save(visit);

        VisitEntity visit2 = new VisitEntity();
        visit2.setDoctor(doctor);
        visit2.setPatient(PatientMapper.mapToEntity(patient));
        visit2.setDescription("Badanie EKG");
        visit2.setTime(LocalDateTime.of(2026, 2, 10, 15, 30));
        visitDao.save(visit2);
    }
}
