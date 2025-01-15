package com.jpacourse.service;

import com.jpacourse.dto.AddressTO;
import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
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
import org.junit.Test;
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

    @Test
    public void deleteById_Should_DeletePatientAndHisVisitsButWithoutDoctors() {
        AddressEntity savedAddress = createAndSaveAddress();
        DoctorEntity doctor = createAndSaveDoctor(savedAddress);
        PatientTO savedPatientTO = createAndSavePatient(savedAddress);
        createAndSaveVisit(doctor, savedPatientTO);

        long doctorCountBefore = doctorDao.count();
        long visitCountBefore = visitDao.count();
        long patientCountBefore = patientDao.count();

        patientService.deleteById(savedPatientTO.getId());

        assertThat(patientService.findById(savedPatientTO.getId())).isNull();
        assertThat(visitDao.count()).isEqualTo(visitCountBefore - 1);
        assertThat(patientDao.count()).isEqualTo(patientCountBefore - 1);
        assertThat(doctorDao.count()).isEqualTo(doctorCountBefore);
    }

    @Test
    public void findById_Should_FindPatientById() {
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
        assertThat(found.getDateOfBirth()).isEqualTo(savedPatientTO.getDateOfBirth());

        assertThat(found.getVisits()).isNotNull();
        assertThat(found.getVisits()).hasSize(1);
        assertThat(found.getVisits().get(0).getDoctorName()).isEqualTo("Marek Kowalczyk");
        assertThat(found.getVisits().get(0).getTime()).isEqualTo(LocalDateTime.of(2025, 5, 20, 15, 30));
    }

    @Test
    public void findVisitsByPatientId_Should_FindVisitsByPatientId() {
        // given
        AddressEntity savedAddress = createAndSaveAddress();
        DoctorEntity doctor = createAndSaveDoctor(savedAddress);
        PatientTO savedPatientTO = createAndSavePatient(savedAddress);
        createAndSaveVisits(doctor, savedPatientTO);

        // when
        List<VisitTO> patientVisits = patientService.findVisitsByPatientId(savedPatientTO.getId());

        // then
        assertThat(patientVisits).isNotNull();
        assertThat(patientVisits).hasSize(2);
        assertThat(patientVisits.get(0).getTime()).isEqualTo(LocalDateTime.of(2025, 4, 20, 15, 30));
        assertThat(patientVisits.get(1).getTime()).isEqualTo(LocalDateTime.of(2026, 2, 20, 15, 30));
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
        patientEntity.setWeight(77.0);
        patientEntity.setAddress(address);
        return PatientMapper.mapToTO(patientDao.save(patientEntity));
    }

    private void createAndSaveVisit(DoctorEntity doctor, PatientTO patient) {
        VisitEntity visit = new VisitEntity();
        visit.setDoctor(doctor);
        visit.setPatient(PatientMapper.mapToEntity(patient));
        visit.setDescription("Badanie USG");
        visit.setTime(LocalDateTime.of(2025, 5, 20, 15, 30));
        visitDao.save(visit);
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
