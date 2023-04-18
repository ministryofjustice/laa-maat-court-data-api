package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.contribution.service.ContributionAppealService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ContributionAppealController.class)
public class ContributionAppealControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/contribution-appeal";

    private static final String CASE_TYPE = "APPEAL CC";
    private static final String APTY_CODE = "ASE";
    private static final String OUTCOME = "SUCCESSFUL";
    private static final String ASSESSMENT_RESULT = "PASS";
    private static final Integer CONTRIBUTION_AMOUNT = 500;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributionAppealService contributionAppealService;

    @Test
    void greetingShouldReturnMessageFromService() throws Exception {
        when(contributionAppealService.getContributionAmount(CASE_TYPE, APTY_CODE, OUTCOME, ASSESSMENT_RESULT)).thenReturn(CONTRIBUTION_AMOUNT);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/caty-case-type/" + CASE_TYPE + "/apty-code/"
                                + APTY_CODE + "/cc-outcome/" + OUTCOME + "/assessmentResult/" + ASSESSMENT_RESULT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(CONTRIBUTION_AMOUNT.toString()));
    }

}
