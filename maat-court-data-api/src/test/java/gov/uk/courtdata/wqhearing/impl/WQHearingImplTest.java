package gov.uk.courtdata.wqhearing.impl;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class WQHearingImplTest {

    @Mock
    private WQHearingRepository wqHearingRepository;
    @InjectMocks
    private WQHearingImpl wqHearingImpl;

    @Test
    public void givenAValidParameter_whenFindByMaatIdAndHearingUUIDIsInvoked_shouldReturnWQHearing() {
        wqHearingImpl.findByMaatIdAndHearingUUID(TestModelDataBuilder.HEARING_ID.toString(), TestModelDataBuilder.REP_ID);
        verify(wqHearingRepository, atLeastOnce()).findByMaatIdAndHearingUUID(anyInt(), anyString());
    }

}