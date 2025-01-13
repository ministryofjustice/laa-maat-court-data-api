package gov.uk.courtdata.authorization.service;

import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ConfigParametersEntity;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.authorization.UserReservation;
import gov.uk.courtdata.model.authorization.UserSession;
import gov.uk.courtdata.repository.ConfigParametersRepository;
import gov.uk.courtdata.repository.ReservationsRepository;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.RESERVATION_RECORD_NAME;
import static org.apache.commons.lang3.StringUtils.isAnyBlank;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final RoleActionsRepository roleActionsRepository;
    private final ReservationsRepository reservationsRepository;
    private final RoleWorkReasonsRepository roleWorkReasonsRepository;
    private final ConfigParametersRepository configParametersRepository;

    public boolean isRoleActionAuthorized(String username, String action) {
        if (isAnyBlank(username, action)) {
            throw new ValidationException("Username and action are required");
        }
        return roleActionsRepository.getRoleAction(username, action.toUpperCase()).isPresent();
    }

    public boolean isNewWorkReasonAuthorized(String username, String nworCode) {
        if (isAnyBlank(username, nworCode)) {
            throw new ValidationException("Username and new work reason are required");
        }
        return roleWorkReasonsRepository.getNewWorkReason(username, nworCode.toUpperCase()).isPresent();
    }

    @Transactional
    public boolean isReserved(UserReservation userReservation) {
        UserSession session = userReservation.getSession();
        Optional<ReservationsEntity> existingReservation =
                reservationsRepository.findReservationByUserSession(
                        session.getId(), session.getUsername(), RESERVATION_RECORD_NAME, userReservation.getReservationId()
                );
        if (existingReservation.isEmpty()) {
            return false;
        }

//        List<ConfigParametersEntity> configParams = configParametersRepository.retrieveAll();

//        Optional<ConfigParametersEntity> filteredParams = configParametersRepository.findConfigParametersEntityByCodeAndEffectiveDate("RESERVATION_TIME", LocalDate.of(2022, 1, 1));

        reservationsRepository.updateReservationExpiryDate(session.getId(), session.getUsername(), RESERVATION_RECORD_NAME, userReservation.getReservationId());

        return true;
    }
}
