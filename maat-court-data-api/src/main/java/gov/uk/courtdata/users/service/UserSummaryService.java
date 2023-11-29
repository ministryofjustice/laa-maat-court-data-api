package gov.uk.courtdata.users.service;

import gov.uk.courtdata.dto.UserSummaryDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import gov.uk.courtdata.users.mapper.UserSummaryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserSummaryService {
    private final RoleActionsRepository roleActionsRepository;
    private final RoleWorkReasonsRepository roleWorkReasonsRepository;
    private final UserSummaryMapper userSummaryMapper;
    private final ReservationsRepositoryHelper reservationsRepositoryHelper;

    public UserSummaryDTO getUserSummary(String username) {

        Optional<List<String>> roleActions = roleActionsRepository.getRoleActionsForUser(username);
        List<String> userRoleActions = roleActions.isEmpty() ? null : roleActions.get();

        Optional<List<String>> newWorkReason = roleWorkReasonsRepository.getNewWorkReasonForUser(username);
        List<String> newWorkReasonForUser = newWorkReason.isEmpty() ? null : newWorkReason.get();

        Optional<ReservationsEntity> reservations = reservationsRepositoryHelper.getReservationByUserName(username);
        ReservationsEntity reservationsEntity = reservations.isEmpty() ? null : reservations.get();

        return userSummaryMapper.userToUserSummaryDTO(username, newWorkReasonForUser, userRoleActions, reservationsEntity);

    }
}
