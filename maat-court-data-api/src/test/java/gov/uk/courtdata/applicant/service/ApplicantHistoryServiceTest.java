package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.mapper.ApplicantHistoryMapper;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicantHistoryServiceTest {

    public static final int ID = 1;
    @Mock
    private ApplicantHistoryRepository applicantHistoryRepository;

    @Mock
    private ApplicantHistoryMapper applicantHistoryMapper;

    @InjectMocks
    private ApplicantHistoryService applicantHistoryService;

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnApplicantHistoryDTO() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.of(ApplicantHistoryEntity.builder().id(ID).build()));
        applicantHistoryService.find(ID);
        verify(applicantHistoryRepository, atLeastOnce()).findById(ID);
        verify(applicantHistoryMapper, atLeastOnce()).mapEntityToDTO(any());
    }

    @Test
    void givenApplicantHistoryNotFound_whenFindIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantHistoryService.find(ID);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant History not found for id ");
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.of(ApplicantHistoryEntity.builder().build()));
        applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(ID, "N"));
        verify(applicantHistoryRepository, atLeastOnce()).findById(any());
        verify(applicantHistoryRepository, atLeastOnce()).saveAndFlush(any());
        verify(applicantHistoryMapper, atLeastOnce()).mapEntityToDTO(any());
    }

    @Test
    void givenApplicantHistoryNotFound_whenUpdateIsInvoked_thenExceptionIsRaised() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(ID, "N"));
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant History not found for id");
    }

    @Test
    void givenAValidInput_whenCreateIsInvoked_thenCreateIsSuccess() {
        applicantHistoryService.create(ApplicantHistoryEntity.builder().id(1).build());
        verify(applicantHistoryRepository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidInput_whenDeleteIsInvoked_thenDeleteIsSuccess() {
        applicantHistoryService.delete(1);
        verify(applicantHistoryRepository, atLeastOnce()).deleteById(any());
    }

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnApplicantDTO() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.of(ApplicantHistoryEntity.builder().id(1).build()));
        applicantHistoryService.find(1);
        verify(applicantHistoryRepository, atLeastOnce()).findById(1);
    }


    @Test
    void givenAValidInput_whenApplicantHistoryUpdateIsInvoked_thenUpdateIsSuccess() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.of(ApplicantHistoryEntity.builder().id(1).build()));
        applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(1, "N"));
        verify(applicantHistoryRepository, atLeastOnce()).findById(any());
        verify(applicantHistoryRepository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidInputMap_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.of(ApplicantHistoryEntity.builder().id(1).build()));
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("email", "test@test.co");
        applicantHistoryService.update(1, inputMap);
        verify(applicantHistoryRepository, atLeastOnce()).findById(any());
        verify(applicantHistoryRepository, atLeastOnce()).save(any());
    }
}
