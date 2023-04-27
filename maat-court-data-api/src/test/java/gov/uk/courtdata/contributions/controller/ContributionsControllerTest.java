package gov.uk.courtdata.contributions.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contributions.service.ContributionsService;
import gov.uk.courtdata.contributions.validator.CreateContributionsValidator;
import gov.uk.courtdata.contributions.validator.UpdateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContributionsController.class)
class ContributionsControllerTest {

    private static final String TEST_CONTRIBUTIONS_ID = "999";
    private static final String endpointUrl = "/api/internal/v1/assessment/contributions";

    @Autowired
    MockMvc mvc;
    @MockBean
    ContributionsService contributionsService;
    @MockBean
    UpdateContributionsValidator updateContributionsValidator;
    @MockBean
    CreateContributionsValidator createContributionsValidator;

    @Test
    void whenFindIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(Integer.valueOf(TEST_CONTRIBUTIONS_ID)).build();
        when(contributionsService.find(anyInt())).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + TEST_CONTRIBUTIONS_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Integer.valueOf(TEST_CONTRIBUTIONS_ID)));
    }

    @Test
    void givenIncorrectParameters_whenFindIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception{
        String contributionsJson = TestModelDataBuilder.getUpdateContributionsJson();
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(Integer.valueOf(TEST_CONTRIBUTIONS_ID)).build();
        when(updateContributionsValidator.validate(any(UpdateContributions.class))).thenReturn(Optional.empty());
        when(contributionsService.update(any(UpdateContributions.class))).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Integer.valueOf(TEST_CONTRIBUTIONS_ID)));
    }

    @Test
    void givenIncorrectPayload_whenUpdateIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getInvalidUpdateContributionsJson();

        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreateIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getCreateContributionsJson();
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(Integer.valueOf(TEST_CONTRIBUTIONS_ID)).build();
        when(createContributionsValidator.validate(any(CreateContributions.class))).thenReturn(Optional.empty());
        when(contributionsService.create(any(CreateContributions.class))).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Integer.valueOf(TEST_CONTRIBUTIONS_ID)));
    }

    @Test
    void givenIncorrectPayload_whenCreateIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getInvalidCreateContributionsJson();

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
