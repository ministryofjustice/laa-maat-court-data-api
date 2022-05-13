package gov.uk.courtdata.authorization.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.authorization.UserReservation;
import gov.uk.courtdata.model.authorization.UserSession;
import gov.uk.courtdata.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserReservationValidatorTest {

    @InjectMocks
    private UserReservationValidator userReservationValidator;

    @Mock
    private UserRepository userRepository;

    @Test
    public void givenCorrectParameters_whenValidateIsInvoked_thenValidationPasses() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        when(userRepository.findById(any())).thenReturn(
                Optional.of(
                        UserEntity.builder()
                                .currentSession(userReservation.getSession().getId())
                                .build()
                )
        );
        assertThat(userReservationValidator.validate(userReservation)).isEqualTo(Optional.empty());
    }

    @Test
    public void givenSpecialUsername_whenValidateIsInvoked_thenValidationPasses() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        UserSession fakeUserSession = userReservation.getSession();

        fakeUserSession.setUsername(CourtDataConstants.RESERVATION_SPECIAL_USERNAMES.get(0));

        when(userRepository.findById(any())).thenReturn(
                Optional.of(
                        UserEntity.builder()
                                .currentSession("FAKE-SESSION-ID")
                                .build()
                )
        );
        assertThat(userReservationValidator.validate(userReservation)).isEqualTo(Optional.empty());

        fakeUserSession.setUsername(CourtDataConstants.RESERVATION_SPECIAL_USERNAMES.get(1));
        assertThat(userReservationValidator.validate(userReservation)).isEqualTo(Optional.empty());
    }

    @Test
    public void givenUserReservationIsNull_whenValidateIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> userReservationValidator.validate(null)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("User reservation is required");
    }

    @Test
    public void givenSessionIdIsMissing_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        UserSession fakeUserSession = UserSession.builder().build();

        fakeUserSession.setUsername("test-f");
        userReservation.setSession(fakeUserSession);

        assertThatThrownBy(
                () -> userReservationValidator.validate(userReservation)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("User session attributes are missing");

        fakeUserSession.setId("");

        assertThatThrownBy(
                () -> userReservationValidator.validate(userReservation)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("User session attributes are missing");
    }

    @Test
    public void givenUsernameIsMissing_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        UserSession fakeUserSession = UserSession.builder().build();

        fakeUserSession.setId("test-f6E3E618A32AC870D07A65CD7AB9131AD");
        userReservation.setSession(fakeUserSession);

        assertThatThrownBy(
                () -> userReservationValidator.validate(userReservation)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("User session attributes are missing");

        fakeUserSession.setUsername("");

        assertThatThrownBy(
                () -> userReservationValidator.validate(userReservation)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("User session attributes are missing");
    }

    @Test
    public void givenReservationIdIsMissing_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        userReservation.setReservationId(null);

        assertThatThrownBy(
                () -> userReservationValidator.validate(userReservation)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("Reservation attributes are missing");
    }

    @Test
    public void givenNoExistingSession_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> userReservationValidator.validate(userReservation)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("Stale user session, reservation not allowed");
    }

    @Test
    public void givenExistingMismatchedSession_whenValidateIsInvoked_thenExceptionIsThrown() {
        UserReservation userReservation = TestModelDataBuilder.getUserReservation();
        when(userRepository.findById(any())).thenReturn(
                Optional.of(
                        UserEntity.builder()
                                .currentSession("FAKE-SESSION-ID")
                                .build()
                )
        );
        assertThatThrownBy(
                () -> userReservationValidator.validate(userReservation)
        ).isInstanceOf(ValidationException.class).hasMessageContaining("Stale user session, reservation not allowed");
    }
}
