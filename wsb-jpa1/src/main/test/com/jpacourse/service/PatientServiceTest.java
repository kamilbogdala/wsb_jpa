package com.jpacourse.service;

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
import com.jpacourse.persistence.entity.VisitEntity;
import com.jpacourse.persistence.enums.Specialization;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private VisitDao visitDao;

    @Autowired
    private AddressDao addressDao;

    @Before
    public void setUp() {
        clearDatabase();
    }

    private void clearDatabase() {
        visitDao.deleteAll();
        patientDao.deleteAll();
        doctorDao.deleteAll();
        addressDao.deleteAll();
    }

    @Test
    public void testShouldDeletePatientAndHisVisitsButWithoutDoctors() {
        AddressEntity savedAddress = createAndSaveAddress();
        DoctorEntity doctor = createAndSaveDoctor(savedAddress);
        PatientTO savedPatientTO = createAndSavePatient(savedAddress);
        createAndSaveVisit(doctor, savedPatientTO);

        long doctorCountBefore = doctorDao.count();
        long visitCountBefore = visitDao.count();
        long patientCountBefore = patientDao.count();

        System.out.println(patientDao.findAll());
        patientService.deletePatient(savedPatientTO.getId());

        assertThat(patientDao.findOne(savedPatientTO.getId())).isNull();
        assertThat(visitDao.count()).isEqualTo(visitCountBefore - 1);
        assertThat(patientDao.count()).isEqualTo(patientCountBefore - 1);
        assertThat(doctorDao.count()).isEqualTo(doctorCountBefore);
    }

    @Test
    public void testShouldFindPatientById() {
        AddressEntity savedAddress = createAndSaveAddress();
        DoctorEntity doctor = createAndSaveDoctor(savedAddress);
        PatientTO savedPatientTO = createAndSavePatient(savedAddress);
        createAndSaveVisit(doctor, savedPatientTO);

        PatientTO found = patientService.findById(savedPatientTO.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(savedPatientTO.getId());
        assertThat(found.getFirstName()).isEqualTo(savedPatientTO.getFirstName());
        assertThat(found.getLastName()).isEqualTo(savedPatientTO.getLastName());
        assertThat(found.getPatientNumber()).isEqualTo(savedPatientTO.getPatientNumber());
        assertThat(found.getAge()).isEqualTo(savedPatientTO.getAge());

        assertThat(found.getVisits()).isNotNull();
        assertThat(found.getVisits()).hasSize(1);
        assertThat(found.getVisits().get(0).getDoctorFirstName()).isEqualTo("Marek");
        assertThat(found.getVisits().get(0).getDoctorLastName()).isEqualTo("Kowalczyk");
        assertThat(found.getVisits().get(0).getTime()).isEqualTo(LocalDateTime.of(2024, 5, 20, 15, 30));
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
        doctor.setSpecialization(Specialization.CARDIOLOGIST);
        doctor.setAddress(address);
        return doctorDao.save(doctor);
    }

    private PatientTO createAndSavePatient(AddressEntity address) {
        AddressTO addressTO = AddressMapper.mapToTO(address);
        PatientTO patientTO = new PatientTO();
        patientTO.setFirstName("Dawid");
        patientTO.setLastName("Kubacki");
        patientTO.setTelephoneNumber("112233445");
        patientTO.setEmail("dkubacki@gmail.com");
        patientTO.setPatientNumber("P0004");
        patientTO.setDateOfBirth(LocalDate.of(1990, 3, 15));
        patientTO.Weight(77);
        patientTO.setAddress(addressTO);
        return patientService.savePatient(patientTO);
    }

    private void createAndSaveVisit(DoctorEntity doctor, PatientTO patient) {
        VisitEntity visit = new VisitEntity();
        visit.setDoctor(doctor);
        visit.setPatient(PatientMapper.mapToEntity(patient));
        visit.setDescription("Badanie USG");
        visit.setTime(LocalDateTime.of(2024, 5, 20, 15, 30));
        visitDao.save(visit);
    }
}
