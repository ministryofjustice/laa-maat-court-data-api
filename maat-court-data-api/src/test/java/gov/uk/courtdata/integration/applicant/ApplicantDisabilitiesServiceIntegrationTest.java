package gov.uk.courtdata.integration.applicant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import gov.uk.courtdata.applicant.service.ApplicantDisabilitiesService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@Slf4j
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class ApplicantDisabilitiesServiceIntegrationTest extends MockMvcIntegrationTest {
    private static final Integer INVALID_ID = 2345;

    @Autowired
    private ApplicantDisabilitiesService applicantDisabilitiesService;

    @Test
    void givenValidId_WhenGetApplicantDisabilitiesIsInvoked_thenCorrectResponseIsReturned() {
        createApplicantDisabilities();
        Integer id = repos.applicantDisabilities.findAll().get(0).getId();
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = applicantDisabilitiesService.find(id);
        assertThat(applicantDisabilitiesDTO.getDisaDisability()).isNotBlank();
    }

    @Test
    void givenInValidId_WhenGetApplicantDisabilitiesIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> applicantDisabilitiesService.find(INVALID_ID))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant Disability details not found for id");
    }

    @Test
    void givenAValidInput_whenCreateApplicantDisabilitiesIsInvoked_thenCreateIsSuccess() {
        ApplicantDisabilitiesDTO recordToCreate = TestModelDataBuilder.getApplicantDisabilitiesDTO();
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = applicantDisabilitiesService
                .create(recordToCreate);
        assertThat(applicantDisabilitiesDTO.getId()).isGreaterThan(0);
        assertThat(applicantDisabilitiesDTO.getDisaDisability()).isEqualTo(recordToCreate.getDisaDisability());
        assertThat(applicantDisabilitiesDTO.getApplId()).isEqualTo(recordToCreate.getApplId());
    }

    @Test
    void givenAValidInput_whenUpdateApplicantDisabilitiesIsInvoked_thenUpdateIsSuccess() {
        createApplicantDisabilities();
        Integer id = repos.applicantDisabilities.findAll().get(0).getId();
        ApplicantDisabilitiesDTO recordToUpdate = TestModelDataBuilder.getApplicantDisabilitiesDTO(id);
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = applicantDisabilitiesService
                .update(recordToUpdate);
        assertThat(applicantDisabilitiesDTO.getId()).isEqualTo(recordToUpdate.getId());
        assertThat(applicantDisabilitiesDTO.getDisaDisability()).isEqualTo(recordToUpdate.getDisaDisability());
        assertThat(applicantDisabilitiesDTO.getApplId()).isEqualTo(recordToUpdate.getApplId());
    }

    @Test
    void givenAInValidInput_whenUpdateApplicantDisabilitiesIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> applicantDisabilitiesService
                .update(TestModelDataBuilder.getApplicantDisabilitiesDTO(INVALID_ID)))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant Disability details not found for id");
    }

    @Test
    void givenAValidInput_whenDeleteApplicantDisabilitiesIsInvoked_thenDeleteIsSuccess() {
        createApplicantDisabilities();
        Integer id = repos.applicantDisabilities.findAll().get(0).getId();
        applicantDisabilitiesService.delete(id);
        Optional<ApplicantDisabilitiesEntity> record = repos.applicantDisabilities.findById(id);
        assertThatThrownBy(record::get).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");

    }

    private void createApplicantDisabilities() {
        repos.applicantDisabilities.save(TestEntityDataBuilder.getApplicantDisabilitiesEntity());
    }

}
