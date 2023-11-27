package gov.uk.courtdata.reporder.impl;

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
class CCOutcomeImplTest {

    @Mock
    private CrownCourtProcessingRepository repository;

    @InjectMocks
    private CCOutcomeImpl repOrderCCOutcomeImpl;

    @Test
    void givenAValidParameter_whenCreateIsInvoked_thenSaveIsSuccess() {
        repOrderCCOutcomeImpl.create(any());
        verify(repository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidParameter_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        repOrderCCOutcomeImpl.update(any());
        verify(repository, atLeastOnce()).saveAndFlush(any());
    }

    @Test
    void givenAValidParameter_whenFindByRepIdIsInvoked_thenOutcomeIsRetrieved() {
        repOrderCCOutcomeImpl.findByRepId(any());
        verify(repository, atLeastOnce()).findByRepOrder_Id(any());
    }

    @Test
    void givenAValidParameter_whenFindIsInvoked_thenOutcomeIsRetrieved() {
        repOrderCCOutcomeImpl.find(any());
        verify(repository, atLeastOnce()).findById(any());
    }

}