package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.model.hearing.HearingResulted;
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
public class CrownCourtValidationProcessorTest {

    @Mock
    private CrownCourtOutComesValidator crownCourtOutComesValidator;
    @Mock
    private OUCodeValidator ouCodeValidator;
    @Mock
    private CaseTypeValidator caseTypeValidator;
    @InjectMocks
    private CrownCourtValidationProcessor crownCourtValidationProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void givenHearingData_whenCrownCourtValidationProcessorIsCalled_thenValidatorsAreCalled() {

        //given
        final int testMaatId = 1000;
        final HearingResulted hearingDetails = HearingResulted.builder().maatId(testMaatId).build();

        // when
        crownCourtValidationProcessor.validate(hearingDetails);

        //then
        verify(crownCourtOutComesValidator, times(1)).validate(hearingDetails);
        verify(ouCodeValidator, times(1)).validate(hearingDetails);
        verify(caseTypeValidator, times(1)).validate(hearingDetails);
    }
}
