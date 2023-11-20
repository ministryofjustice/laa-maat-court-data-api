package gov.uk.courtdata.eform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.uk.courtdata.eform.repository.entity.EformResultsEntity;
import gov.uk.courtdata.eform.service.EformResultsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EformResultsController.class)
public class EformResultsControllerTest {

    private static final String BASE_ENDPOINT_FORMAT = "/api/eform/results";
    private static final Integer ID_NUMBER = 94271347;
    private static final Integer USN_NUMBER = 729862;
    private static final Integer MAAT_REF_NUMBER = 4659133;
    private static final LocalDateTime DATE_CREATED_DATE = LocalDateTime.of(2014, Month.SEPTEMBER, 22, 0, 0);
    private static final String CASE_ID = "2209144444-1974";
    private static final String IOJ_RESULT = "PASS";
    private static final String IOJ_ASSESSOR_NAME = "Test Person";
    private static final String FUNDING_DECISION = "Granted";
    private static final String PASSPORT_RESULT = "PASS";
    private static final String DWP_RESULT = "Yes";
    private static final String CASE_TYPE = "EITHER WAY";
    private static final String STAGE = "C";

    @MockBean
    private EformResultsService eformResultsService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldSuccessfullyGetEformResultForGivenUSN() throws Exception {
        EformResultsEntity eformResultsEntity = buildEformResult();

        when(eformResultsService.retrieve(USN_NUMBER))
                .thenReturn(eformResultsEntity);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+ "/" + USN_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(String.valueOf(ID_NUMBER)))
                .andExpect(jsonPath("$.usn").value(String.valueOf(USN_NUMBER)))
                .andExpect(jsonPath("$.maatRef").value(String.valueOf(MAAT_REF_NUMBER)))
                .andExpect(jsonPath("$.dateCreated").value("2014-09-22T00:00:00"))
                .andExpect(jsonPath("$.caseId").value(CASE_ID))
                .andExpect(jsonPath("$.iojResult").value(IOJ_RESULT))
                .andExpect(jsonPath("$.iojAssessorName").value(IOJ_ASSESSOR_NAME))
                .andExpect(jsonPath("$.meansResult").value(nullValue()))
                .andExpect(jsonPath("$.meansAssessorName").value(nullValue()))
                .andExpect(jsonPath("$.dateMeansCreated").value(nullValue()))
                .andExpect(jsonPath("$.fundingDecision").value(FUNDING_DECISION))
                .andExpect(jsonPath("$.iojReason").value(nullValue()))
                .andExpect(jsonPath("$.passportResult").value(PASSPORT_RESULT))
                .andExpect(jsonPath("$.passportAssesorName").value(IOJ_ASSESSOR_NAME))
                .andExpect(jsonPath("$.datePassportCreated").value("2014-09-22T00:00:00"))
                .andExpect(jsonPath("$.dwpResult").value(DWP_RESULT))
                .andExpect(jsonPath("$.iojAppealResult").value(nullValue()))
                .andExpect(jsonPath("$.caseType").value(CASE_TYPE))
                .andExpect(jsonPath("$.iojAssessorName").value(IOJ_ASSESSOR_NAME));

        verify(eformResultsService, times(1)).retrieve(USN_NUMBER);
    }

    @Test
    void shouldSuccessfullyCreateEformDecisionHistory() throws Exception {
        EformResultsEntity eformResultsEntity = buildEformResult();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT).content(objectMapper.writeValueAsString(eformResultsEntity))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(eformResultsService, times(1)).create(eformResultsEntity);
    }

    @Test
    void shouldSuccessfullyDeleteEformResultForGivenUSN() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_ENDPOINT_FORMAT+ "/" +USN_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(eformResultsService, times(1)).delete(USN_NUMBER);
    }

    @Test
    void shouldSuccessfullyUpdateEformResultForGivenUSN() throws Exception {
        EformResultsEntity eformResultsEntity = EformResultsEntity.builder().iojResult("FAIL").build();
        String requestJson = "{\"iojResult\":\"FAIL\"}";
        mvc.perform(MockMvcRequestBuilders.patch(BASE_ENDPOINT_FORMAT+ "/" +USN_NUMBER).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(eformResultsService, times(1)).updateEformResultFields(USN_NUMBER, eformResultsEntity);
    }

    public static EformResultsEntity buildEformResult() {
        return EformResultsEntity.builder()
                .id(ID_NUMBER)
                .usn(USN_NUMBER)
                .maatRef(MAAT_REF_NUMBER)
                .dateCreated(DATE_CREATED_DATE)
                .caseId(CASE_ID)
                .iojResult(IOJ_RESULT)
                .iojAssessorName(IOJ_ASSESSOR_NAME)
                .meansResult(null)
                .meansAssessorName(null)
                .dateMeansCreated(null)
                .fundingDecision(FUNDING_DECISION)
                .iojReason(null)
                .passportResult(PASSPORT_RESULT)
                .passportAssesorName(IOJ_ASSESSOR_NAME)
                .datePassportCreated(DATE_CREATED_DATE)
                .dwpResult(DWP_RESULT)
                .iojAppealResult(null)
                .caseType(CASE_TYPE)
                .stage(STAGE)
                .build();
    }

}