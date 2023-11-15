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

        List<String> userRoleActions = Optional.ofNullable(roleActionsRepository.getRoleActionsForUser(username))
                .map(x -> x.get()).orElse(null);

        List<String> newWorkReasonForUser = Optional.ofNullable(roleWorkReasonsRepository.getNewWorkReasonForUser(username))
                .map(x -> x.get()).orElse(null);

        ReservationsEntity reservationsEntity = Optional.ofNullable(reservationsRepositoryHelper.getReservationByUserName(username))
                .map(x -> x.get()).orElse(null);

        return userSummaryMapper.userToUserSummaryDTO(username, newWorkReasonForUser, userRoleActions, reservationsEntity);

    }
}
