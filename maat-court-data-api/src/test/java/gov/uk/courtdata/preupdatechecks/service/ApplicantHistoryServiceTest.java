package gov.uk.courtdata.preupdatechecks.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.preupdatechecks.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.preupdatechecks.mapper.ApplicantHistoryMapper;
import gov.uk.courtdata.preupdatechecks.repository.ApplicantHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicantHistoryServiceTest {

    @Mock
    private ApplicantHistoryRepository applicantHistoryRepository;

    @Mock
    private ApplicantHistoryMapper applicantHistoryMapper;

    @InjectMocks
    private ApplicantHistoryService applicantHistoryService;

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.of(ApplicantHistoryEntity.builder().build()));
        applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO());
        verify(applicantHistoryRepository, atLeastOnce()).findById(any());
        verify(applicantHistoryRepository, atLeastOnce()).saveAndFlush(any());
        verify(applicantHistoryMapper, atLeastOnce()).mapEntityToDTO(any());
    }

    @Test
    void givenApplicantHistoryEntryDoesntExist_whenFindByIdIsInvoked_thenExceptionIsRaised() {
        when(applicantHistoryRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO());
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant History not found for id");
    }

}
