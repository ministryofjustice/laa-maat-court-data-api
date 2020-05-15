package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.crowncourt.CrownCourtProcessingImpl;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class HearingResultedImplTest {

    @InjectMocks
    private HearingResultedImpl hearingResultedImpl;

    @Mock
    private CrownCourtProcessingImpl crownCourtProcessingImpl;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenACaseDetailForCrownCourt_whenHearingResultedImplIsInvoked_thenCrownCourtProcessingImplIsInvoked() {
        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().jurisdictionType(JurisdictionType.CROWN).build();
        //when
        hearingResultedImpl.execute(laaHearingDetails);
        //then
        verify(crownCourtProcessingImpl, times(1)).execute(laaHearingDetails);
    }
}
