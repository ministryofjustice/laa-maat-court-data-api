package gov.uk.courtdata.eform.controller;


import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import gov.uk.courtdata.eform.service.EformsDecisionHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EformsDecisionHistoryController.class)
public class EformsDecisionHistoryControllerTest {

    private static final String ENDPOINT_FORMAT = "/api/eform/decision-history";
    private static final int USN = 123;

    @MockBean
    private EformsDecisionHistoryService eformsDecisionHistoryService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldSuccessfullyGetEformApplication() throws Exception {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(USN).repId(45635).fundingDecision("Granted").iojResult("Pass").build();
        List<EformsDecisionHistory> eformsDecisionHistoryList = List.of(eformsDecisionHistory);
        when(eformsDecisionHistoryService.getAllEformsDecisionHistory(USN))
                .thenReturn(eformsDecisionHistoryList);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_FORMAT+ "/" +123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("["+responseEformsDecisionHistory()+"]"));
        verify(eformsDecisionHistoryService, times(1)).getAllEformsDecisionHistory(USN);
    }

    private String responseEformsDecisionHistory(){
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
