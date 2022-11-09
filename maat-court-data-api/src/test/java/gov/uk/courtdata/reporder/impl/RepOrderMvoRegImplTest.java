package gov.uk.courtdata.reporder.impl;


import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.RepOrderMvoRegRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RepOrderMvoRegImplTest {

    @InjectMocks
    private RepOrderMvoRegImpl repOrderMvoRegImpl;

    @Mock
    private RepOrderMvoRegRepository repOrderMvoRegRepository;

    @Test
    public void givenMvoIdAndVehicleOwner_whenFindRepOrderMvoByRepIdAndVehicleOwnerIsInvoked_thenRepOrderMvoEntityInfoIsRetrieved() {
        repOrderMvoRegImpl.findByCurrentMvoRegistration(TestModelDataBuilder.MVO_ID);
        verify(repOrderMvoRegRepository, times(1)).findByMvoIdAndAndDateDeletedIsNull(TestModelDataBuilder.MVO_ID);
    }
}