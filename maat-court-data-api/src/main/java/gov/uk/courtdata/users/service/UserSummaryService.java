package gov.uk.courtdata.users.service;

import gov.uk.courtdata.dto.FeatureToggleDTO;
import gov.uk.courtdata.dto.ReservationsDTO;
import gov.uk.courtdata.dto.RoleDataItemDTO;
import gov.uk.courtdata.dto.UserSummaryDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.entity.RoleDataItemEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReflectionHelper;
import gov.uk.courtdata.mapper.ReservationsMapper;
import gov.uk.courtdata.mapper.RoleDataItemsMapper;
import gov.uk.courtdata.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleDataItemsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import gov.uk.courtdata.repository.UserRepository;
import gov.uk.courtdata.service.FeatureToggleService;
import gov.uk.courtdata.users.mapper.UserSummaryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    private final FeatureToggleService featureToggleService;

    public UserSummaryDTO getUserSummary(String username) {

        Optional<List<String>> roleActions = roleActionsRepository.getRoleActionsForUser(username);
        List<String> userRoleActions = roleActions.isEmpty() ? null : roleActions.get();

        Optional<List<String>> newWorkReason = roleWorkReasonsRepository.getNewWorkReasonForUser(username);
        List<String> newWorkReasonForUser = newWorkReason.isEmpty() ? null : newWorkReason.get();

        Optional<List<RoleDataItemEntity>> roleDataItemEntities = roleDataItemsRepository.getRoleDataItemsForUser(username);
        List<RoleDataItemEntity> roleDataItemEntityList = roleDataItemEntities.isEmpty() ? null : roleDataItemEntities.get();
        List<RoleDataItemDTO> roleDataItemDTOList = (roleDataItemEntityList != null) ? roleDataItemsMapper.roleDataItemEntitytoDTO(roleDataItemEntityList) : null;

        Optional<ReservationsEntity> reservations = reservationsRepositoryHelper.getReservationByUserName(username);
        ReservationsDTO reservationsDTO = reservations.isEmpty() ? null : reservationsMapper.reservationsEntitytoDTO(reservations.get());

        List<FeatureToggleDTO> featureToggleDtos = featureToggleService.getFeatureTogglesForUser(username);

        Optional<UserEntity> userEntity = userRepository.findById(username);
        String currentUserSession = userEntity.isEmpty() ? null : userEntity.get().getCurrentSession();
        return userSummaryMapper.userToUserSummaryDTO(username, newWorkReasonForUser, userRoleActions, reservationsDTO, currentUserSession, roleDataItemDTOList, featureToggleDtos);
    }

    public UserEntity getUser(String username) {
        Optional<UserEntity> userEntity = userRepository.findById(username);
        if (userEntity.isEmpty()) {
            String message = String.format("No User found with user name: [%s]", username);
            throw new RequestedObjectNotFoundException(message);
        }
        return userEntity.get();
    }

    public void createUser(UserEntity userEntity) {
        userRepository.saveAndFlush(userEntity);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(UserEntity updateFields) {
        UserEntity userEntity = getUser(updateFields.getUsername());
        ReflectionHelper.updateEntityFromObject(userEntity, updateFields);
        userRepository.save(userEntity);
    }

    public void patchUser(String username, Map<String, Object> updateFields) {
        UserEntity userEntity = getUser(username);
        ReflectionHelper.updateEntityFromMap(userEntity, updateFields);
        userRepository.save(userEntity);
    }
}
