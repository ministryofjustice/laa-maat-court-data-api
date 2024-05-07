package gov.uk.courtdata.users.service;

import gov.uk.courtdata.dto.ReservationsDTO;
import gov.uk.courtdata.dto.RoleDataItemDTO;
import gov.uk.courtdata.dto.UserSummaryDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
<<<<<<< HEAD
import gov.uk.courtdata.entity.RoleDataItemEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.mapper.ReservationsMapper;
=======
import gov.uk.courtdata.entity.UserEntity;
>>>>>>> main
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleDataItemsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import gov.uk.courtdata.repository.UserRepository;
<<<<<<< HEAD
import gov.uk.courtdata.mapper.RoleDataItemsMapper;
=======
>>>>>>> main
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
    private final UserRepository userRepository;
    private final RoleDataItemsRepository roleDataItemsRepository;
    private final RoleDataItemsMapper roleDataItemsMapper;
    private final ReservationsMapper reservationsMapper;


    public UserSummaryDTO getUserSummary(String username) {

        Optional<List<String>> roleActions = roleActionsRepository.getRoleActionsForUser(username);
        List<String> userRoleActions = roleActions.isEmpty() ? null : roleActions.get();

        Optional<List<String>> newWorkReason = roleWorkReasonsRepository.getNewWorkReasonForUser(username);
        List<String> newWorkReasonForUser = newWorkReason.isEmpty() ? null : newWorkReason.get();

        Optional<List<RoleDataItemEntity>>  roleDataItemEntities = roleDataItemsRepository.getRoleDataItemsForUser(username);
        List<RoleDataItemEntity> roleDataItemEntityList = roleDataItemEntities.isEmpty() ? null :  roleDataItemEntities.get();
        List<RoleDataItemDTO> roleDataItemDTOList = (roleDataItemEntityList != null)  ? roleDataItemsMapper.roleDataItemEntitytoDTO(roleDataItemEntityList):null;

        Optional<ReservationsEntity> reservations = reservationsRepositoryHelper.getReservationByUserName(username);
        ReservationsDTO reservationsDTO = reservations.isEmpty() ? null : reservationsMapper.reservationsEntitytoDTO(reservations.get());

        Optional<UserEntity> userEntity = userRepository.findById(username);
        String currentUserSession = userEntity.isEmpty() ? null : userEntity.get().getCurrentSession();
        return userSummaryMapper.userToUserSummaryDTO(username, newWorkReasonForUser, userRoleActions, reservationsDTO, currentUserSession, roleDataItemDTOList);
    }
}
