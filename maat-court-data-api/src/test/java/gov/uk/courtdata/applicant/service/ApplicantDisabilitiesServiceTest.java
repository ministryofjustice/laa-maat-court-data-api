package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import gov.uk.courtdata.applicant.mapper.ApplicantDisabilitiesMapper;
import gov.uk.courtdata.applicant.repository.ApplicantDisabilitiesRepository;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
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
public class ApplicantDisabilitiesServiceTest {

    public static final int ID = 1;
    @Mock
    private ApplicantDisabilitiesRepository applicantDisabilitiesRepository;

    @Mock
    private ApplicantDisabilitiesMapper applicantDisabilitiesMapper;

    @InjectMocks
    private ApplicantDisabilitiesService applicantDisabilitiesService;

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnApplicantHistoryDTO() {
        when(applicantDisabilitiesRepository.findById(anyInt())).thenReturn(Optional.ofNullable(ApplicantDisabilitiesEntity.builder().id(ID).build()));
        applicantDisabilitiesService.find(ID);
        verify(applicantDisabilitiesRepository, atLeastOnce()).findById(ID);
        verify(applicantDisabilitiesMapper, atLeastOnce()).mapEntityToDTO(any());
    }

    @Test
    void givenApplicantDisabilitiesNotFound_whenFindIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> applicantDisabilitiesService
                .find(ID))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant Disability details not found for id ");
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(applicantDisabilitiesRepository.findById(any()))
                .thenReturn(Optional.of(ApplicantDisabilitiesEntity.builder().id(ID).build()));
        applicantDisabilitiesService.update(TestModelDataBuilder.getApplicantDisabilitiesDTO());
        verify(applicantDisabilitiesRepository, atLeastOnce()).findById(any());
        verify(applicantDisabilitiesRepository, atLeastOnce()).save(any());
        verify(applicantDisabilitiesMapper, atLeastOnce()).mapEntityToDTO(any());
        verify(applicantDisabilitiesMapper, atLeastOnce()).updateApplicantDisabilitiesDTOToApplicantDisabilitiesEntity(any(), any());
    }

    @Test
    void givenApplicantDisabilitiesNotFound_whenUpdateIsInvoked_thenExceptionIsRaised() {
        when(applicantDisabilitiesRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> applicantDisabilitiesService
                .update(TestModelDataBuilder.getApplicantDisabilitiesDTO()))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant Disability details not found for id");
    }

    @Test
    void givenAValidInput_whenCreateIsInvoked_thenCreateIsSuccess() {
        applicantDisabilitiesService.create(TestModelDataBuilder.getApplicantDisabilitiesDTO());
        verify(applicantDisabilitiesMapper, atLeastOnce()).mapEntityToDTO(any());
        verify(applicantDisabilitiesRepository, atLeastOnce()).save(any());
    }

    @Test
    void givenAValidInput_whenDeleteIsInvoked_thenDeleteIsSuccess() {
        when(applicantDisabilitiesRepository.findById(any()))
                .thenReturn(Optional.of(ApplicantDisabilitiesEntity.builder().id(ID).build()));
        applicantDisabilitiesService.delete(ID);
        verify(applicantDisabilitiesRepository, atLeastOnce()).findById(any());
    }

    @Test
    void givenApplicantDisabilitiesNotFound_whenDeleteIsInvoked_thenExceptionIsRaised() {
        when(applicantDisabilitiesRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> applicantDisabilitiesService.delete(ID)).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant Disability details not found for id");
    }

}