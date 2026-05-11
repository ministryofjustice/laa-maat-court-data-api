package gov.uk.courtdata.link.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.link.validator.PreConditionsValidator;
import gov.uk.courtdata.model.CaseDetailsValidate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class LinkControllerTest {

    @InjectMocks
    private LinkController linkController;

    @Mock
    private PreConditionsValidator preConditionsValidator;

    @Test
    void givenCaseValidationDetails_whenValidateIsInvoked_thenValidationPerformed() {
        CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate.builder().caseUrn("1244").maatId(1234).build();
        ResponseEntity<Object> response = linkController.validate(caseDetailsValidate, anyString());

        verify(preConditionsValidator).validate(caseDetailsValidate);
        assertThat(response).isEqualTo(ResponseEntity.ok().build());
    }
}
