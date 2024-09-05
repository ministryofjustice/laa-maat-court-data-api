package gov.uk.courtdata.integration.users;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.FeatureToggleEntity;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.entity.RoleActionEntity;
import gov.uk.courtdata.entity.RoleDataItemEntity;
import gov.uk.courtdata.entity.RoleWorkReasonEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.entity.UserRoleEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class UserSummaryControllerIntegrationTest extends MockMvcIntegrationTest {

  private final String VALID_TEST_USER = "test-user-valid";
  private final String INVALID_TEST_USER = "test-user-invalid";
  private final String AUTHORISED_ACTION = "VALID_ACTION";
  private final String DISABLED_ACTION = "DISABLED_ACTION";
  private final String VALID_NWORCODE = "VALID_WORK_REASON";
  private final Integer VALID_RESERVATION_ID = 1234;
  private final String VALID_SESSION_ID = "valid-session";
  private final String BASE_URL = "/api/internal/v1/users/";
  private final String GET_USER_SUMMARY_URL = BASE_URL + "summary/{username}";
  private final String AUTHORISED_ROLE = "VALID_ROLE";

  @Autowired
  MockMvc mvc;

  @BeforeEach
  public void setUp() throws Exception {
    setupTestData();
  }

  private void setupTestData() {
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
            .Id(1).roleName(AUTHORISED_ROLE).action(AUTHORISED_ACTION).enabled(ROLE_ACTION_ENABLED)
            .build(),
        RoleActionEntity.builder()
            .Id(2).roleName(DISABLED_ROLE).action(DISABLED_ACTION).enabled(ROLE_ACTION_DISABLED)
            .build()
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

    repos.roleDataItems.save(RoleDataItemEntity.builder()
        .id(1)
        .roleName(AUTHORISED_ROLE)
        .dataItem("DATA_ITEM")
        .enabled("Y")
        .insertAllowed("Y")
        .updateAllowed("Y")
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

    repos.featureToggleRepository.save(FeatureToggleEntity.builder()
        .id(1)
        .username(VALID_TEST_USER)
        .featureName("Test feature")
        .action("Create")
        .build());

    repos.user.save(UserEntity.builder()
        .username(VALID_TEST_USER)
        .currentSession(VALID_SESSION_ID)

        .build());
  }

  @Test
  public void givenEmptyUsername_whenGetUserSummaryIsInvoked_theCorrectErrorIsThrown()
      throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/summary/")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void givenAValidUsername_whenGetUserSummaryIsInvoked_thenCorrectResponseIsReturned()
      throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(GET_USER_SUMMARY_URL, VALID_TEST_USER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.roleActions[0]").value(DISABLED_ACTION))
        .andExpect(jsonPath("$.newWorkReasons[0]").value(VALID_NWORCODE))
        .andExpect(jsonPath("$.reservationsDTO.recordId").value(VALID_RESERVATION_ID))
        .andExpect(jsonPath("$.roleDataItem[0].roleName").value(AUTHORISED_ROLE))
        .andExpect(jsonPath("$.featureToggle[0].username").value(VALID_TEST_USER))
        .andExpect(jsonPath("$.username").value(VALID_TEST_USER));
  }

  @Test
  public void givenInValidUsername_whenGetUserSummaryIsInvoked_thenCorrectResponseIsReturned()
      throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(GET_USER_SUMMARY_URL, INVALID_TEST_USER))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.roleActions").isEmpty())
        .andExpect(jsonPath("$.newWorkReasons").isEmpty())
        .andExpect(jsonPath("$.reservationsDTO").doesNotExist())
        .andExpect(jsonPath("$.featureToggle").doesNotExist())
        .andExpect(jsonPath("$.username").value(INVALID_TEST_USER));

  }
}
