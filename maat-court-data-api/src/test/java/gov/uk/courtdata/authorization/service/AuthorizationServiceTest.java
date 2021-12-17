package gov.uk.courtdata.authorization.service;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.AuthorizationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @Test
    public void givenInvalidAction_whenIsRoleActionAuthorizedIsInvoked_thenValidationExceptionIsThrown() {
        when(authorizationRepository.getAvailableActions()).thenReturn(List.of());
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> authorizationService.isRoleActionAuthorized("test-f", "FAKE_ACTION"));
        assertThat(validationException.getMessage()).isEqualTo("The specified action does not exist");
    }

    @Test
    public void givenInvalidUsername_whenIsRoleActionAuthorizedIsInvoked_thenValidationExceptionIsThrown() {
        when(authorizationRepository.getAuthorizedActions(any())).thenReturn(List.of());
        when(authorizationRepository.getAvailableActions()).thenReturn(List.of("CREATE_ASSESSMENT"));

        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> authorizationService.isRoleActionAuthorized("fake-u", "CREATE_ASSESSMENT"));
        assertThat(validationException.getMessage()).isEqualTo("User does not exist or has invalid role");
    }

    @Test
    public void givenValidParameters_whenIsRoleActionAuthorizedIsInvoked_thenResultIsReturned() {
        when(authorizationRepository.getAvailableActions()).thenReturn(List.of("CREATE_ASSESSMENT", "FAKE_ACTION"));
        when(authorizationRepository.getAuthorizedActions(any())).thenReturn(List.of("CREATE_ASSESSMENT"));

        assertThat(authorizationService.isRoleActionAuthorized("test-f", "FAKE_ACTION")).isEqualTo(Boolean.FALSE);
        assertThat(authorizationService.isRoleActionAuthorized("test-f", "CREATE_ASSESSMENT")).isEqualTo(Boolean.TRUE);
    }
}
