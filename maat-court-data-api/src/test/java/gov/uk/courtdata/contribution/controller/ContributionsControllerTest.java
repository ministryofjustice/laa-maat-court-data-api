package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.contribution.service.ContributionsService;
import gov.uk.courtdata.contribution.validator.CreateContributionsValidator;
import gov.uk.courtdata.contribution.validator.UpdateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContributionsController.class)
class ContributionsControllerTest {

    private static final String endpointUrl = "/api/internal/v1/assessment/contributions";
    private static Integer TEST_CONTRIBUTIONS_ID = 999;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ContributionsService contributionsService;
    @MockBean
    private UpdateContributionsValidator updateContributionsValidator;
    @MockBean
    private CreateContributionsValidator createContributionsValidator;

    @MockBean
    private MaatIdValidator validator;

    @Test
    void givenAValidParameter_whenFindIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(TEST_CONTRIBUTIONS_ID).build();
        when(contributionsService.find(TEST_CONTRIBUTIONS_ID)).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + TEST_CONTRIBUTIONS_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TEST_CONTRIBUTIONS_ID));
    }

    @Test
    void givenIncorrectParameters_whenFindIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidContent_whenUpdateIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getUpdateContributionsJson();
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(TEST_CONTRIBUTIONS_ID).build();
        when(updateContributionsValidator.validate(any(UpdateContributions.class))).thenReturn(Optional.empty());
        when(contributionsService.update(any(UpdateContributions.class))).thenReturn(contributionsDTO);

        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TEST_CONTRIBUTIONS_ID));
    }

    @Test
    void givenIncorrectPayload_whenUpdateIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getInvalidUpdateContributionsJson();

        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(contributionsJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidContent_whenCreateIsInvoked_thenOKResponseWithContributionsEntryIsReturned() throws Exception {
        String contributionsJson = TestModelDataBuilder.getCreateContributionsJson();
        ContributionsDTO contributionsDTO = ContributionsDTO.builder().id(TEST_CONTRIBUTIONS_ID).build();
        when(createContributionsValidator.validate(any(CreateContributions.class))).thenReturn(Optional.empty());
        when(contributionsService.create(any(CreateContributions.class))).thenReturn(contributionsDTO);

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
    void givenAValidRepId_whenGetContributionCountIsInvoked_thenReturnContributionCount() throws Exception {
        when(validator.validate(TestModelDataBuilder.REP_ID)).thenReturn(Optional.empty());
        when(contributionsService.getContributionCount(TestModelDataBuilder.REP_ID)).thenReturn(1);
        mvc.perform(MockMvcRequestBuilders.head(endpointUrl + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

    @Test
    void givenAInValidRepId_whenGetContributionCountIsInvoked_thenReturnContributionCount() throws Exception {
        when(validator.validate(TestModelDataBuilder.REP_ID)).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
