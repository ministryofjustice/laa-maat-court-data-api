package gov.uk.courtdata.wqhearing.impl;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.WQHearingRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WQHearingImplTest {

    @Mock
    private WQHearingRepository wqHearingRepository;

    @InjectMocks
    private WQHearingImpl wqHearingImpl;

    @Test
    public void givenAValidParameter_whenFindByMaatIdAndHearingUUIDIsInvoked_shouldReturnWQHearing() {
        wqHearingImpl.findByMaatIdAndHearingUUID(
                TestModelDataBuilder.REP_ID, TestModelDataBuilder.HEARING_ID.toString());
        verify(wqHearingRepository, atLeastOnce()).findByMaatIdAndHearingUUID(anyInt(), anyString());
    }
}
