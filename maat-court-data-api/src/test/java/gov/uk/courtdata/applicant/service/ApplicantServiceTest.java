package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.repository.ApplicantRepository;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
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


    @BeforeEach
    void setUp() {
        applicantService = new ApplicantService(applicantRepository);
    }

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnApplicantDTO() {
        when(applicantRepository.findById(anyInt())).thenReturn(Optional.of(Applicant.builder().id(1).build()));
        applicantService.find(1);
        verify(applicantRepository, atLeastOnce()).findById(1);
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
        when(applicantRepository.findById(anyInt())).thenReturn(Optional.of(Applicant.builder().id(1).build()));
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("email", "test@test.co");
        applicantService.update(1, inputMap);
        verify(applicantRepository, atLeastOnce()).findById(any());
        verify(applicantRepository, atLeastOnce()).save(any());
    }


    @Test
    void givenAValidInput_whenCreateIsInvoked_thenCreateIsSuccess() {
        applicantService.create(Applicant.builder().id(1).build());
        verify(applicantRepository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidInput_whenDeleteIsInvoked_thenDeleteIsSuccess() {
        applicantService.delete(1);
        verify(applicantRepository, atLeastOnce()).deleteById(any());
    }
}
