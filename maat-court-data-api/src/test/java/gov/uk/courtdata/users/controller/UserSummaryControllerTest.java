package gov.uk.courtdata.users.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.users.service.UserSummaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserSummaryController.class)
public class UserSummaryControllerTest {
    private static final String ENDPOINT_URL = "/api/internal/v1/users/summary/";
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
        when(userSummaryService.getUserSummary(TestModelDataBuilder.TEST_USER))
                .thenReturn(TestModelDataBuilder.getUserSummaryDTO());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + TestModelDataBuilder.TEST_USER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(TestModelDataBuilder.TEST_USER));
    }


}
