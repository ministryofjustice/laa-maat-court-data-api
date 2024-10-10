package gov.uk.courtdata.applicant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.dto.SendToCCLFDTO;
import gov.uk.courtdata.applicant.service.ApplicantHistoryService;
import gov.uk.courtdata.applicant.service.ApplicantService;
import gov.uk.courtdata.applicant.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.applicant.validator.ApplicantValidationProcessor;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.util.RequestBuilderUtils;

import java.util.List;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicantController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ApplicantControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/application/applicant";
    private static final int ID = 1;
    private static final int INVALID_ID = 999999999;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ApplicantService applicantService;

    @MockBean
    private ApplicantHistoryService applicantHistoryService;

    @MockBean
    private RepOrderApplicantLinksService repOrderApplicantLinksService;

    @MockBean
    private ApplicantValidationProcessor validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenAValidationException_whenGetRepOrderApplicantLinksIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(validator.validate(any()))
                .thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + REP_ID))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenResponseIsReturned() throws Exception {
        List<RepOrderApplicantLinksDTO> response = TestModelDataBuilder.getRepOrderApplicantLinksDTOs(ID);
        when(repOrderApplicantLinksService.find(REP_ID))
                .thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + REP_ID))
                .andExpect(status().isOk());
        verify(repOrderApplicantLinksService).find(REP_ID);
    }

    @Test
    void givenValidRequest_whenUpdateRepOrderApplicantLinkIsInvoked_thenUpdateIsSuccess() throws Exception {
        RepOrderApplicantLinksDTO repOrderApplicantLinksDTO = TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID);
        when(repOrderApplicantLinksService.update(any()))
                .thenReturn(repOrderApplicantLinksDTO);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(repOrderApplicantLinksDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void givenInValidRequest_whenUpdateRepOrderApplicantLinkIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(repOrderApplicantLinksService.update(any()))
                .thenThrow(new RequestedObjectNotFoundException("Rep Order Applicant Links not found"));
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenUpdateRepOrderApplicantLinkIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(repOrderApplicantLinksService.update(any()))
                .thenThrow(EmptyResultDataAccessException.class);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenAValidRequest_whenCreateRepOrderApplicantLinkIsInvoked_thenCreateIsSuccess() throws Exception {
        RepOrderApplicantLinksDTO repOrderApplicantLinksDTO = TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID);
        repOrderApplicantLinksDTO.setId(null);
        when(repOrderApplicantLinksService.create(any()))
                .thenReturn(repOrderApplicantLinksDTO);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(repOrderApplicantLinksDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenMissingRequestBody_whenCreateRepOrderApplicantLinkIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/rep-order-applicant-links"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidRequest_whenCreateRepOrderApplicantLinkIsInvoked_thenBadRequestResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectId_whenGetApplicantHistoryIsInvoked_thenResponseIsReturned() throws Exception {
        ApplicantHistoryDTO applicantHistoryDTO = TestModelDataBuilder.getApplicantHistoryDTO(ID, "N");
        when(applicantHistoryService.find(ID))
                .thenReturn(applicantHistoryDTO);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/applicant-history/" + ID))
                .andExpect(status().isOk());
        verify(applicantHistoryService).find(1);
    }

    @Test
    void givenInternalServerError_whenGetApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(applicantHistoryService.find(ID))
                .thenThrow(EmptyResultDataAccessException.class);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/applicant-history/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "Y")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidRequest_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() throws Exception {
        ApplicantHistoryDTO applicantHistoryDTO = TestModelDataBuilder.getApplicantHistoryDTO(ID, "N");
        when(applicantHistoryService.update(any()))
                .thenReturn(applicantHistoryDTO);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(applicantHistoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInValidRequest_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(applicantHistoryService.update(any()))
                .thenThrow(new RequestedObjectNotFoundException("Applicant History not found"));
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "Y")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(applicantHistoryService.update(any()))
                .thenThrow(EmptyResultDataAccessException.class);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "Y")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenCorrectId_whenGetApplicantIsInvoked_thenResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + ID))
                .andExpect(status().isOk());
        verify(applicantService).find(ID);
    }

    @Test
    void givenInternalServerError_whenGetApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(applicantService.find(ID))
                .thenThrow(EmptyResultDataAccessException.class);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(Applicant.builder().id(ID).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidRequest_whenUpdateApplicantIsInvoked_thenUpdateIsSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenUpdateApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenInValidRequest_whenUpdateApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(new RequestedObjectNotFoundException("Applicant not found")).when(applicantService).update(any(), any());
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenUpdateApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(applicantService).update(any(), any());
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenCorrectId_whenDeleteApplicantIsInvoked_thenResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/" + ID))
                .andExpect(status().isOk());
        verify(applicantService).delete(ID);
    }

    @Test
    void givenInternalServerError_whenDeleteApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(applicantService).delete(anyInt());
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidRequest_whenCreateApplicantIsInvoked_thenUpdateIsSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenCreateApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenInValidRequest_whenCreateApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(new RequestedObjectNotFoundException("Applicant not found")).when(applicantService).create(any());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenCreateApplicantIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(applicantService).create(any());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenCorrectId_whenDeleteApplicantHistoryIsInvoked_thenResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/applicant-history" + "/" + ID))
                .andExpect(status().isOk());
        verify(applicantHistoryService).delete(ID);
    }

    @Test
    void givenInternalServerError_whenDeleteApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(applicantHistoryService).delete(anyInt());
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/applicant-history" + "/" + ID)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicant(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidRequest_whenCreateApplicantHistoryIsInvoked_thenCreateIsSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "N")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenCreateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/applicant-history")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenInValidRequest_whenCreateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(new RequestedObjectNotFoundException("Applicant not found")).when(applicantHistoryService).create(any());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "N")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenCreateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(applicantHistoryService).create(any());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "N")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidRequest_whenUpdateSendToCCLFIsInvoked_thenUpdateIsSuccess() throws Exception {
        SendToCCLFDTO sendToCCLFDTO = SendToCCLFDTO.builder().applId(1).repId(1).applHistoryId(1).build();
        doNothing().when(applicantService).updateSendToCCLF(any());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/update-cclf")
                        .content(objectMapper.writeValueAsString(sendToCCLFDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenUpdateSendToCCLFIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/update-cclf")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInValidRequest_whenUpdateSendToCCLFIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        doThrow(new RequestedObjectNotFoundException("Applicant not found"))
                .when(applicantService).updateSendToCCLF(any());
        SendToCCLFDTO sendToCCLFDTO = SendToCCLFDTO.builder().applId(1).repId(1).applHistoryId(1).build();
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/update-cclf")
                        .content(objectMapper.writeValueAsString(sendToCCLFDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
