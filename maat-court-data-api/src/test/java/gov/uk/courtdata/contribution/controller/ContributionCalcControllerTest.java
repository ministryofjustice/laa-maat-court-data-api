package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.dto.ContributionCalcParametersDTO;
import gov.uk.courtdata.contribution.service.ContributionCalcService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.builder.TestModelDataBuilder.EFFECTIVE_DATE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributionCalcController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContributionCalcControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/contribution-calc-params/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContributionCalcService contributionCalcService;

    @Test
    void givenValidEffectiveDate_whenGetContributionCalcParametersIsInvoked_thenContributionCalcParametersDTOIsReturned() throws Exception {
        ContributionCalcParametersDTO expected = TestModelDataBuilder.getContributionCalcParametersDTO();
        when(contributionCalcService.getContributionCalcParameters(EFFECTIVE_DATE)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + EFFECTIVE_DATE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalMonths").value(expected.getTotalMonths()));
    }

    @Test
    void givenInvalidEffectiveDate_whenGetContributionCalcParametersIsInvoked_thenNotFoundIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
                .andExpect(status().isNotFound());
    }

}
