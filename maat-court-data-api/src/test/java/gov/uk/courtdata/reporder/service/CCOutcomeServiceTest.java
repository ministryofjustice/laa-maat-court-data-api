package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.impl.CCOutcomeImpl;
import gov.uk.courtdata.reporder.mapper.CCOutcomeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CCOutcomeServiceTest {

    @Mock
    private CCOutcomeImpl repOrderCCOutcomeImpl;

    @Mock
    private CCOutcomeMapper mapper;

    @InjectMocks
    private CCOutcomeService service;


    @Test
    void givenAValidInput_whenCreateIsInvoked_thenCreateOutcomeIsSuccess() {
        when(repOrderCCOutcomeImpl.create(any()))
                .thenReturn(TestEntityDataBuilder.getRepOrderCCOutcomeEntity());
        service.create(TestModelDataBuilder.getRepOrderCCOutcome());
        verify(repOrderCCOutcomeImpl, atLeastOnce()).create(any());
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdatedCCOutComeIsSuccess() {
        when(repOrderCCOutcomeImpl.find(anyInt())).thenReturn(RepOrderCCOutComeEntity.builder().build());
        service.update(TestModelDataBuilder.getRepOrderCCOutcome());
        verify(repOrderCCOutcomeImpl, atLeastOnce()).update(any());
    }

    @Test
    void givenAInvalidOutcomeId_whenUpdateIsInvoked_thenReturnException() {
        when(repOrderCCOutcomeImpl.find(anyInt())).thenThrow(new RequestedObjectNotFoundException(""));
        assertThatThrownBy(() -> service.update(TestModelDataBuilder.getRepOrderCCOutcome()))
                .isInstanceOf(RequestedObjectNotFoundException.class);
    }

    @Test
    void givenAValidInput_whenFindByRepIdIsInvoked_shouldReturnRepOrderCCOutComeEntity() {
        service.findByRepId(TestModelDataBuilder.REP_ID);
        verify(repOrderCCOutcomeImpl, atLeastOnce()).findByRepId(any());
    }
}