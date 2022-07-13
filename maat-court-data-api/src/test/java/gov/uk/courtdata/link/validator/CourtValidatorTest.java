package gov.uk.courtdata.link.validator;


import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class CourtValidatorTest {

    @InjectMocks
    private CourtValidator courtValidator;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenCjsCodeNotAvailable_throwsValidationException() {

        Assertions.assertThrows(ValidationException.class, ()->{
            courtValidator.validate(CaseDetails.builder().maatId(100).caseUrn("caseURN").cjsAreaCode(null).build());
        }, "cjs area code not found.");
    }

    @Test
    public void testWhenSessionsNotAvailable_throwsValidationException() {

        Assertions.assertThrows(ValidationException.class, ()->{
            courtValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16").build());
        }, "Sessions not available.");

    }


    @Test
    public void testWhenCourtLocationsNotAvailable_throwsValidationException() {

        Assertions.assertThrows(ValidationException.class, ()-> {
            courtValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                    .sessions(Arrays.asList(Session.builder().build()))
                    .build());
        },"Court Location not available in session.");
    }

    @Test
    public void testWhenCjsCodeAndCourtLocationAvailable_validationPasses() {

        courtValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build()))
                .build());

    }


}
