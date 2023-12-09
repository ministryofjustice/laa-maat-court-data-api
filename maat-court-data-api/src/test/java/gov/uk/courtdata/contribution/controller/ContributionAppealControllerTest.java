package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.contribution.dto.ContributionAppealDTO;
import gov.uk.courtdata.contribution.service.ContributionAppealService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.builder.TestModelDataBuilder.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContributionAppealController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ContributionAppealControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/contribution-appeal";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributionAppealService contributionAppealService;

    @Test
    void givenCorrectParameters_whenGetContributionAmountIsInvoked_thenCorrectContributionAmountIsReturned() throws Exception {
        ContributionAppealDTO contributionAppealDTO = new ContributionAppealDTO(CASE_TYPE, APTY_CODE, OUTCOME, ASSESSMENT_RESULT);
        when(contributionAppealService.getContributionAmount(contributionAppealDTO)).thenReturn(CONTRIBUTION_AMOUNT);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/caty-case-type/" + CASE_TYPE + "/apty-code/"
                        + APTY_CODE + "/cc-outcome/" + OUTCOME + "/assessmentResult/" + ASSESSMENT_RESULT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(CONTRIBUTION_AMOUNT.toString()));
    }

    @Test
    public void givenInvalidAptyCode_whenGetContributionAmountIsInvoked_thenNotFoundIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/caty-case-type/" + CASE_TYPE + "/apty-code/"
                        + "/cc-outcome/" + OUTCOME + "/assessmentResult/" + ASSESSMENT_RESULT))
                .andExpect(status().isNotFound());
    }

}
