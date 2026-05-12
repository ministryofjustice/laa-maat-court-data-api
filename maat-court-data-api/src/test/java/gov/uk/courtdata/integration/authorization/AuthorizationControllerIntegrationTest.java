package gov.uk.courtdata.integration.authorization;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static org.assertj.core.api.Assertions.assertThat;
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
class AuthorizationControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String VALID_TEST_USER = "test-user-valid";
    private static final String AUTHORISED_ACTION = "VALID_ACTION";
    private static final String DISABLED_ACTION = "DISABLED_ACTION";
    private static final String VALID_NWORCODE = "VALID_WORK_REASON";
    private static final String MISSING_USER_NAME_OR_NWORCODE_ERROR = "Username and new work reason are required";
    private static final String MISSING_USER_NAME_OR_ACTION_ERROR = "Username and action are required";
    private static final Integer VALID_RESERVATION_ID = 1234;
    private static final String VALID_SESSION_ID = "valid-session";
    private static final String MISSING_USER_ATTRIBUTES_ERROR = "User session attributes are missing";
    private static final String BASE_URL = "/api/internal/v1/assessment/authorization/users/";
    private static final String ROLE_ACTION_AUTHORIZED_URL = BASE_URL + "{username}/actions/{action}";
    private static final String NEW_WORK_REASON_AUTHORIZED_URL = BASE_URL + "{username}/work-reasons/{nworCode}";
    private static final String IS_RESERVED_URL =
            BASE_URL + "{username}/reservations/{reservationId}/sessions/{sessionId}";

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    private void setupTestData() {
        String authorisedRole = "VALID_ROLE";
        String disabledRole = "DISABLED_ROLE";
        List<UserRoleEntity> userRoleEntities = List.of(
                UserRoleEntity.builder()
                        .username(VALID_TEST_USER)
                        .roleName(authorisedRole)
                        .build(),
                UserRoleEntity.builder()
                        .username(VALID_TEST_USER)
                        .roleName(disabledRole)
                        .build());

        repos.userRoles.saveAll(userRoleEntities);

        List<RoleActionEntity> roleActionEntities = List.of(
                RoleActionEntity.builder()
                        .Id(1)
                        .roleName(authorisedRole)
                        .action(AUTHORISED_ACTION)
                        .enabled(YES)
                        .build(),
                RoleActionEntity.builder()
                        .Id(2)
                        .roleName(disabledRole)
                        .action(DISABLED_ACTION)
                        .enabled(NO)
                        .build());

        repos.roleActions.saveAll(roleActionEntities);

        repos.roleWorkReasons.save(RoleWorkReasonEntity.builder()
                .id(1)
                .roleName(authorisedRole)
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
    void givenAValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runSuccessScenario(
                        getAuthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, AUTHORISED_ACTION)))
                .isTrue();
    }

    @Test
    void givenAValidUserAnInvalidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        assertThat(runSuccessScenario(
                        getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, "UNKNOWN_ACTION")))
                .isTrue();
    }

    @Test
    void givenAnInvalidUserAndValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        assertThat(runSuccessScenario(
                        getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, "INVALID_USER", AUTHORISED_ACTION)))
                .isTrue();
    }

    @Test
    void givenADisabledRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runSuccessScenario(
                        getUnauthorizedResponse(), get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, DISABLED_ACTION)))
                .isTrue();
    }

    @Test
    void givenMissingUsername_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        MISSING_USER_NAME_OR_ACTION_ERROR, get(ROLE_ACTION_AUTHORIZED_URL, " ", AUTHORISED_ACTION)))
                .isTrue();
    }

    @Test
    void givenMissingAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        MISSING_USER_NAME_OR_ACTION_ERROR, get(ROLE_ACTION_AUTHORIZED_URL, VALID_TEST_USER, " ")))
                .isTrue();
    }

    @Test
    void givenValidUsernameAndNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        assertThat(runSuccessScenario(
                        getAuthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, VALID_NWORCODE)))
                .isTrue();
    }

    @Test
    void givenValidUsernameAndInvalidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        assertThat(runSuccessScenario(
                        getUnauthorizedResponse(),
                        get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, "INVALID_WORK_REASON")))
                .isTrue();
    }

    @Test
    void givenAnInvalidUsernameAndValidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        assertThat(runSuccessScenario(
                        getUnauthorizedResponse(), get(NEW_WORK_REASON_AUTHORIZED_URL, "invalid-user", VALID_NWORCODE)))
                .isTrue();
    }

    @Test
    void givenMissingNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        MISSING_USER_NAME_OR_NWORCODE_ERROR, get(NEW_WORK_REASON_AUTHORIZED_URL, VALID_TEST_USER, " ")))
                .isTrue();
    }

    @Test
    void givenMissingUsername_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        MISSING_USER_NAME_OR_NWORCODE_ERROR, get(NEW_WORK_REASON_AUTHORIZED_URL, " ", VALID_NWORCODE)))
                .isTrue();
    }

    @Test
    void givenAReservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturnedAndTheReservationTimeIsUpdated()
            throws Exception {
        assertThat(runSuccessScenario(
                        getAuthorizedResponse(),
                        get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, VALID_SESSION_ID)))
                .isTrue();
    }

    @Test
    void givenAnUnreservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runSuccessScenario(
                        getUnauthorizedResponse(), get(IS_RESERVED_URL, VALID_TEST_USER, 5678, VALID_SESSION_ID)))
                .isTrue();
    }

    @Test
    void givenAStaleUserSession_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        "Stale user session, reservation not allowed",
                        get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, "Stale-session")))
                .isTrue();
    }

    @Test
    void givenAMissingUsername_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        MISSING_USER_ATTRIBUTES_ERROR,
                        get(IS_RESERVED_URL, " ", VALID_RESERVATION_ID, VALID_SESSION_ID)))
                .isTrue();
    }

    @Test
    void givenAMissingSessionId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        MISSING_USER_ATTRIBUTES_ERROR,
                        get(IS_RESERVED_URL, VALID_TEST_USER, VALID_RESERVATION_ID, " ")))
                .isTrue();
    }

    @Test
    void givenAStringTypeReservationId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() throws Exception {
        String reservationIdString = "incorrect-type";

        assertThat(runBadRequestErrorScenario(
                        String.format(
                                "The provided value '%s' is the incorrect type for the 'reservationId' parameter.",
                                reservationIdString),
                        get(IS_RESERVED_URL, VALID_TEST_USER, reservationIdString, VALID_SESSION_ID)))
                .isTrue();
    }

    private AuthorizationResponse getAuthorizedResponse() {
        return AuthorizationResponse.builder().result(true).build();
    }

    private AuthorizationResponse getUnauthorizedResponse() {
        return AuthorizationResponse.builder().result(false).build();
    }
}
