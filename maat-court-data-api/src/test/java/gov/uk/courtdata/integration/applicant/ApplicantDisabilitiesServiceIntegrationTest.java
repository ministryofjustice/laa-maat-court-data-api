package gov.uk.courtdata.integration.applicant;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import gov.uk.courtdata.applicant.repository.ApplicantDisabilitiesRepository;
import gov.uk.courtdata.applicant.service.ApplicantDisabilitiesService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.integration.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
@Slf4j
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class ApplicantDisabilitiesServiceIntegrationTest extends MockMvcIntegrationTest {
    private static final Integer INVALID_ID = 234;

    private static final int ID = 1;

    @Autowired
    private ApplicantDisabilitiesRepository applicantDisabilitiesRepository;

    @Autowired
    private ApplicantDisabilitiesService applicantDisabilitiesService;

    @AfterEach
    public void tearDown() {
        new RepositoryUtil().clearUp(applicantDisabilitiesRepository);
    }

    @Test
    void givenValidId_WhenGetApplicantDisabilitiesIsInvoked_thenCorrectResponseIsReturned() {
        createApplicantDisabilities();
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = applicantDisabilitiesService.find(ID);
        assertThat(applicantDisabilitiesDTO.getDisaDisability()).isNotBlank();
    }

    @Test
    void givenInValidId_WhenGetApplicantDisabilitiesIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantDisabilitiesService.find(INVALID_ID);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant Disability details not found for id");
    }

    @Test
    void givenAValidInput_whenCreateApplicantDisabilitiesIsInvoked_thenCreateIsSuccess() {
        ApplicantDisabilitiesDTO recordToCreate = TestModelDataBuilder.getApplicantDisabilitiesDTO(11);
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = applicantDisabilitiesService
                .create(recordToCreate);
        assertThat(applicantDisabilitiesDTO.getId()).isEqualTo(recordToCreate.getId());
        assertThat(applicantDisabilitiesDTO.getDisaDisability()).isEqualTo(recordToCreate.getDisaDisability());
        assertThat(applicantDisabilitiesDTO.getApplId()).isEqualTo(recordToCreate.getApplId());
    }

    @Test
    void givenAValidInput_whenUpdateApplicantDisabilitiesIsInvoked_thenUpdateIsSuccess() {
        createApplicantDisabilities();
        ApplicantDisabilitiesDTO recordToUpdate = TestModelDataBuilder.getApplicantDisabilitiesDTO(ID);
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = applicantDisabilitiesService
                .update(recordToUpdate);
        assertThat(applicantDisabilitiesDTO.getId()).isEqualTo(recordToUpdate.getId());
        assertThat(applicantDisabilitiesDTO.getDisaDisability()).isEqualTo(recordToUpdate.getDisaDisability());
        assertThat(applicantDisabilitiesDTO.getApplId()).isEqualTo(recordToUpdate.getApplId());
    }

    @Test
    void givenAInValidInput_whenUpdateRepOrderApplicantLinksIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantDisabilitiesService.update(TestModelDataBuilder.getApplicantDisabilitiesDTO(INVALID_ID));
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant Disability details not found for id");
    }

    @Test
    void givenAValidInput_whenDeleteApplicantDisabilitiesIsInvoked_thenDeleteIsSuccess() {
        createApplicantDisabilities();
        applicantDisabilitiesService.delete(ID);
        Optional<ApplicantDisabilitiesEntity> record = applicantDisabilitiesRepository.findById(ID);
        assertThatThrownBy(() -> {
            record.get();
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("No value present");

    }

    private ApplicantDisabilitiesEntity createApplicantDisabilities() {
        return applicantDisabilitiesRepository.save(TestEntityDataBuilder.getApplicantDisabilitiesEntity(ID));
    }

}
