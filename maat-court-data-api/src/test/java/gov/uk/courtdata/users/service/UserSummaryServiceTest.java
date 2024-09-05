package gov.uk.courtdata.users.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.repository.FeatureToggleRepository;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleDataItemsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import gov.uk.courtdata.repository.UserRepository;
import gov.uk.courtdata.service.FeatureToggleService;
import gov.uk.courtdata.users.mapper.UserSummaryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserSummaryServiceTest {

    @InjectMocks
    private UserSummaryService userSummaryService;

    @Mock
    private RoleActionsRepository roleActionsRepository;
    @Mock
    private RoleWorkReasonsRepository roleWorkReasonsRepository;
    @Mock
    private UserSummaryMapper userSummaryMapper;
    @Mock
    private ReservationsRepositoryHelper reservationsRepositoryHelper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleDataItemsRepository roleDataItemsRepository;
    @Mock
    private FeatureToggleService featureToggleService;

    @Test
    void whenGetUserSummaryIsInvoked_thenUserSummaryDTOIsReturned() {
        userSummaryService.getUserSummary(TestModelDataBuilder.TEST_USER);
        verify(roleActionsRepository).getRoleActionsForUser(any());
        verify(roleWorkReasonsRepository).getNewWorkReasonForUser(any());
        verify(roleDataItemsRepository).getRoleDataItemsForUser(any());
        verify(featureToggleService).getFeatureTogglesForUser(any());
        verify(reservationsRepositoryHelper).getReservationByUserName(any());
        verify(userRepository).findById(any());
        verify(userSummaryMapper).userToUserSummaryDTO(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void givenInvalidUser_whenGetUserIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(() -> userSummaryService.getUser(TestModelDataBuilder.TEST_USER))
                .isInstanceOf(RequestedObjectNotFoundException.class);
    }

    @Test
    void givenValidUser_whenGetUserIsInvoked_thenUserEntityIsReturned() {
        when(userRepository.findById(TestModelDataBuilder.TEST_USER))
                .thenReturn(Optional.of(UserEntity.builder().build()));
        userSummaryService.getUser(TestModelDataBuilder.TEST_USER);
    }

    @Test
    void givenValidUserEntity_whenCreateUserIsInvoked_thenUserIsCreated() {
        UserEntity userEntity = UserEntity.builder().build();
        userSummaryService.createUser(userEntity);
        verify(userRepository).saveAndFlush(userEntity);
    }

    @Test
    void givenValidUser_whenDeleteUserIsInvoked_thenUserIsDeleted() {
        userSummaryService.deleteUser(TestModelDataBuilder.TEST_USER);
        verify(userRepository).deleteById(TestModelDataBuilder.TEST_USER);
    }

    @Test
    void givenValidUser_whenPatchUserIsInvoked_thenUserIsUpdated() throws JsonProcessingException {
        String userName = "TEST_USER";
        UserEntity userEntity = UserEntity.builder()
                .username(userName)
                .loggedIn("N")
                .build();
        String requestJson = """
                {
                  "loggedIn" : "Y",
                  "currentSession" : null
                }
                """;
        Map<String, Object> updateFields = new ObjectMapper().readValue(requestJson, HashMap.class);
        when(userRepository.findById(userName)).thenReturn(Optional.of(userEntity));
        userSummaryService.patchUser(userName, updateFields);
        verify(userRepository).save(userEntity);
    }

    @Test
    void givenValidUser_whenUpdateUserIsInvoked_thenUserIsUpdated() {
        String userName = "TEST_USER";
        UserEntity userEntity = UserEntity.builder()
                .username(userName)
                .loggedIn("N")
                .build();

        UserEntity updateFields = UserEntity.builder()
                .username(userName)
                .loggedIn("Y")
                .currentSession("mock-session")
                .build();
        when(userRepository.findById(userName)).thenReturn(Optional.of(userEntity));
        userSummaryService.updateUser(updateFields);
        verify(userRepository).save(userEntity);
    }

}
