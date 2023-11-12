package gov.uk.courtdata.concor.controller;

import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import gov.uk.courtdata.dces.controller.ConcorContributionsRestController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ConcorContributionsRestController.class)
class ConcorControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConcorContributionsService concorService;

    @Test
    void testContributionFileContent() throws Exception {

        when(concorService.getConcorFiles(ConcorContributionStatus.ACTIVE)).thenReturn(List.of("Hello"));

        mvc.perform(MockMvcRequestBuilders.get(String.format(ENDPOINT_URL  +"/concor-contribution-files"))
                        .queryParam("status", ConcorContributionStatus.ACTIVE.name())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
    @Test
    void testUpdateContributionFileRef() throws Exception {
        ConcorContributionRequest concorContributionRequest = new ConcorContributionRequest(123,"");
        doNothing().when(concorService).createContributionFileAndUpdateConcorContributionsStatus(concorContributionRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(concorContributionRequest);

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/update-contribution"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}