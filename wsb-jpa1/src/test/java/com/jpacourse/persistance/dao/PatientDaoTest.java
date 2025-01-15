package com.jpacourse.persistance.dao;

import com.jpacourse.persistence.dao.PatientDao;
import com.jpacourse.persistence.entity.PatientEntity;
import com.jpacourse.persistence.entity.VisitEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Test
    void findByAmountOfX_Should_FindPatientWithAmountOfVisitsHigherThanX() {
        // when
        List<PatientEntity> patientList = patientDao.findByAmountOfX(3L);

        // then;
        assertThat(patientList).isNotNull();
        assertThat(patientList.get(0).getVisits()).hasSize(3);

        VisitEntity visit1 = patientList.get(0).getVisits().get(0);
        VisitEntity visit2 = patientList.get(0).getVisits().get(1);
        VisitEntity visit3 = patientList.get(0).getVisits().get(2);
        assertThat(visit1.getDescription()).isEqualTo("Badanie wzroku");
        assertThat(visit1.getDoctor().getId()).isEqualTo(3L);
        assertThat(visit2.getDescription()).isEqualTo("Badanie krwi");
        assertThat(visit2.getDoctor().getId()).isEqualTo(2L);
        assertThat(visit3.getDescription()).isEqualTo("Badanie EKG");
        assertThat(visit3.getDoctor().getId()).isEqualTo(5L);
    }

    @Test
    void addVisitToPatient_Should_AddVisitToPatient() {
        // given
        LocalDateTime visitDate = LocalDateTime.now().plusDays(1);
        String description = "Routine check-up";

        // when
        patientDao.addVisitToPatient(1L, 1L, visitDate, description);

        // then
        PatientEntity patient = patientDao.findOne(1L);
        assertThat(patient).isNotNull();
        assertThat(patient.getVisits()).hasSize(2);

        VisitEntity visit = patient.getVisits().get(1);
        assertThat(visit.getDescription()).isEqualTo(description);
        assertThat(visit.getTime()).isEqualTo(visitDate);
        assertThat(visit.getDoctor().getId()).isEqualTo(1L);
    }

    @Test
    void findAllWeightLowerThan_Should_FindPatientsWithWeightLowerThan() {
        // when
        List<PatientEntity> patientList = patientDao.findAllWeightLowerThan(67.0);

        // then;
        assertThat(patientList.size()).isEqualTo(2);
        assertThat(patientList.get(0).getId()).isEqualTo(3L);
    }
}
