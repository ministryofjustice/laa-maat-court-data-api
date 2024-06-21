package gov.uk.courtdata.integration.authorization;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.entity.RoleActionEntity;
import gov.uk.courtdata.entity.RoleWorkReasonEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.entity.UserRoleEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
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

    @BeforeEach
    public void setUp() throws Exception {
        setupTestData();
    }

    private void setupTestData() {
        String AUTHORISED_ROLE = "VALID_ROLE";
        String DISABLED_ROLE = "DISABLED_ROLE";
        List<UserRoleEntity> userRoleEntities = List.of(
                UserRoleEntity.builder()
                        .username(VALID_TEST_USER).roleName(AUTHORISED_ROLE).build(),
                UserRoleEntity.builder()
                        .username(VALID_TEST_USER).roleName(DISABLED_ROLE).build()
        );

        repos.userRoles.saveAll(userRoleEntities);

        String ROLE_ACTION_ENABLED = "Y";
        String ROLE_ACTION_DISABLED = "N";
        List<RoleActionEntity> roleActionEntities = List.of(
                RoleActionEntity.builder()
                        .Id(1).roleName(AUTHORISED_ROLE).action(AUTHORISED_ACTION).enabled(ROLE_ACTION_ENABLED).build(),
                RoleActionEntity.builder()
                        .Id(2).roleName(DISABLED_ROLE).action(DISABLED_ACTION).enabled(ROLE_ACTION_DISABLED).build()
        );

        repos.roleActions.saveAll(roleActionEntities);

        repos.roleWorkReasons
                .save(RoleWorkReasonEntity.builder()
                        .id(1)
                        .roleName(AUTHORISED_ROLE)
                        .nworCode(VALID_NWORCODE)
                        .dateCreated(LocalDateTime.now())
                        .userCreated(VALID_TEST_USER)
                        .build());


        LocalDateTime reservationDate = LocalDateTime.now();
        LocalDateTime expiryDate = reservationDate.plusHours(3);

        repos.reservations.save(ReservationsEntity.builder()
                .recordId(1)
                .userName(VALID_TEST_USER)
                .userSession(VALID_SESSION_ID)
                .recordId(VALID_RESERVATION_ID)
                .reservationDate(reservationDate)
                .recordName("REP_ORDER")
                .expiryDate(expiryDate)
                .build());

        repos.user.save(UserEntity.builder()
                .username(VALID_TEST_USER)
                .currentSession(VALID_SESSION_ID)

                .build());
    }

    @Test
    public void givenAValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getAuthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, AUTHORISED_ACTION)));
    }

    @Test
    public void givenAValidUserAnInvalidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, "UNKNOWN_ACTION")));
    }

    @Test
    public void givenAnInvalidUserAndValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, "INVALID_USER", AUTHORISED_ACTION)));
    }

    @Test
    public void givenADisabledRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, DISABLED_ACTION)));
    }

    @Test
    public void givenMissingUsername_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertTrue(runBadRequestErrorScenario(MISSING_USER_NAME_OR_ACTION_ERROR, get(ROLE_ACTION_AUTHORIZED_URL, " ", AUTHORISED_ACTION)));
    }

    @Test
    public void givenMissingAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertTrue(runBadRequestErrorScenario(MISSING_USER_NAME_OR_ACTION_ERROR, get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, " ")));
    }

    @Test
    public void givenValidUsernameAndNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getAuthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, VALID_NWORCODE)));
    }

    @Test
    public void givenValidUsernameAndInvalidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getUnauthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, "INVALID_WORK_REASON")));
    }

    @Test
    public void givenAnInvalidUsernameAndValidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getUnauthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, "invalid-user", VALID_NWORCODE)));
    }

    @Test
    public void givenMissingNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertTrue(runBadRequestErrorScenario(MISSING_USER_NAME_OR_NWORCODE_ERROR, get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, " ")));
    }

    @Test
    public void givenMissingUsername_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertTrue(runBadRequestErrorScenario(MISSING_USER_NAME_OR_NWORCODE_ERROR, get(NEW_WORK_REASON_AUTHORIZED_URL, " ", VALID_NWORCODE)));
    }

    @Test
    public void givenAReservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturnedAndTheReservationTimeIsUpdated() throws Exception {
        assertTrue(runSuccessScenario(getAuthorizedResponse(), get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, VALID_SESSION_ID)));
    }

    @Test
    public void givenAnUnreservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(getUnauthorizedResponse(), get(IS_RESERVED_URL, VALID_TEST_USER, 5678, VALID_SESSION_ID)));
    }

    @Test
    public void givenAStaleUserSession_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertTrue(runBadRequestErrorScenario(
                "Stale user session, reservation not allowed",
                get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, "Stale-session")));
    }

    @Test
    public void givenAMissingUsername_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertTrue(runBadRequestErrorScenario(MISSING_USER_ATTRIBUTES_ERROR, get(IS_RESERVED_URL, " ", VALID_RESERVATION_ID, VALID_SESSION_ID)));
    }

    @Test
    public void givenAMissingSessionId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertTrue(runBadRequestErrorScenario(MISSING_USER_ATTRIBUTES_ERROR, get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, " ")));
    }

    @Test
    public void givenAStringTypeReservationId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        String reservationIdString = "incorrect-type";

        assertTrue(runBadRequestErrorScenario(
                String.format("The provided value '%s' is the incorrect type for the 'reservationId' parameter.", reservationIdString),
                get(IS_RESERVED_URL, VALID_TEST_USER, reservationIdString, VALID_SESSION_ID)));
    }

    private AuthorizationResponse getAuthorizedResponse() {
        return AuthorizationResponse.builder().result(true).build();
    }

    private AuthorizationResponse getUnauthorizedResponse() {
        return AuthorizationResponse.builder().result(false).build();
    }
}
