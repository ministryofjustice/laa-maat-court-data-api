package gov.uk.courtdata.integration.authorization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.authorization.controller.AuthorizationController;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class AuthorizationControllerIntegrationTest {


    private final String LAA_TRANSACTION_ID = "b27b97e4-0514-42c4-8e09-fcc2c693e11f";
    private final String VALID_TEST_USER = "test-user-valid";
    private final String AUTHORISED_ACTION = "VALID_ACTION";
    private final String DISABLED_ACTION = "DISABLED_ACTION";
    private final String VALID_NWORCODE = "VALID_WORK_REASON";
    private final String MISSING_USER_NAME_OR_NWORCODE_ERROR = "Username and new work reason are required";
    private final String MISSING_USER_NAME_OR_ACTION_ERROR = "Username and action are required";
    private final Integer VALID_RESERVATION_ID = 1234;
    private final String VALID_SESSION_ID = "valid-session";
    private final String MISSING_USER_ATTRIBUTES_ERROR = "User session attributes are missing";

    @Autowired
    private RoleActionsRepository roleActionsRepository;
    @Autowired
    private ReservationsRepository reservationsRepository;
    @Autowired
    private RoleWorkReasonsRepository roleWorkReasonsRepository;
    @Autowired
    private AuthorizationController authorizationController;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
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
    public void givenAValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() {
        runRoleActionAuthorizedScenario(VALID_TEST_USER, AUTHORISED_ACTION, true);
    }

    @Test
    public void givenAValidUserAnInvalidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() {
        runRoleActionAuthorizedScenario(VALID_TEST_USER, "UNKNOWN_ACTION", false);
    }

    @Test
    public void givenAnInvalidUserAndValidRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() {
        runRoleActionAuthorizedScenario("INVALID_USER", AUTHORISED_ACTION, false);
    }

    @Test
    public void givenADisabledRoleAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectResponseIsReturned() {
        runRoleActionAuthorizedScenario(VALID_TEST_USER, DISABLED_ACTION, false);
    }

    @Test
    public void givenMissingUsername_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isRoleActionAuthorized("", AUTHORISED_ACTION, LAA_TRANSACTION_ID),
                MISSING_USER_NAME_OR_ACTION_ERROR);
    }

    @Test
    public void givenMissingAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isRoleActionAuthorized(VALID_TEST_USER, "", LAA_TRANSACTION_ID),
                MISSING_USER_NAME_OR_ACTION_ERROR);
    }

    @Test
    public void givenValidUsernameAndNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() {
        runNewWorkReasonAuthorizedScenario(VALID_TEST_USER, VALID_NWORCODE, true);
    }

    @Test
    public void givenValidUsernameAndInvalidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() {
        runNewWorkReasonAuthorizedScenario(VALID_TEST_USER, "INVALID_WORK_REASON", false);
    }

    @Test
    public void givenAnInvalidUsernameAndValidNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectResponseIsReturned() {
        runNewWorkReasonAuthorizedScenario("invalid-user", VALID_NWORCODE, false);
    }

    @Test
    public void givenMissingNWorCode_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isNewWorkReasonAuthorized(VALID_TEST_USER, "", LAA_TRANSACTION_ID),
                MISSING_USER_NAME_OR_NWORCODE_ERROR);
    }

    @Test
    public void givenMissingUsername_whenIsNewWorkReasonAuthorizedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isNewWorkReasonAuthorized("", VALID_NWORCODE, LAA_TRANSACTION_ID),
                MISSING_USER_NAME_OR_NWORCODE_ERROR);
    }

    @Test
    public void givenAReservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturnedAndTheReservationTimeIsUpdated() {
        runIsReservedScenario(VALID_RESERVATION_ID, true);
    }

    @Test
    public void givenAnUnreservedRecord_whenIsReservedIsInvoked_theCorrectResponseIsReturned() {
        runIsReservedScenario(5678, false);
    }

    @Test
    public void givenAStaleUserSession_whenIsReservedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isReserved(VALID_TEST_USER, VALID_RESERVATION_ID, "Stale-session", LAA_TRANSACTION_ID),
                "Stale user session, reservation not allowed");
    }

    @Test
    public void givenAMissingUsername_whenIsReservedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isReserved("", VALID_RESERVATION_ID, VALID_SESSION_ID, LAA_TRANSACTION_ID),
                MISSING_USER_ATTRIBUTES_ERROR);
    }

    @Test
    public void givenAMissingSessionId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isReserved(VALID_TEST_USER, VALID_RESERVATION_ID, "", LAA_TRANSACTION_ID),
                MISSING_USER_ATTRIBUTES_ERROR);
    }

    @Test
    public void givenAMissingReservationId_whenIsReservedIsInvoked_theCorrectErrorIsThrown() {
        runValidationErrorScenario(
                () -> authorizationController.isReserved(VALID_TEST_USER, null, VALID_SESSION_ID, LAA_TRANSACTION_ID),
                "Reservation attributes are missing");
    }

    private void runRoleActionAuthorizedScenario(String username, String action, Boolean expectedResult) {
        ResponseEntity<AuthorizationResponse> response =
                authorizationController.isRoleActionAuthorized(username, action, LAA_TRANSACTION_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).isResult()).isEqualTo(expectedResult);
    }

    private void runNewWorkReasonAuthorizedScenario(String username, String nWorCode, Boolean expectedResult) {
        ResponseEntity<AuthorizationResponse> response =
                authorizationController.isNewWorkReasonAuthorized(username, nWorCode, LAA_TRANSACTION_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).isResult()).isEqualTo(expectedResult);
    }

    private void runIsReservedScenario(Integer reservationId, Boolean expectedResult) {
        ResponseEntity<Object> response =
                authorizationController.isReserved(VALID_TEST_USER, reservationId, VALID_SESSION_ID, LAA_TRANSACTION_ID);
        AuthorizationResponse responseBody = (AuthorizationResponse) response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.isResult()).isEqualTo(expectedResult);
    }

    private void runValidationErrorScenario(ThrowingRunnable throwingRunnable, String expectedErrorMessage) {
        ValidationException error = assertThrows(ValidationException.class, throwingRunnable);
        assertThat(error.getMessage()).contains(expectedErrorMessage);
    }
}
