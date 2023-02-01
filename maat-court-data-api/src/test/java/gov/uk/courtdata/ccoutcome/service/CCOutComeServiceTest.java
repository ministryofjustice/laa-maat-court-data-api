package gov.uk.courtdata.ccoutcome.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.ccoutcome.impl.CCOutComeImpl;
import gov.uk.courtdata.ccoutcome.mapper.CCOutComeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CCOutComeServiceTest {

    @Mock
    private CCOutComeImpl repOrderCCOutComeImpl;

    @Mock
    private CCOutComeMapper mapper;

    @InjectMocks
    private CCOutComeService service;


    @Test
    public void givenAValidInput_whenCreateCCOutComeIsInvoked_shouldSavedCCOutCome() {
        when(repOrderCCOutComeImpl.createCCOutCome(any())).thenReturn(TestEntityDataBuilder.getRepOrderCCOutComeEntity());
        service.createCCOutCome(TestModelDataBuilder.getRepOrderCCOutCome());
        verify(repOrderCCOutComeImpl, atLeastOnce()).createCCOutCome(any());
    }

    @Test
    public void givenAValidInput_whenUpdateCCOutComeIsInvoked_shouldUpdatedCCOutCome() {
        when(repOrderCCOutComeImpl.find(anyInt())).thenReturn(TestEntityDataBuilder.getRepOrderCCOutComeEntity());
        service.updateCCOutcome(TestModelDataBuilder.getRepOrderCCOutCome());
        verify(repOrderCCOutComeImpl, atLeastOnce()).updateCCOutcome(any());
    }

    @Test
    public void givenAValidInput_whenFindByRepIdIsInvoked_shouldReturnRepOrderCCOutComeEntity() {
        List ccOutComeList = List.of(TestEntityDataBuilder.getRepOrderCCOutComeEntity());
        when(repOrderCCOutComeImpl.findByRepId(any())).thenReturn(ccOutComeList);
        service.findByRepId(TestModelDataBuilder.REP_ID);
        verify(repOrderCCOutComeImpl, atLeastOnce()).findByRepId(any());
    }

}