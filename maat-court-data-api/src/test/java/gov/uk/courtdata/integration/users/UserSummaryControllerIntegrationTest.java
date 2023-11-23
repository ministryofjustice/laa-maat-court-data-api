package gov.uk.courtdata.integration.users;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import gov.uk.courtdata.util.RepositoryUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @Autowired
    MockMvc mvc;
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

    @BeforeEach
    public void setUp() throws Exception {
        setupTestData();
    }

    private void setupTestData() {
        RepositoryUtil.clearUp(roleActionsRepository,
                reservationsRepository,
                roleWorkReasonsRepository,
                userRolesRepository,
                userRepository);

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

    @AfterEach
    void clearUp() {
        RepositoryUtil.clearUp(roleActionsRepository,
                reservationsRepository,
                roleWorkReasonsRepository,
                userRolesRepository,
                userRepository);
    }

    @Test
    public void givenEmptyUsername_whenGetUserSummaryIsInvoked_theCorrectErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/summary/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAValidUsername_whenGetUserSummaryIsInvoked_thenCorrectResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(GET_USER_SUMMARY_URL, VALID_TEST_USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roleActions[0]").value(DISABLED_ACTION))
                .andExpect(jsonPath("$.newWorkReasons[0]").value(VALID_NWORCODE))
                .andExpect(jsonPath("$.reservationsEntity.recordId").value(VALID_RESERVATION_ID))
                .andExpect(jsonPath("$.username").value(VALID_TEST_USER));
    }

    @Test
    public void givenInValidUsername_whenGetUserSummaryIsInvoked_thenCorrectResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(GET_USER_SUMMARY_URL, INVALID_TEST_USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roleActions").isEmpty())
                .andExpect(jsonPath("$.newWorkReasons").isEmpty())
                .andExpect(jsonPath("$.reservationsEntity").isEmpty())
                .andExpect(jsonPath("$.username").value(INVALID_TEST_USER));

    }
}
