package gov.uk.courtdata.integration.authorization;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class AuthorizationControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String VALID_TEST_USER = "test-user-valid";
    private final String AUTHORISED_ACTION = "VALID_ACTION";
    private final String DISABLED_ACTION = "DISABLED_ACTION";
    private final String VALID_NWORCODE = "VALID_WORK_REASON";
    private final String MISSING_USER_NAME_OR_NWORCODE_ERROR = "Username and new work reason are required";
    private final String MISSING_USER_NAME_OR_ACTION_ERROR = "Username and action are required";
    private final Integer VALID_RESERVATION_ID = 1234;
    private final String VALID_SESSION_ID = "valid-session";
    private final String MISSING_USER_ATTRIBUTES_ERROR = "User session attributes are missing";
    private final String BASE_URL = "/api/internal/v1/assessment/authorization/users/";
    private final String ROLE_ACTION_AUTHORIZED_URL = BASE_URL + "{username}/actions/{action}";
    private final String NEW_WORK_REASON_AUTHORIZED_URL = BASE_URL + "{username}/work-reasons/{nworCode}";
    private final String IS_RESERVED_URL = BASE_URL + "{username}/reservations/{reservationId}/sessions/{sessionId}";

    @Autowired
    private RoleActionsRepository roleActionsRepository;
    @Autowired
    private ReservationsRepository reservationsRepository;
    @Autowired
    private RoleWorkReasonsRepository roleWorkReasonsRepository;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupTestData();
    }

    private void setupTestData() {
        roleActionsRepository.deleteAll();
        reservationsRepository.deleteAll();
        roleWorkReasonsRepository.deleteAll();
        userRolesRepository.deleteAll();

        String AUTHORISED_ROLE = "VALID_ROLE";
        String DISABLED_ROLE = "DISABLED_ROLE";
        List<UserRoleEntity> userRoleEntities = List.of(
                UserRoleEntity.builder()
                        .username(VALID_TEST_USER).roleName(AUTHORISED_ROLE).build(),
                UserRoleEntity.builder()
                        .username(VALID_TEST_USER).roleName(DISABLED_ROLE).build()
        );

        userRolesRepository.saveAll(userRoleEntities);

        String ROLE_ACTION_ENABLED = "Y";
        String ROLE_ACTION_DISABLED = "N";
        List<RoleActionEntity> roleActionEntities = List.of(
            RoleActionEntity.builder()
                    .Id(1).roleName(AUTHORISED_ROLE).action(AUTHORISED_ACTION).enabled(ROLE_ACTION_ENABLED).build(),
            RoleActionEntity.builder()
                    .Id(2).roleName(DISABLED_ROLE).action(DISABLED_ACTION).enabled(ROLE_ACTION_DISABLED).build()
        );

        roleActionsRepository.saveAll(roleActionEntities);

        roleWorkReasonsRepository
                .save(RoleWorkReasonEntity.builder()
                        .id(1)
                        .roleName(AUTHORISED_ROLE)
                        .nworCode(VALID_NWORCODE)
                        .dateCreated(LocalDateTime.now())
                        .userCreated(VALID_TEST_USER)
                        .build());


        LocalDateTime reservationDate = LocalDateTime.now();
        LocalDateTime expiryDate = reservationDate.plusHours(3);

        reservationsRepository.save(ReservationsEntity.builder()
                .recordId(1)
                .userName(VALID_TEST_USER)
                .userSession(VALID_SESSION_ID)
                .recordId(VALID_RESERVATION_ID)
                .reservationDate(reservationDate)
                .recordName("REP_ORDER")
                .expiryDate(expiryDate)
                .build());

        userRepository.save(UserEntity.builder()
                        .username(VALID_TEST_USER)
                        .currentSession(VALID_SESSION_ID)

                .build());
    }

    @Test
    public void givenAValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getAuthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, AUTHORISED_ACTION));
    }

    @Test
    public void givenAValidUserAnInvalidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, "UNKNOWN_ACTION"));
    }

    @Test
    public void givenAnInvalidUserAndValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, "INVALID_USER", AUTHORISED_ACTION));
    }

    @Test
    public void givenADisabledRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, DISABLED_ACTION));
    }

    @Test
    public void givenMissingUsername_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(MISSING_USER_NAME_OR_ACTION_ERROR, get(ROLE_ACTION_AUTHORIZED_URL, " ", AUTHORISED_ACTION));
    }

    @Test
    public void givenMissingAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(MISSING_USER_NAME_OR_ACTION_ERROR, get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, " "));
    }

    @Test
    public void givenValidUsernameAndNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getAuthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, VALID_NWORCODE));
    }

    @Test
    public void givenValidUsernameAndInvalidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getUnauthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, "INVALID_WORK_REASON"));
    }

    @Test
    public void givenAnInvalidUsernameAndValidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getUnauthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, "invalid-user", VALID_NWORCODE));
    }

    @Test
    public void givenMissingNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(MISSING_USER_NAME_OR_NWORCODE_ERROR, get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, " "));
    }

    @Test
    public void givenMissingUsername_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(MISSING_USER_NAME_OR_NWORCODE_ERROR, get(NEW_WORK_REASON_AUTHORIZED_URL, " ", VALID_NWORCODE));
    }

    @Test
    public void givenAReservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturnedAndTheReservationTimeIsUpdated() throws Exception {
        runSuccessScenario(getAuthorizedResponse(), get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, VALID_SESSION_ID));
    }

    @Test
    public void givenAnUnreservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(getUnauthorizedResponse(), get(IS_RESERVED_URL, VALID_TEST_USER, 5678, VALID_SESSION_ID));
    }

    @Test
    public void givenAStaleUserSession_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(
                "Stale user session, reservation not allowed",
                get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, "Stale-session"));
    }

    @Test
    public void givenAMissingUsername_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(MISSING_USER_ATTRIBUTES_ERROR, get(IS_RESERVED_URL, " ", VALID_RESERVATION_ID, VALID_SESSION_ID));
    }

    @Test
    public void givenAMissingSessionId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(MISSING_USER_ATTRIBUTES_ERROR, get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, " "));
    }

    @Test
    @Ignore("This test will fail until LASB-1141 has been addressed.")
    public void givenAMissingReservationId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        runBadRequestErrorScenario(
                "Reservation attributes are missing",
                get(IS_RESERVED_URL, VALID_TEST_USER, "null", VALID_SESSION_ID));
    }

    private AuthorizationResponse getAuthorizedResponse() {
       return AuthorizationResponse.builder().result(true).build();
    }

    private AuthorizationResponse getUnauthorizedResponse() {
        return AuthorizationResponse.builder().result(false).build();
    }
}
