package gov.uk.courtdata.users.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.repository.RoleActionsRepository;
import gov.uk.courtdata.repository.RoleDataItemsRepository;
import gov.uk.courtdata.repository.RoleWorkReasonsRepository;
import gov.uk.courtdata.repository.UserRepository;
import gov.uk.courtdata.users.mapper.UserSummaryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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


    @Test
    void whenGetUserSummaryIsInvoked_thenUserSummaryDTOIsReturned() {
        userSummaryService.getUserSummary(TestModelDataBuilder.TEST_USER);
        verify(roleActionsRepository).getRoleActionsForUser(any());
        verify(roleWorkReasonsRepository).getNewWorkReasonForUser(any());
        verify(roleDataItemsRepository).getRoleDataItemsForUser(any());
        verify(reservationsRepositoryHelper).getReservationByUserName(any());
        verify(userRepository).findById(any());
        verify(userSummaryMapper).userToUserSummaryDTO(any(), any(), any(), any(), any(), any());
    }

}
