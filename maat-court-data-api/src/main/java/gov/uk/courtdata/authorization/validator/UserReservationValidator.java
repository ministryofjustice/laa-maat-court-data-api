package gov.uk.courtdata.authorization.validator;

import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.authorization.UserReservation;
import gov.uk.courtdata.model.authorization.UserSession;
import gov.uk.courtdata.repository.UserRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.RESERVATION_SPECIAL_USERNAMES;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@AllArgsConstructor
@Component
public class UserReservationValidator implements IValidator<Void, UserReservation> {

    private final UserRepository userRepository;

    @Override
    public Optional<Void> validate(UserReservation userReservation) {
        if (userReservation != null) {

            UserSession session = userReservation.getSession();
            if (isBlank(session.getId()) || isBlank(session.getUsername())) {
                throw new ValidationException("User session attributes are missing");
            }

            checkUserSession(session);

            if (userReservation.getReservationId() == null) {
                throw new ValidationException("Reservation attributes are missing");
            }
        }
        return Optional.empty();
    }

    private void checkUserSession(UserSession session) {
        UserEntity user = userRepository.findById(session.getUsername())
                .orElse(null);

        if (user != null) {
            if (!(user.getCurrentSession().equals(session.getId()) || RESERVATION_SPECIAL_USERNAMES.contains(session.getUsername()))) {
                throw new ValidationException("Stale user session, reservation not allowed");
            }
        }
    }
}
