package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.reporder.impl.RepOrderMvoImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMvoMapper;
import gov.uk.courtdata.reporder.projection.RepOrderMvoEntityInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RepOrderMvoServiceTest {

    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";
    @Mock
    private RepOrderMvoImpl repOrderMvoImpl;
    @Mock
    private RepOrderMvoMapper repOrderMvoMapper;
    @InjectMocks
    private RepOrderMvoService repOrderMvoService;

    @Test
    public void givenValidRepIdAndVehicleOwner_whenFindRepOrderMvoByRepIdAndVehicleOwnerIsInvoked_thenRepOrderMvoIsReturned() {
        when(repOrderMvoImpl.findRepOrderMvoByRepIdAndVehicleOwner(anyInt(), anyString()))
                .thenReturn(TestEntityDataBuilder.getRepOrderMvoEntityInfo());
        when(repOrderMvoMapper.repOrderMvoEntityInfoToRepOrderMvoDTO(any(RepOrderMvoEntityInfo.class)))
                .thenReturn(TestModelDataBuilder.getRepOrderMvoDTO());
        assertThat(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(TestModelDataBuilder.REP_ID, VEHICLE_OWNER_INDICATOR_YES))
                .isEqualTo(TestModelDataBuilder.getRepOrderMvoDTO());

    }
}