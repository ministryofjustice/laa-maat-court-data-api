package gov.uk.courtdata.contributions.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contributions.service.ContributionsService;
import gov.uk.courtdata.contributions.validator.CreateContributionsValidator;
import gov.uk.courtdata.contributions.validator.UpdateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import org.junit.Test;
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
public class ContributionsControllerTest {

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
    public void whenFindIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(Integer.valueOf(TEST_CONTRIBUTIONS_ID)).build();
        when(contributionsService.find(any())).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + TEST_CONTRIBUTIONS_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Integer.valueOf(TEST_CONTRIBUTIONS_ID)));
    }

    @Test
    public void givenIncorrectParameters_whenFindIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception{
        String contributionsJson = TestModelDataBuilder.getUpdateContributionsJson();
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(Integer.valueOf(TEST_CONTRIBUTIONS_ID)).build();
        when(updateContributionsValidator.validate(any())).thenReturn(Optional.empty());
        when(contributionsService.update(any())).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Integer.valueOf(TEST_CONTRIBUTIONS_ID)));
    }

    @Test
    public void givenIncorrectPayload_whenUpdateIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getInvalidUpdateContributionsJson();

        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getCreateContributionsJson();
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(Integer.valueOf(TEST_CONTRIBUTIONS_ID)).build();
        when(createContributionsValidator.validate(any())).thenReturn(Optional.empty());
        when(contributionsService.create(any())).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Integer.valueOf(TEST_CONTRIBUTIONS_ID)));
    }

    @Test
    public void givenIncorrectPayload_whenCreateIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getInvalidCreateContributionsJson();

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
