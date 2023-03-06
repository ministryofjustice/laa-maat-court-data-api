package gov.uk.courtdata.reporder.impl;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.RepOrderCapitalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RepOrderCapitalImplTest {

    @InjectMocks
    private RepOrderCapitalImpl repOrderCapital;

    @Mock
    private RepOrderCapitalRepository repository;

    @Test
    void givenAValidRepId_whenGetCapitalAssetCountIsInvoked_thenReturnCount() {
        repOrderCapital.getCapitalAssetCount(TestModelDataBuilder.REP_ID);
        verify(repository).getCapitalAssetCount(any());
    }

}