package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepOrderApplicantLinksServiceTest {

    public static final int ID = 1;
    @Mock
    private RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;

    @Mock
    private RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;

    @InjectMocks
    private RepOrderApplicantLinksService repOrderApplicantLinksService;

    @Test
    void givenAValidInput_whenFindIsInvoked_thenShouldReturnListOfRepOrderApplicantLinksDTO() {
        when(repOrderApplicantLinksRepository.findAllByRepId(anyInt())).thenReturn(List.of(RepOrderApplicantLinksEntity.builder().repId(TestModelDataBuilder.REP_ID).build()));
        repOrderApplicantLinksService.find(REP_ID);
        verify(repOrderApplicantLinksRepository, atLeastOnce()).findAllByRepId(REP_ID);
        verify(repOrderApplicantLinksMapper, atLeastOnce()).mapEntityToDTO(anyList());
    }

    @Test
    void givenRepOrderApplicantLinksNotFound_whenFindIsInvoked_thenExceptionIsRaised() {
        when(repOrderApplicantLinksRepository.findAllByRepId(anyInt())).thenReturn(List.of());
        assertThatThrownBy(() -> {
            repOrderApplicantLinksService.find(REP_ID);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Rep Order Applicant Links not found for repId");
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        when(repOrderApplicantLinksRepository.findById(anyInt())).thenReturn(Optional.of(RepOrderApplicantLinksEntity.builder().id(ID).build()));
        when(repOrderApplicantLinksRepository.saveAndFlush(any())).thenReturn(Mockito.mock(RepOrderApplicantLinksEntity.class));
        repOrderApplicantLinksService.update(TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID));
        verify(repOrderApplicantLinksRepository, atLeastOnce()).findById(any());
        verify(repOrderApplicantLinksRepository, atLeastOnce()).saveAndFlush(any());
        verify(repOrderApplicantLinksMapper, atLeastOnce()).mapEntityToDTO(any(RepOrderApplicantLinksEntity.class));
    }

    @Test
    void givenRepOrderApplicantLinksNotFound_whenUpdateIsInvoked_thenExceptionIsRaised() {
        when(repOrderApplicantLinksRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            repOrderApplicantLinksService.update(TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID));
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Rep Order Applicant Link not found for id");
    }

}
