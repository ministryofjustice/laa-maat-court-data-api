package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OUCodeValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @InjectMocks
    private OUCodeValidator ouCodeValidator;



    @Test
    public void testUserIdValidator_whenUserIsNullThrowsException() {

        Session session = Session.builder().courtLocation(null).build();
        HearingResulted hearingResulted = HearingResulted.builder().session(session).build();
        thrown.expect(ValidationException.class);
        thrown.expectMessage("OU Code is missing.");

        ouCodeValidator.validate(hearingResulted);
    }


    @Test
    public void testWhenOUCodeIsNotNullANDExist_validationPasses() {
        Session session = Session.builder().courtLocation("BI6G").build();
        HearingResulted hearingResulted = HearingResulted.builder().session(session).build();

        Optional<Void> result = ouCodeValidator.validate(hearingResulted);
        assertThat(result).isNotPresent();

    }
}
