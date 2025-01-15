package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistence.dao.AddressDao;
import com.jpacourse.persistence.dao.DoctorDao;
import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.dao.VisitDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        long doctorCountBefore = doctorDao.count();
        long visitCountBefore = visitDao.count();
        long patientCountBefore = patientDao.count();

        patientService.deleteById(1L);
        patientDao.getOne(1L);

        assertThat(patientService.findById(1L)).isNull();
        assertThat(visitDao.count()).isEqualTo(visitCountBefore - 1);
        assertThat(patientDao.count()).isEqualTo(patientCountBefore - 1);
        assertThat(doctorDao.count()).isEqualTo(doctorCountBefore);
    }

    @Test
    public void findById_Should_FindPatientById() {
        PatientTO found = patientService.findById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getFirstName()).isEqualTo("Kamil");
        assertThat(found.getLastName()).isEqualTo("Stoch");
        assertThat(found.getPatientNumber()).isEqualTo("P0001");

        assertThat(found.getVisits()).isNotNull();
        assertThat(found.getVisits()).hasSize(1);
        assertThat(found.getVisits().get(0).getDoctorName()).isEqualTo("Pablo Baginski");
        assertThat(found.getVisits().get(0).getTime()).isEqualTo(LocalDateTime.of(2024, 12, 1, 12, 45));
    }

    @Test
    public void findVisitsByPatientId_Should_FindVisitsByPatientId() {
        // when
        List<VisitTO> patientVisits = patientService.findVisitsByPatientId(3L);

        // then
        assertThat(patientVisits).isNotNull();
        assertThat(patientVisits).hasSize(3);
        assertThat(patientVisits.get(0).getTime()).isEqualTo(LocalDateTime.of(2024, 12, 3, 8, 30));
        assertThat(patientVisits.get(1).getTime()).isEqualTo(LocalDateTime.of(2025, 12, 1, 12, 45));
        assertThat(patientVisits.get(2).getTime()).isEqualTo(LocalDateTime.of(2025, 10, 1, 12, 45));

    }
}
