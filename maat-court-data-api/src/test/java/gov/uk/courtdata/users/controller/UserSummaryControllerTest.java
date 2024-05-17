package gov.uk.courtdata.users.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.users.service.UserSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.builder.TestModelDataBuilder.TEST_USER;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserSummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserSummaryControllerTest {
    private static final String ENDPOINT_URL = "/api/internal/v1/users/summary/";
    private static final String USER_ENDPOINT_URL = "/api/internal/v1/users/";
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserSummaryService userSummaryService;

    @Test
    void givenIncorrectParameters_whenUpdateCCOutcomeIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenGetUserSummaryIsInvoked_thenReturnStatusOK() throws Exception {
        when(userSummaryService.getUserSummary(TEST_USER))
                .thenReturn(TestModelDataBuilder.getUserSummaryDTO());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + TEST_USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(TEST_USER));
    }

    @Test
    void givenIncorrectParameters_whenCreateUserIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenValidParameters_whenCreateUserIsInvoked_thenReturnStatusOK() throws Exception {
        String requestJson = """
                {
                  "username" : "test-f",
                  "loggedIn" : "Y",
                  "currentSession" : "mock-session"
                }
                """;

        mvc.perform(MockMvcRequestBuilders.post(USER_ENDPOINT_URL).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidParameters_whenGetUserIsInvoked_thenReturnStatusOK() throws Exception {
        when(userSummaryService.getUser(TEST_USER))
                .thenReturn(UserEntity.builder().username(TEST_USER).build());
        mvc.perform(MockMvcRequestBuilders.get(USER_ENDPOINT_URL + TEST_USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(TEST_USER));
    }

    @Test
    void givenIncorrectParameters_whenPatchUserIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(USER_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenValidParameters_whenPatchUserIsInvoked_thenReturnStatusOK() throws Exception {
        when(userSummaryService.getUser(TEST_USER))
                .thenReturn(UserEntity.builder().username(TEST_USER).build());
        String requestJson = """
                {
                  "loggedIn" : "Y",
                  "currentSession" : "mock-session"
                }
                """;
        mvc.perform(MockMvcRequestBuilders.patch(USER_ENDPOINT_URL + TEST_USER).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenIncorrectParameters_whenDeleteUserIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(USER_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenValidParameters_whenDeleteUserIsInvoked_thenReturnStatusOK() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(USER_ENDPOINT_URL + TEST_USER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
