package gov.uk.courtdata.hardship.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.HardshipReviewDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hardship.service.HardshipReviewService;
import gov.uk.courtdata.hardship.validator.HardshipReviewValidationProcessor;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HardshipReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class HardshipReviewControllerTest {

    private static final int MOCK_REP_ID = 621580;
    private static final int INVALID_REP_ID = 3456;
    private static final String MOCK_DETAIL_TYPE = "EXPENDITURE";
    private static final Integer MOCK_HARDSHIP_ID = 1000;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/hardship";
    private static final String PATCH_ENDPOINT_URL = ENDPOINT_URL.concat("/{hardshipReviewId}");
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private HardshipReviewService hardshipReviewService;
    @MockitoBean
    private HardshipReviewValidationProcessor hardshipReviewValidationProcessor;

    @Test
    void givenCorrectParameters_whenGetHardshipIsInvoked_thenHardshipIsReturned() throws Exception {

        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();

        when(hardshipReviewService.find(MOCK_HARDSHIP_ID)).thenReturn(hardshipReviewDTO);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + MOCK_HARDSHIP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_HARDSHIP_ID)))
                .andExpect(jsonPath("$.reviewDetails[0].id").value(String.valueOf(TestModelDataBuilder.MOCK_HRD_ID)))
                .andExpect(jsonPath("$.reviewProgressItems[0].id").value(String.valueOf(1254)));
    }

    @Test
    void givenIncorrectParameters_whenGetHardshipIsInvoked_then4xxIsThrown() throws Exception {
        when(hardshipReviewValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        when(hardshipReviewService.find(MOCK_HARDSHIP_ID)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/" + MOCK_HARDSHIP_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenCorrectParameters_whenGetHardshipByRepIdIsInvoked_thenHardshipReviewIsReturned() throws Exception {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTO();
        when(hardshipReviewService.findByRepId(MOCK_REP_ID)).thenReturn(hardshipReviewDTO);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + MOCK_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_HARDSHIP_ID)));

        verify(hardshipReviewService).findByRepId(MOCK_REP_ID);
    }

    @Test
    void givenInvalidRepId_whenGetHardshipByRepIdIsInvoked_then404NotFoundErrorIsThrown() throws Exception {
        when(hardshipReviewService.findByRepId(INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException(String.format("Hardship Review with repId %s not found", INVALID_REP_ID)));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + INVALID_REP_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNullRepId_whenGetHardshipByRepIdIsInvoked_thenBadRequestIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenGetHardshipByDetailTypeIsInvoked_thenHardshipReviewIsReturned() throws Exception {
        HardshipReviewDetail hardshipReviewDetail = TestModelDataBuilder.getHardshipReviewDetail();
        when(hardshipReviewService.findDetails(MOCK_DETAIL_TYPE, MOCK_REP_ID)).thenReturn(List.of(hardshipReviewDetail));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + MOCK_REP_ID + "/detailType/" + MOCK_DETAIL_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(String.valueOf(TestModelDataBuilder.MOCK_HRD_ID)));

        verify(hardshipReviewService).findDetails(MOCK_DETAIL_TYPE, MOCK_REP_ID);
    }

    @Test
    void givenInvalidRepId_whenGetHardshipByDetailTypeIsInvoked_then404NotFoundErrorIsThrown() throws Exception {
        when(hardshipReviewService.findDetails(MOCK_DETAIL_TYPE, INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException(String.format("Hardship Review with detail type %s and repId %d not found", MOCK_DETAIL_TYPE, INVALID_REP_ID)));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + INVALID_REP_ID + "/detailType/" + MOCK_DETAIL_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNullRepId_whenGetHardshipByDetailTypeIsInvoked_thenBadRequestIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/null/detailType/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenCreateHardshipIsInvoked_thenHardshipIsPersisted() throws Exception {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        String requestJson = TestModelDataBuilder.getCreateHardshipReviewJson(true);

        when(hardshipReviewService.create(any(CreateHardshipReview.class))).thenReturn(hardshipReviewDTO);

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.reviewDetails[0].id").value(String.valueOf(TestModelDataBuilder.MOCK_HRD_ID)))
                .andExpect(jsonPath("$.reviewProgressItems[0].id").value(String.valueOf(1254)));
    }

    @Test
    void givenIncorrectParameters_whenCreateHardshipIsInvoked_then4xxIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenCorrectParameters_whenUpdateHardshipIsInvoked_thenHardshipIsPersisted() throws Exception {
        HardshipReviewDTO hardshipReviewDTO = TestModelDataBuilder.getHardshipReviewDTOWithRelationships();
        String requestJson = TestModelDataBuilder.getUpdateHardshipReviewJson(true);

        when(hardshipReviewService.update(any(UpdateHardshipReview.class))).thenReturn(hardshipReviewDTO);

        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.reviewDetails[0].id").value(String.valueOf(TestModelDataBuilder.MOCK_HRD_ID)))
                .andExpect(jsonPath("$.reviewProgressItems[0].id").value(String.valueOf(1254)));
    }

    @Test
    void givenIncorrectParameters_whenUpdateHardshipIsInvoked_then4xxIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenCorrectParameters_whenPatchIsInvoked_thenHardshipIsUpdated() throws Exception {
        String requestJson = "{\"solicitorHours\" : 2.0}";

        doNothing().when(hardshipReviewService).patch(MOCK_HARDSHIP_ID, Map.of("solicitorHours", "2.0"));

        mvc.perform(MockMvcRequestBuilders.patch(PATCH_ENDPOINT_URL, MOCK_HARDSHIP_ID).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenIncorrectParameters_whenPatchIsInvoked_then4xxIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(PATCH_ENDPOINT_URL, MOCK_HARDSHIP_ID).content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }}
