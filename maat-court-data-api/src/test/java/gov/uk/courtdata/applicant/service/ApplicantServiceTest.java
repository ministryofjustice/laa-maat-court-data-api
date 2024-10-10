package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.SendToCCLFDTO;
import gov.uk.courtdata.applicant.repository.ApplicantRepository;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.service.RepOrderService;
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
    @MockBean
    private RepOrderService repOrderService;
    @MockBean
    private ApplicantHistoryService applicantHistoryService;

    @BeforeEach
    void setUp() {
        applicantService = new ApplicantService(applicantRepository, repOrderService, applicantHistoryService);
    }

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnApplicantDTO() {
        when(applicantRepository.findById(anyInt())).thenReturn(Optional.of(Applicant.builder().id(ID).build()));
        applicantService.find(ID);
        verify(applicantRepository, atLeastOnce()).findById(ID);
    }

    @Test
    void givenApplicantNotFound_whenFindIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> applicantService.find(ID)).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant not found for id ");
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(applicantRepository.findById(anyInt())).thenReturn(Optional.of(Applicant.builder().id(ID).build()));
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("email", "test@test.co");
        applicantService.update(ID, inputMap);
        verify(applicantRepository, atLeastOnce()).findById(any());
        verify(applicantRepository, atLeastOnce()).save(any());
    }


    @Test
    void givenAValidInput_whenCreateIsInvoked_thenCreateIsSuccess() {
        applicantService.create(Applicant.builder().build());
        verify(applicantRepository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidInput_whenDeleteIsInvoked_thenDeleteIsSuccess() {
        applicantService.delete(ID);
        verify(applicantRepository, atLeastOnce()).deleteById(any());
    }

    @Test
    void givenAValidInput_whenUpdateSendToCCLFIsInvoked_thenUpdateIsSuccess() {
        doNothing().when(repOrderService).update(any(), any());
        doNothing().when(applicantHistoryService).update(any(), any());
        when(applicantRepository.findById(anyInt())).thenReturn(Optional.of(Applicant.builder().id(ID).build()));
        SendToCCLFDTO sendToCCLFDTO = SendToCCLFDTO.builder().applId(ID).repId(ID).applHistoryId(ID).build();
        applicantService.updateSendToCCLF(sendToCCLFDTO);
        verify(repOrderService, atLeastOnce()).update(any(), any());
        verify(applicantHistoryService, atLeastOnce()).update(any(), any());
        verify(applicantRepository, atLeastOnce()).findById(any());
        verify(applicantRepository, atLeastOnce()).save(any());
    }
}
