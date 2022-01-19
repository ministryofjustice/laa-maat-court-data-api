package gov.uk.courtdata.authorization.validator;

import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.authorization.Reservation;
import gov.uk.courtdata.model.authorization.UserReservation;
import gov.uk.courtdata.model.authorization.UserSession;
import gov.uk.courtdata.repository.UserRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@AllArgsConstructor
@Component
public class UserReservationValidator implements IValidator<Void, UserReservation> {

    private final UserRepository userRepository;

    @Override
    public Optional<Void> validate(UserReservation userReservation) {
        if (userReservation != null) {

            UserSession session = userReservation.getUserSession();
            if (isBlank(session.getAppName()) || isBlank(session.getAppServer()) || isBlank(session.getSession()) || isBlank(session.getUsername())) {
                throw new ValidationException("User session attributes are missing");
            }

            checkUserSession(session);

            Reservation reservation = userReservation.getReservation();
            if (reservation.getRecordId() == null || isBlank(reservation.getRecordName())) {
                throw new ValidationException("Reservation attributes are missing");
            }
        }
        return Optional.empty();
    }

    private void checkUserSession(UserSession session) {
        UserEntity user = userRepository.findById(session.getUsername())
                .orElse(null);

        if (user != null) {
            if (!(user.getCurrentSession().equals(session.getSession()) || session.getUsername().equals("HUB") || session.getUsername().equals("MLA"))) {
                throw new ValidationException("Stale user session, reservation not allowed");
            }
        }
    }
}
