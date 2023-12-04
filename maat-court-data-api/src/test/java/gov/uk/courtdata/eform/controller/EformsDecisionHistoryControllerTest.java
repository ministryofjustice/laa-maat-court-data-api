package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import gov.uk.courtdata.eform.service.EformsDecisionHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EformsDecisionHistoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EformsDecisionHistoryControllerTest {

    private static final String BASE_ENDPOINT_FORMAT = "/api/eform/decision-history";

    @MockBean
    private EformsDecisionHistoryService eformsDecisionHistoryService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldSuccessfullyGetNewEformDecisionHistoryForGivenUSN() throws Exception {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(12345).repId(4563578).fundingDecision("Granted").iojResult("Pass").repDecision("Granted").build();

        when(eformsDecisionHistoryService.getNewEformsDecisionHistoryRecord(12345))
                .thenReturn(eformsDecisionHistory);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+ "/" +12345+"/latest-record")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usn").value(String.valueOf(12345)))
                .andExpect(jsonPath("$.repId").value(String.valueOf(4563578)))
                .andExpect(jsonPath("$.fundingDecision").value("Granted"))
                .andExpect(jsonPath("$.iojResult").value("Pass"))
                .andExpect(jsonPath("$.repDecision").value("Granted"));

        verify(eformsDecisionHistoryService, times(1)).getNewEformsDecisionHistoryRecord(12345);
    }

    @Test
    void shouldSuccessfullyGetPreviousEformDecisionHistoryWroteToResultForGivenUSN() throws Exception {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(2).usn(12345).repId(4563578)
                .fundingDecision("Granted").iojResult("Pass").repDecision("Granted").wroteToResults("Y").build();

        when(eformsDecisionHistoryService.getPreviousEformsDecisionHistoryRecordWroteToResult(12345))
                .thenReturn(eformsDecisionHistory);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+ "/" +12345+"/previous-wrote-to-result")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usn").value(String.valueOf(12345)))
                .andExpect(jsonPath("$.repId").value(String.valueOf(4563578)))
                .andExpect(jsonPath("$.fundingDecision").value("Granted"))
                .andExpect(jsonPath("$.iojResult").value("Pass"))
                .andExpect(jsonPath("$.repDecision").value("Granted"))
                .andExpect(jsonPath("$.wroteToResults").value("Y"));

        verify(eformsDecisionHistoryService, times(1)).getPreviousEformsDecisionHistoryRecordWroteToResult(12345);
    }

    @Test
    void shouldSuccessfullyDeleteEformDecisionHistoryForGivenUSN() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_ENDPOINT_FORMAT+ "/" +12345)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(eformsDecisionHistoryService, times(1)).deleteEformsDecisionHistory(12345);
    }

    @Test
    void shouldSuccessfullyUpdateEformDecisionHistory() throws Exception {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().wroteToResults("Y").build();
        String requestJson = "{\"wroteToResults\":\"Y\"}";
        mvc.perform(MockMvcRequestBuilders.patch(BASE_ENDPOINT_FORMAT+ "/" +12345).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(eformsDecisionHistoryService, times(1)).updateEformsDecisionHistoryFields(12345, eformsDecisionHistory);
    }

    @Test
    void shouldSuccessfullyCreateEformDecisionHistory() throws Exception {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).repId(45635).fundingDecision("Granted").iojResult("Pass").build();
        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT).content(eformsDecisionHistory())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(eformsDecisionHistoryService, times(1)).createEformsDecisionHistory(eformsDecisionHistory);
    }
    @Test
    void shouldSuccessfullyGetAllEformDecisionHistoryForGivenUSN() throws Exception {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).repId(45635).fundingDecision("Granted").iojResult("Pass").build();
        List<EformsDecisionHistory> eformsDecisionHistoryList = List.of(eformsDecisionHistory);
        when(eformsDecisionHistoryService.getAllEformsDecisionHistory(123))
                .thenReturn(eformsDecisionHistoryList);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+ "/" +123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("["+eformsDecisionHistory()+"]"));
        verify(eformsDecisionHistoryService, times(1)).getAllEformsDecisionHistory(123);
    }

    private String eformsDecisionHistory(){
        return "{\n" +
                "  \"id\": 1,\n" +
                "  \"usn\": 123,\n" +
                "  \"repId\": 45635,\n" +
                "  \"dateResultCreated\": null,\n" +
                "  \"caseId\": null,\n" +
                "  \"dateAppCreated\": null,\n" +
                "  \"iojResult\": \"Pass\",\n" +
                "  \"iojAssessorName\": null,\n" +
                "  \"iojReason\": null,\n" +
                "  \"meansResult\": null,\n" +
                "  \"meansAssessorName\": null,\n" +
                "  \"dateMeansCreated\": null,\n" +
                "  \"fundingDecision\": \"Granted\",\n" +
                "  \"passportResult\": null,\n" +
                "  \"passportAssessorName\": null,\n" +
                "  \"datePassportCreated\": null,\n" +
                "  \"dwpResult\": null,\n" +
                "  \"iojAppealResult\": null,\n" +
                "  \"hardshipResult\": null,\n" +
                "  \"caseType\": null,\n" +
                "  \"repDecision\": null,\n" +
                "  \"ccRepDecision\": null,\n" +
                "  \"assessmentId\": null,\n" +
                "  \"assessmentType\": null,\n" +
                "  \"wroteToResults\": null,\n" +
                "  \"magsOutcome\": null\n" +
                "}";
    }
}
