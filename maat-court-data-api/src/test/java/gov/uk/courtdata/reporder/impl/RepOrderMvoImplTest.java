package gov.uk.courtdata.reporder.impl;


import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.reporder.projection.RepOrderMvoEntityInfo;
import gov.uk.courtdata.repository.RepOrderMvoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RepOrderMvoImplTest {

    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";
    @InjectMocks
    private RepOrderMvoImpl repOrderMvoImpl;
    @Mock
    private RepOrderMvoRepository repOrderMvoRepository;

    @Test
    public void givenRepIdAndVehicleOwner_whenFindRepOrderMvoByRepIdAndVehicleOwnerIsInvoked_thenRepOrderMvoEntityInfoIsRetrieved() {
        repOrderMvoImpl.findRepOrderMvoByRepIdAndVehicleOwner(TestModelDataBuilder.REP_ID, VEHICLE_OWNER_INDICATOR_YES);
        verify(repOrderMvoRepository, times(1)).findByRepIdAndVehicleOwner(TestModelDataBuilder.REP_ID, VEHICLE_OWNER_INDICATOR_YES, RepOrderMvoEntityInfo.class);
    }
}