package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.ApplicantDTO;
import gov.uk.courtdata.applicant.mapper.ApplicantMapper;
import gov.uk.courtdata.applicant.repository.ApplicantRepository;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ApplicantService.class)
public class ApplicantServiceTest {

    private static final int ID = 1;
    @MockBean
    private ApplicantRepository applicantRepository;
    private ApplicantService applicantService;
    @MockBean
    private ApplicantMapper applicantMapper;

    @BeforeEach
    void setUp() {
        applicantService = new ApplicantService(applicantRepository, applicantMapper);
    }

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnApplicantDTO() {
        when(applicantRepository.findById(anyInt())).thenReturn(Optional.of(Applicant.builder().id(1).build()));
        applicantService.find(1);
        verify(applicantRepository, atLeastOnce()).findById(1);
        verify(applicantMapper, atLeastOnce()).mapEntityToDTO(any());
    }

    @Test
    void givenApplicantNotFound_whenFindIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantService.find(1);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant not found for id ");
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(applicantRepository.getById(anyInt())).thenReturn(TestModelDataBuilder.getApplicant());
        applicantService.update(1, Applicant.builder().email("test@test.com").build());
        verify(applicantRepository, atLeastOnce()).getById(any());
        verify(applicantRepository, atLeastOnce()).save(any());
    }


    @Test
    void givenAValidInput_whenCreateIsInvoked_thenCreateIsSuccess() {
        applicantService.create(ApplicantDTO.builder().id(1).build());
        verify(applicantRepository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidInput_whenDeleteIsInvoked_thenDeleteIsSuccess() {
        applicantService.delete(1);
        verify(applicantRepository, atLeastOnce()).deleteById(any());
    }
}
