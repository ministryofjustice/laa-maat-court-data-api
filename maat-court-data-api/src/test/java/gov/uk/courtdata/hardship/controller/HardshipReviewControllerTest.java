package gov.uk.courtdata.hardship.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hardship.service.HardshipReviewService;
import gov.uk.courtdata.hardship.validator.HardshipReviewValidationProcessor;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReview;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(HardshipReviewController.class)
public class HardshipReviewControllerTest {

    private static final int MOCK_REP_ID = 621580;
    private static final int INVALID_REP_ID = 3456;
    private static final Integer MOCK_HARDSHIP_ID = 1000;
    private static final String ENDPOINT_URL = "api/internal/v1/assessments/hardship";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private HardshipReviewService hardshipReviewService;
    @MockBean
    private HardshipReviewValidationProcessor hardshipReviewValidationProcessor;

    @Test
    public void givenCorrectParameters_whenGetHardshipIsInvoked_thenHardshipIsReturned() throws Exception {

        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();

        when(hardshipReviewService.findHardshipReview(MOCK_HARDSHIP_ID)).thenReturn(hardshipReviewDTO);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + MOCK_HARDSHIP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_HARDSHIP_ID)))
                .andExpect(jsonPath("$.reviewDetails[0].id").value(String.valueOf(4253)))
                .andExpect(jsonPath("$.reviewProgressItems[0].id").value(String.valueOf(1254)));
    }

    @Test
    public void givenIncorrectParameters_whenGetHardshipIsInvoked_then4xxIsThrown() throws Exception {
        when(hardshipReviewValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        when(hardshipReviewService.findHardshipReview(MOCK_HARDSHIP_ID)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/" + MOCK_HARDSHIP_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenGetHardshipByRepIdIsInvoked_thenHardshipReviewIsReturned() throws Exception {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTO();
        when(hardshipReviewService.findHardshipReviewByRepId(MOCK_REP_ID)).thenReturn(hardshipReviewDTO);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + MOCK_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_HARDSHIP_ID)));

        verify(hardshipReviewService).findHardshipReviewByRepId(MOCK_REP_ID);
    }

    @Test
    public void givenInvalidRepId_whenGetHardshipByRepIdIsInvoked_then404NotFoundErrorIsThrown() throws Exception {
        when(hardshipReviewService.findHardshipReviewByRepId(INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException(String.format("Hardship Review with repId %s not found", INVALID_REP_ID)));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + INVALID_REP_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNullRepId_whenGetHardshipByRepIdIsInvoked_thenBadRequestIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCorrectParameters_whenCreateHardshipIsInvoked_thenHardshipIsPersisted() throws Exception {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        String requestJson = TestModelDataBuilder.getCreateHardshipReviewJson(true);

        when(hardshipReviewService.createHardshipReview(any(CreateHardshipReview.class))).thenReturn(hardshipReviewDTO);
        when(hardshipReviewValidationProcessor.validate(any(HardshipReview.class))).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.reviewDetails[0].id").value(String.valueOf(4253)))
                .andExpect(jsonPath("$.reviewProgressItems[0].id").value(String.valueOf(1254)));
    }

    @Test
    public void givenIncorrectParameters_whenCreateHardshipIsInvoked_then4xxIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenUpdateHardshipIsInvoked_thenHardshipIsPersisted() throws Exception {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        String requestJson = TestModelDataBuilder.getUpdateHardshipReviewJson(true);

        when(hardshipReviewService.updateHardshipReview(any(UpdateHardshipReview.class))).thenReturn(hardshipReviewDTO);
        when(hardshipReviewValidationProcessor.validate(any(HardshipReview.class))).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.reviewDetails[0].id").value(String.valueOf(4253)))
                .andExpect(jsonPath("$.reviewProgressItems[0].id").value(String.valueOf(1254)));
    }

    @Test
    public void givenIncorrectParameters_whenUpdateHardshipIsInvoked_then4xxIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
