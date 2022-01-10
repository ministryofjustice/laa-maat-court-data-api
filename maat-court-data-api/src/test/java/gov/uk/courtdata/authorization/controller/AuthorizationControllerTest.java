package gov.uk.courtdata.authorization.controller;

import gov.uk.courtdata.authorization.service.AuthorizationService;
import gov.uk.courtdata.exception.ValidationException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizationController.class)
@Ignore
public class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorizationService authorizationService;

    private final String baseURL = "/authorization";

    private String getRoleActionUrl(String action) {
        return String.format(baseURL + "/users/test-f/validation/action/%s", action);
    }

    private String getWorkReasonUrl(String nworCode) {
        return String.format(baseURL + "/users/test-f/validation/nwor/%s", nworCode);
    }

    @Test
    public void givenCorrectParameters_whenIsRoleActionAuthorizedIsInvoked_thenResultIsReturned() throws Exception {
        when(authorizationService.isRoleActionAuthorized(any(), any())).thenReturn(Boolean.TRUE);
        mvc.perform(MockMvcRequestBuilders.get(getRoleActionUrl("CREATE_ASSESSMENT")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("true"));

        when(authorizationService.isRoleActionAuthorized(any(), any())).thenReturn(Boolean.FALSE);
        mvc.perform(MockMvcRequestBuilders.get(getRoleActionUrl("CREATE_ASSESSMENT")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("false"));
    }

    @Test
    public void givenIncorrectParameters_whenIsRoleActionAuthorizedIsInvoked_thenReturn400ClientError() throws Exception {
        when(authorizationService.isRoleActionAuthorized(any(), any())).thenThrow(
                new ValidationException("Username and action are required")
        );

        mvc.perform(MockMvcRequestBuilders.get(getRoleActionUrl("FAKE_ACTION")))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Username and action are required"));
    }

    @Test
    public void givenCorrectParameters_whenIsNewWorkReasonAuthorizedIsInvoked_thenResultIsReturned() throws Exception {
        when(authorizationService.isNewWorkReasonAuthorized(any(), any())).thenReturn(Boolean.TRUE);
        mvc.perform(MockMvcRequestBuilders.get(getWorkReasonUrl("FMA")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("true"));

        when(authorizationService.isNewWorkReasonAuthorized(any(), any())).thenReturn(Boolean.FALSE);
        mvc.perform(MockMvcRequestBuilders.get(getWorkReasonUrl("FMA")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value("false"));
    }

    @Test
    public void givenIncorrectParameters_IsNewWorkReasonAuthorizedIsInvoked_thenReturn400ClientError() throws Exception {
        when(authorizationService.isNewWorkReasonAuthorized(any(), any())).thenThrow(
                new ValidationException("Username and new work reason are required")
        );

        mvc.perform(MockMvcRequestBuilders.get(getWorkReasonUrl("FAKE_WORK_REASON")))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Username and new work reason are required"));
    }
}

