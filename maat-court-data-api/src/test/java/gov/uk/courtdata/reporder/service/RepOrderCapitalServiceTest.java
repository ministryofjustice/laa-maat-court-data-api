package gov.uk.courtdata.reporder.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.reporder.impl.RepOrderCapitalImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepOrderCapitalServiceTest {

    @InjectMocks
    private RepOrderCapitalService service;

    @Mock
    private RepOrderCapitalImpl repOrderCapital;

    @Test
    void givenAValidRepId_whenGetCapitalAssetCountIsInvoked_thenReturnCount() {
        service.getCapitalAssetCount(TestModelDataBuilder.REP_ID);
        verify(repOrderCapital).getCapitalAssetCount(any());
    }
}
