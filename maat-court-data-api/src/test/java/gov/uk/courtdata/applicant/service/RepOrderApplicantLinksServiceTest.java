package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepOrderApplicantLinksServiceTest {

    @Mock
    private RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;

    @Mock
    private RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;

    @InjectMocks
    private RepOrderApplicantLinksService repOrderApplicantLinksService;

    @Test
    void givenAValidInput_whenGetRepOrderApplicantLinksIsInvoked_thenShouldReturnsListOfRepOrderApplicantLinksDTO() {
        when(repOrderApplicantLinksRepository.findAllByRepId(anyInt())).thenReturn(List.of(RepOrderApplicantLinksEntity.builder().repId(TestModelDataBuilder.REP_ID).build()));
        repOrderApplicantLinksService.find(REP_ID);
        verify(repOrderApplicantLinksRepository, atLeastOnce()).findAllByRepId(REP_ID);
        verify(repOrderApplicantLinksMapper, atLeastOnce()).mapEntityToDTO(any());
    }

    @Test
    void givenRepOrderApplicantLinksNotFound_whenFindAllIsInvoked_thenExceptionIsRaised() {
        when(repOrderApplicantLinksRepository.findAllByRepId(anyInt())).thenReturn(List.of());
        assertThatThrownBy(() -> {
            repOrderApplicantLinksService.find(REP_ID);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Rep Order Applicant Links not found for repId");
    }

}
