package gov.uk.courtdata.unlink.controller;

import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.validator.UnLinkValidationProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class UnLinkControllerTest {

    @InjectMocks
    private UnLinkController unLinkController;

    @Mock
    private UnLinkValidationProcessor unLinkValidationProcessor;

    @Test
    public void givenUnLink_WhenUnLinkValidationIsInvoked_thenValidateMAATID() {

        Unlink unlink = Unlink.builder()
                .maatId(121211)
                .reasonId(23555)
                .otherReasonText("some other reason text")
                .userId("user_id_here")
                .build();

        ResponseEntity<?> responseEntity = unLinkController.validate("", unlink);
        verify(unLinkValidationProcessor).validate(unlink);

        assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build());
        assertNull(responseEntity.getBody());
    }
}