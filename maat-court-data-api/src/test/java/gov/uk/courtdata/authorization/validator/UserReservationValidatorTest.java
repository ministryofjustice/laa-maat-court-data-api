package gov.uk.courtdata.authorization.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.authorization.UserReservation;
import gov.uk.courtdata.model.authorization.UserSession;
import gov.uk.courtdata.repository.UserRepository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserReservationValidatorTest {

    @InjectMocks
    private UserReservationValidator userReservationValidator;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenCorrectParameters_whenValidateIsInvoked_thenValidationPasses() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(UserEntity.builder()
                        .currentSession(userReservation.getSession().getId())
                        .build()));
        assertThat(userReservationValidator.validate(userReservation)).isEqualTo(Optional.empty());
    }

    @Test
    void givenSpecialUsername_whenValidateIsInvoked_thenValidationPasses() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        UserSession fakeUserSession = userReservation.getSession();

        fakeUserSession.setUsername(CourtDataConstants.RESERVATION_SPECIAL_USERNAMES.getFirst());

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(
                        UserEntity.builder().currentSession("FAKE-SESSION-ID").build()));
        assertThat(userReservationValidator.validate(userReservation)).isNotPresent();

        fakeUserSession.setUsername(CourtDataConstants.RESERVATION_SPECIAL_USERNAMES.get(1));
        assertThat(userReservationValidator.validate(userReservation)).isNotPresent();
    }

    @Test
    void givenUserReservationIsNull_whenValidateIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(() -> userReservationValidator.validate(null))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("User reservation is required");
    }

    @Test
    void givenSessionIdIsMissing_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        UserSession fakeUserSession = UserSession.builder().build();

        fakeUserSession.setUsername("test-f");
        userReservation.setSession(fakeUserSession);

        assertThatThrownBy(() -> userReservationValidator.validate(userReservation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("User session attributes are missing");

        fakeUserSession.setId("");

        assertThatThrownBy(() -> userReservationValidator.validate(userReservation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("User session attributes are missing");
    }

    @Test
    void givenUsernameIsMissing_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        UserSession fakeUserSession = UserSession.builder().build();

        fakeUserSession.setId("test-f6E3E618A32AC870D07A65CD7AB9131AD");
        userReservation.setSession(fakeUserSession);

        assertThatThrownBy(() -> userReservationValidator.validate(userReservation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("User session attributes are missing");

        fakeUserSession.setUsername("");

        assertThatThrownBy(() -> userReservationValidator.validate(userReservation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("User session attributes are missing");
    }

    @Test
    void givenReservationIdIsMissing_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        userReservation.setReservationId(null);

        assertThatThrownBy(() -> userReservationValidator.validate(userReservation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Reservation attributes are missing");
    }

    @Test
    void givenNoExistingSession_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userReservationValidator.validate(userReservation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Stale user session, reservation not allowed");
    }

    @Test
    void givenExistingMismatchedSession_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(
                        UserEntity.builder().currentSession("FAKE-SESSION-ID").build()));
        assertThatThrownBy(() -> userReservationValidator.validate(userReservation))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Stale user session, reservation not allowed");
    }
}
