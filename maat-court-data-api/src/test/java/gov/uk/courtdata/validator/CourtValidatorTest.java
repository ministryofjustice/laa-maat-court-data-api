package gov.uk.courtdata.validator;


import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class CourtValidatorTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @InjectMocks
    private CourtValidator courtValidator;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenCjsCodeNotAvailable_throwsValidationException() {

        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("cjs area code not found.");
        courtValidator.validate(CaseDetails.builder().maatId(100).caseUrn("caseURN").cjsAreaCode(null).build());
    }

    @Test
    public void testWhenSessionsNotAvailable_throwsValidationException() {

        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Sessions not available.");
        courtValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16").build());
    }


    @Test
    public void testWhenCourtLocationsNotAvailable_throwsValidationException() {

        exceptionRule.expect(ValidationException.class);
        exceptionRule.expectMessage("Court Location not available in session.");
        courtValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().build()))
                .build());
    }

    @Test
    public void testWhenCjsCodeAndCourtLocationAvailable_validationPasses() {

        courtValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build()))
                .build());

    }


}
