package gov.uk.courtdata.link.controller;

import gov.uk.courtdata.link.validator.PreConditionsValidator;
import gov.uk.courtdata.model.CaseDetailsValidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LinkControllerTest {

    @InjectMocks
    private LinkController linkController;

    @Mock
    private PreConditionsValidator preConditionsValidator;

    @Test
    public void givenCaseValidationDetails_whenValidateIsInvoked_thenValidationPerformed() {
        CaseDetailsValidate caseDetailsValidate = CaseDetailsValidate.builder().caseUrn("1244").maatId(1234).build();
        ResponseEntity<Object> x = linkController.validate(caseDetailsValidate, anyString());

        verify(preConditionsValidator, times(1)).validate(caseDetailsValidate);
        assertThat(x).isEqualTo(ResponseEntity.ok().build());

    }

}
