package gov.uk.courtdata.ccoutcome.impl;

import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RepOrderCCOutComeImplTest {

    @Mock
    private CrownCourtProcessingRepository repository;

    @InjectMocks
    private CCOutComeImpl repOrderCCOutComeImpl;

    @Test
    public void givenAValidParameter_whenCreateCCOutComeIsInvoked_shouldSaveRepOrderCCOutComeEntity() {
        repOrderCCOutComeImpl.createCCOutCome(any());
        verify(repository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    public void givenAValidParameter_whenUpdateCCOutcomeIsInvoked_shouldUpdateCCOutCome() {
        repOrderCCOutComeImpl.updateCCOutcome(any());
        verify(repository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    public void givenAValidParameter_whenFindByRepIdIsInvoked_shouldReturnRepOrderCCOutComeEntity() {
        repOrderCCOutComeImpl.findByRepId(any());
        verify(repository, atLeastOnce()).findByRepId(any());
    }

    @Test
    public void givenAValidParameter_whenFindIsInvoked_shouldReturnRepOrderCCOutComeEntity() {
        repOrderCCOutComeImpl.find(any());
        verify(repository, atLeastOnce()).getReferenceById(any());
    }

}