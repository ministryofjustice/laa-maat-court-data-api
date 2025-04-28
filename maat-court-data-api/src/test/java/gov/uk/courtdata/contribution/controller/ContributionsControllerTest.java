package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.service.ContributionsService;
import gov.uk.courtdata.contribution.validator.CreateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContributionsController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContributionsControllerTest {

    private static final String endpointUrl = "/api/internal/v1/assessment/contributions";
    private static final Integer TEST_CONTRIBUTIONS_ID = 999;
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private ContributionsService contributionsService;
    @MockitoBean
    private CreateContributionsValidator createContributionsValidator;

    @Test
    void givenAValidParameter_whenFindIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(TEST_CONTRIBUTIONS_ID).build();
        when(contributionsService.find(TEST_CONTRIBUTIONS_ID, false)).thenReturn(List.of(contributionsDTO));

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + TEST_CONTRIBUTIONS_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TEST_CONTRIBUTIONS_ID));
    }

    @Test
    void givenIncorrectParameters_whenFindIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/null"))
                .andExpect(status().isBadRequest());
    }
    

    @Test
    void givenAValidContent_whenCreateIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getCreateContributionsJson();
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(TEST_CONTRIBUTIONS_ID).build();
        when(createContributionsValidator.validate(any(CreateContributionRequest.class))).thenReturn(Optional.empty());
        when(contributionsService.create(any(CreateContributionRequest.class))).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TEST_CONTRIBUTIONS_ID));
    }

    @Test
    void givenIncorrectPayload_whenCreateIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getInvalidCreateContributionsJson();
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidRepId_whenGetContributionSummaryIsInvoked_thenOkResponseIsReturned() throws Exception {
        List<ContributionsSummaryDTO> contributionsSummaryDTOs = List.of(ContributionsSummaryDTO.builder()
                .id(TEST_CONTRIBUTIONS_ID).build());
        when(contributionsService.getContributionsSummary(TEST_CONTRIBUTIONS_ID)).thenReturn(contributionsSummaryDTOs);

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + TEST_CONTRIBUTIONS_ID + "/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TEST_CONTRIBUTIONS_ID));
    }

    @Test
    void givenInvalidRepId_whenGetContributionsSummaryIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        String invalidRepId = "INVALID";

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + invalidRepId + "/summary"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(String.format(
                        "The provided value '%s' is the incorrect type for the 'repId' parameter.", invalidRepId)));
    }

    @Test
    void givenNoContributionsForRepId_whenGetContributionsSummaryIsInvoked_thenNotFoundResponseIsReturned() throws Exception {
        String exceptionMessage = "Test not found exception";
        when(contributionsService.getContributionsSummary(TEST_CONTRIBUTIONS_ID))
                .thenThrow(new RequestedObjectNotFoundException(exceptionMessage));

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + TEST_CONTRIBUTIONS_ID + "/summary"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(exceptionMessage));
    }

    @Test
    void givenDatabaseIsDown_whenGetContributionSummaryIsInvoked_thenInternalServerErrorResponseIsReturned() throws Exception {
        String exceptionMessage = "Test maat court data exception";
        when(contributionsService.getContributionsSummary(TEST_CONTRIBUTIONS_ID))
                .thenThrow(new MAATCourtDataException(exceptionMessage));

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + TEST_CONTRIBUTIONS_ID + "/summary"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(exceptionMessage));
    }

}
