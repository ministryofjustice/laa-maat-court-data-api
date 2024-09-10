package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.dto.*;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.service.RepOrderMvoRegService;
import gov.uk.courtdata.reporder.service.RepOrderMvoService;
import gov.uk.courtdata.reporder.service.RepOrderService;
import gov.uk.courtdata.reporder.validator.UpdateAppDateCompletedValidator;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class RepOrderControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders";
    private static final String MVO_REG_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo-reg";
    private static final String MVO_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo";
    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";
    private static final String CURRENT_REGISTRATION = "current-registration";
    private static final Integer USN = 12345;
    private static final Integer REP_ID = 54321;
    private static final int USN_VALUE = 810529;
    private static final int MAAT_REF_VALUE = 4799873;
    private static final String CASE_ID_VALUE = "1400466826-10";
    private static final String CASE_TYPE_VALUE = "SUMMARY ONLY";
    private static final String IOJ_RESULT_VALUE = "PASS";
    private static final String IOJ_ASSESSOR_FULL_NAME = "Maeve OConnor";
    private static final String IOJ_ASSESSOR_USERNAME = "ocon-m";
    private static final String DATE_APP_CREATED_VALUE = "2015-01-09";
    private static final String MEANS_INIT_RESULT_VALUE = "PASS";
    private static final String MEANS_INIT_STATUS_VALUE = "COMPLETE";
    private static final String MEANS_ASSESSOR_NAME_VALUE = "Maeve OConnor";
    private static final String DATE_MEANS_CREATED_VALUE = "2015-01-09T11:16:54";
    private static final String PASSPORT_RESULT_VALUE = "FAIL";
    private static final String PASSPORT_STATUS_VALUE = "COMPLETE";
    private static final String PASSPORT_ASSESSOR_NAME_VALUE = "Maeve OConnor";
    private static final String DATE_PASSPORT_CREATED_VALUE = "2015-01-09T11:16:29";
    private static final String FUNDING_DECISION_VALUE = "GRANTED";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UpdateAppDateCompletedValidator updateAppDateCompletedValidator;
    @MockBean
    private MaatIdValidator maatIdValidator;
    @MockBean
    private RepOrderService repOrderService;
    @MockBean
    private RepOrderMvoRegService repOrderMvoRegService;
    @MockBean
    private RepOrderMvoService repOrderMvoService;
    @MockBean
    private RepOrderRepository repOrderRepository;

    @Test
    void givenGetRequestWithValidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenAssessmentIsRetrieved() throws Exception {
        RepOrderDTO expected = TestModelDataBuilder.getRepOrderDTO();
        when(repOrderService.find(anyInt(), anyBoolean()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()));
    }

    @Test
    void givenGetRequestWithValidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenContributionsAndIojAppealAndRepOrderCCOutcomeIsRetrieved() throws Exception {
        RepOrderDTO expected = TestModelDataBuilder.getRepOrderDTO();
        expected.setContributions(Collections.singletonList(TestModelDataBuilder.getContributionsDTO()));
        expected.setIojAppeal(Collections.singletonList(TestModelDataBuilder.getIOJAppealDTO()));
        expected.setRepOrderCCOutcome(Collections.singletonList(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1)));
        when(repOrderService.find(anyInt(), anyBoolean()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.contributions.length()").value(1))
                .andExpect(jsonPath("$.repOrderCCOutcome.length()").value(1))
                .andExpect(jsonPath("$.iojAppeal.length()").value(1));
    }

    @Test
    void givenGetRequestWithValidRepIdAndOptionalParameters_whenFindInvoked_thenAssessmentIsRetrieved() throws Exception {
        RepOrderDTO expected = TestModelDataBuilder.getRepOrderDTO();
        when(repOrderService.find(anyInt(), eq(true)))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?has_sentence_order_date=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()));
    }

    @Test
    void givenGetRequestWithInvalidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderService.find(anyInt(), anyBoolean()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order found for ID: 1234"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order found for ID: 1234"));
    }

    @Test
    void givenHeadRequestWithValidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenContentLengthIsOne() throws Exception {
        when(repOrderService.exists(anyInt(), anyBoolean()))
                .thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.head(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

    @Test
    void givenHeadRequestWithInvalidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenContentLengthIsZero() throws Exception {
        when(repOrderService.exists(anyInt(), anyBoolean()))
                .thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.head(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "0"));
    }

    @Test
    void givenCorrectParameters_whenUpdateAppCompletedIsInvoked_thenCompletedDateShouldUpdated() throws Exception {
        when(updateAppDateCompletedValidator.validate(any(UpdateAppDateCompleted.class)))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/update-date-completed")
                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson(TestModelDataBuilder.REP_ID))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void givenIncorrectParameters_whenUpdateAppCompletedIsInvoked_thenErrorIsThrown() throws Exception {
        when(updateAppDateCompletedValidator.validate(any(UpdateAppDateCompleted.class)))
                .thenThrow(new ValidationException());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/update-date-completed").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenValidMvoId_whenFindByCurrentRegistrationIsInvoked_thenDataIsRetrieved() throws Exception {
        List<RepOrderMvoRegDTO> expected = List.of(TestModelDataBuilder.getRepOrderMvoRegDTO());
        when(repOrderMvoRegService.findByCurrentMvoRegistration(anyInt()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(MVO_REG_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/" + CURRENT_REGISTRATION))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].registration").value(expected.get(0).getRegistration()));
    }

    @Test
    void givenInvalidMvoId_whenFindByCurrentRegistrationIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderMvoRegService.findByCurrentMvoRegistration(anyInt()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order MVO Reg found for ID: 1234"));

        mvc.perform(MockMvcRequestBuilders.get(MVO_REG_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/" + CURRENT_REGISTRATION))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order MVO Reg found for ID: 1234"));
    }

    @Test
    void givenValidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenDataIsRetrieved() throws Exception {
        RepOrderMvoDTO expected = TestModelDataBuilder.getRepOrderMvoDTO();
        when(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(anyInt(), anyString()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicleOwner").value(expected.getVehicleOwner()));
    }

    @Test
    void givenInvalidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(anyInt(), anyString()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order MVO found for ID: 1234"));

        mvc.perform(MockMvcRequestBuilders.get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order MVO found for ID: 1234"));
    }

    @Test
    void givenIncorrectParameters_whenUpdateIsInvoked_thenErrorIsThrown() throws Exception {
        when(maatIdValidator.validate(any()))
                .thenThrow(new ValidationException());

        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenCorrectParameters_whenUpdateIsInvoked_thenUpdateRepOrderIsSuccess() throws Exception {
        when(maatIdValidator.validate(any()))
                .thenReturn(Optional.empty());
        when(repOrderService.update(any())).thenReturn(TestModelDataBuilder.getRepOrderDTO());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(TestModelDataBuilder.getUpdateRepOrderJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TestModelDataBuilder.REP_ID));
    }

    @Test
    void givenCorrectParameters_whenCreateIsInvoked_thenOkResponseAndRepOrderIsReturned() throws Exception {
        when(repOrderService.create(any())).thenReturn(TestModelDataBuilder.getRepOrderDTO());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(TestModelDataBuilder.getCreateRepOrderJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TestModelDataBuilder.REP_ID));
    }

    @Test
    void givenValidRepId_whenDeleteIsInvoked_thenReturnOkResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/12345678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(repOrderService, times(1)).delete(12345678);
    }

    @Test
    void givenValidRepId_whenIOJAssessorDetailsGetRequestIsMade_thenIOJAssessorDetailsAreReturned() throws Exception {
        when(repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID))
                .thenReturn(TestModelDataBuilder.getAssessorDetails());

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/ioj-assessor-details"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName").value("Karen Greaves"))
                .andExpect(jsonPath("$.userName").value("grea-k"));
    }

    @Test
    void givenUnknownRepId_whenIOJAssessorDetailsGetRequestIsMade_thenNotFoundResponseIsReturned() throws Exception {
        int unknownRepId = 1245;
        when(repOrderService.findIOJAssessorDetails(unknownRepId))
                .thenThrow(new RequestedObjectNotFoundException("Unable to find AssessorDetails for repId: [1245]"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + unknownRepId + "/ioj-assessor-details"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Unable to find AssessorDetails for repId: [1245]"));
    }


    @Test
    void givenValidRequest_whenUpdateRepOrderIsInvoked_thenUpdateIsSuccess() throws Exception {
        doNothing().when(repOrderService).update(TestModelDataBuilder.REP_ID, Map.of("iojResult", "PASS"));

        String requestJson = "{\"iojResult\":\"PASS\"}";
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(repOrderService).update(TestModelDataBuilder.REP_ID, Map.of("iojResult", "PASS"));
    }


    @Test
    void givenAEmptyContent_whenUpdateRepOrderIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenInValidRequest_whenUpdateRepOrderIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(new RequestedObjectNotFoundException("Applicant not found")).when(repOrderService).update(any(), any());
        String requestJson = "{\"iojResult\":\"PASS\"}";
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenUpdateRepOrderIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(repOrderService).update(any(), any());
        String requestJson = "{\"iojResult\":\"PASS\"}";
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidUsn_whenFindRepOrderIdByUsnIsInvokedAndFindsARepOrder_thenCorrectResponseIsReturned() throws Exception {
        when(repOrderService.findRepOrderIdByUsn(USN))
                .thenReturn(REP_ID);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("usn", String.valueOf(USN)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(REP_ID)));
    }

    @Test
    void givenValidUsn_whenFindRepOrderIdByUsnIsInvokedAndFindsNoRepOrder_thenCorrectResponseIsReturned() throws Exception {
        RepOrderEntity expectedRepOrder = null;
        when(repOrderRepository.findByUsn(USN))
                .thenReturn(expectedRepOrder);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("usn", String.valueOf(USN)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenValidUsn_whenFindRepOrderByUsnIsInvokedAndFindsARepOrder_thenCorrectResponseIsReturned() throws Exception {
        RepOrderStateDTO repOrderStateDTO = RepOrderStateDTO.builder()
                .usn(USN_VALUE)
                .maatRef(MAAT_REF_VALUE)
                .caseId(CASE_ID_VALUE)
                .caseType(CASE_TYPE_VALUE)
                .iojResult(IOJ_RESULT_VALUE)
                .iojAssessorName(new AssessorDetails(IOJ_ASSESSOR_FULL_NAME, IOJ_ASSESSOR_USERNAME))
                .dateAppCreated(LocalDate.parse(DATE_APP_CREATED_VALUE))
                .iojReason(null)
                .meansInitResult(MEANS_INIT_RESULT_VALUE)
                .meansInitStatus(MEANS_INIT_STATUS_VALUE)
                .meansFullResult(null)
                .meansFullStatus(null)
                .meansAssessorName(MEANS_ASSESSOR_NAME_VALUE)
                .dateMeansCreated(LocalDateTime.parse(DATE_MEANS_CREATED_VALUE))
                .passportResult(PASSPORT_RESULT_VALUE)
                .passportStatus(PASSPORT_STATUS_VALUE)
                .passportAssessorName(PASSPORT_ASSESSOR_NAME_VALUE)
                .datePassportCreated(LocalDateTime.parse(DATE_PASSPORT_CREATED_VALUE))
                .fundingDecision(FUNDING_DECISION_VALUE)
                .build();
        when(repOrderService.findRepOrderStateByUsn(USN)).thenReturn(repOrderStateDTO);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/usn/" + USN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usn").value(USN_VALUE))
                .andExpect(jsonPath("$.maatRef").value(MAAT_REF_VALUE))
                .andExpect(jsonPath("$.caseId").value(CASE_ID_VALUE))
                .andExpect(jsonPath("$.caseType").value(CASE_TYPE_VALUE))
                .andExpect(jsonPath("$.iojResult").value(IOJ_RESULT_VALUE))
                .andExpect(jsonPath("$.iojAssessorName.fullName").value(IOJ_ASSESSOR_FULL_NAME))
                .andExpect(jsonPath("$.iojAssessorName.userName").value(IOJ_ASSESSOR_USERNAME))
                .andExpect(jsonPath("$.dateAppCreated").value(DATE_APP_CREATED_VALUE))
                .andExpect(jsonPath("$.iojReason").doesNotExist())
                .andExpect(jsonPath("$.meansInitResult").value(MEANS_INIT_RESULT_VALUE))
                .andExpect(jsonPath("$.meansInitStatus").value(MEANS_INIT_STATUS_VALUE))
                .andExpect(jsonPath("$.meansFullResult").doesNotExist())
                .andExpect(jsonPath("$.meansFullStatus").doesNotExist())
                .andExpect(jsonPath("$.meansAssessorName").value(MEANS_ASSESSOR_NAME_VALUE))
                .andExpect(jsonPath("$.dateMeansCreated").value(DATE_MEANS_CREATED_VALUE))
                .andExpect(jsonPath("$.passportResult").value(PASSPORT_RESULT_VALUE))
                .andExpect(jsonPath("$.passportStatus").value(PASSPORT_STATUS_VALUE))
                .andExpect(jsonPath("$.passportAssessorName").value(PASSPORT_ASSESSOR_NAME_VALUE))
                .andExpect(jsonPath("$.datePassportCreated").value(DATE_PASSPORT_CREATED_VALUE))
                .andExpect(jsonPath("$.fundingDecision").value(FUNDING_DECISION_VALUE));
    }

    @Test
    void givenValidUsn_whenFindRepOrderByUsnIsInvokedAndFindsNoRepOrder_thenCorrectResponseIsReturned() throws Exception {
        when(repOrderService.findRepOrderStateByUsn(USN)).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/usn/" + USN))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidParameters_whenFindFdcFastTrackingInvoked_thenIdsAreReturned() throws Exception {
        Set<Integer> expectedIds = Set.of(5,6);
        LocalDate date = LocalDate.now();
        when(repOrderService.findEligibleForFdcFastTracking(5, date, 2)).thenReturn(expectedIds);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcFastTrack", "true")
                        .param("delay", "5")
                        .param("dateReceived", date.format(DateTimeFormatter.ISO_DATE))
                        .param("numRecords", "2")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(5, 6)));
    }

    @Test
    void givenInvalidParameters_whenFindFdcFastTrackingInvoked_thenErrorIsReturned() throws Exception {
        LocalDate date = LocalDate.now();
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcFastTrack", "true")
                        .param("dateReceived", date.format(DateTimeFormatter.ISO_DATE))
                        .param("numRecords", "2")
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail").value("Required parameter 'delay' is not present."))
                .andExpect(jsonPath("title").value("Bad Request"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcFastTrack", "true")
                        .param("delay", "5")
                        .param("numRecords", "2")
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail").value("Required parameter 'dateReceived' is not present."))
                .andExpect(jsonPath("title").value("Bad Request"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcFastTrack", "true")
                        .param("delay", "5")
                        .param("dateReceived", date.format(DateTimeFormatter.ISO_DATE))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail").value("Required parameter 'numRecords' is not present."))
                .andExpect(jsonPath("title").value("Bad Request"));
    }

    @Test
    void givenValidParameters_whenFindFdcDelayedPickupInvoked_thenIdsAreReturned() throws Exception {
        Set<Integer> expectedIds = Set.of(5,6);
        LocalDate date = LocalDate.now();
        when(repOrderService.findEligibleForFdcDelayedPickup(5, date, 2)).thenReturn(expectedIds);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcDelayedPickup", "true")
                        .param("delay", "5")
                        .param("dateReceived", date.format(DateTimeFormatter.ISO_DATE))
                        .param("numRecords", "2")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", Matchers.containsInAnyOrder(5, 6)));
    }

    @Test
    void givenInvalidParameters_whenFindFdcDelayedPickupInvoked_thenErrorIsReturned() throws Exception {
        LocalDate date = LocalDate.now();
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcDelayedPickup", "true")
                        .param("dateReceived", date.format(DateTimeFormatter.ISO_DATE))
                        .param("numRecords", "2")
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail").value("Required parameter 'delay' is not present."))
                .andExpect(jsonPath("title").value("Bad Request"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcDelayedPickup", "true")
                        .param("delay", "5")
                        .param("numRecords", "2")
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail").value("Required parameter 'dateReceived' is not present."))
                .andExpect(jsonPath("title").value("Bad Request"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("fdcDelayedPickup", "true")
                        .param("delay", "5")
                        .param("dateReceived", date.format(DateTimeFormatter.ISO_DATE))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("detail").value("Required parameter 'numRecords' is not present."))
                .andExpect(jsonPath("title").value("Bad Request"));
    }
}
