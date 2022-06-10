package gov.uk.courtdata.integration.authorization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.authorization.controller.AuthorizationController;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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

        userRolesRepository.saveAllAndFlush(userRoleEntities);

        String ROLE_ACTION_ENABLED = "Y";
        String ROLE_ACTION_DISABLED = "N";
        List<RoleActionEntity> roleActionEntities = List.of(
            RoleActionEntity.builder()
                    .Id(1).roleName(AUTHORISED_ROLE).action(AUTHORISED_ACTION).enabled(ROLE_ACTION_ENABLED).build(),
            RoleActionEntity.builder()
                    .Id(2).roleName(DISABLED_ROLE).action(DISABLED_ACTION).enabled(ROLE_ACTION_DISABLED).build()
        );

        roleActionsRepository.saveAllAndFlush(roleActionEntities);
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
    public void givenMissingUser_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() {
        ValidationException error =
                assertThrows(
                        ValidationException.class,
                        () -> authorizationController.isRoleActionAuthorized("", AUTHORISED_ACTION, LAA_TRANSACTION_ID));

        assertThat(error.getMessage()).contains("Username and action are required");
    }

    @Test
    public void givenMissingAction_whenIsRoleActionAuthorizedIsInvoked_theCorrectErrorIsThrown() {
        ValidationException error =
                assertThrows(
                        ValidationException.class,
                        () -> authorizationController.isRoleActionAuthorized(VALID_TEST_USER, "", LAA_TRANSACTION_ID));

        assertThat(error.getMessage()).contains("Username and action are required");
    }

    private void runRoleActionAuthorizedScenario(String username, String action, Boolean expectedResult) {
        ResponseEntity<AuthorizationResponse> response =
                authorizationController.isRoleActionAuthorized(username, action, LAA_TRANSACTION_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).isResult()).isEqualTo(expectedResult);
    }
}
