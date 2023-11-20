package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.repository.entity.EformsHistory;
import gov.uk.courtdata.eform.service.EformsHistoryService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EformsHistoryController.class)
public class EformsHistoryControllerTest {

    private static final String BASE_ENDPOINT_FORMAT = "/api/eform/history";

    @MockBean
    private EformsHistoryService eformsHistoryService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldSuccessfullyGetNewEformHistoryRecordForGivenUSN() throws Exception {
        EformsHistory eformsHistory = EformsHistory.builder().id(1).usn(12345).action("Get").build();

        when(eformsHistoryService.getLatestEformsHistoryRecord(12345))
                .thenReturn(eformsHistory);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+ "/" +12345+"/latest-record")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usn").value(String.valueOf(12345)))
                .andExpect(jsonPath("$.action").value(String.valueOf("Get")));

        verify(eformsHistoryService, times(1)).getLatestEformsHistoryRecord(12345);
    }

    @Test
    void shouldSuccessfullyDeleteEformHistoryForGivenUSN() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_ENDPOINT_FORMAT+ "/" +12345)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(eformsHistoryService, times(1)).deleteEformsHistory(12345);
    }

     @Test
    void shouldSuccessfullyCreateEformDecisionHistory() throws Exception {
         EformsHistory eformsHistory = EformsHistory.builder().id(2).usn(123).action("Get").userCreated("test").build();
        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT).content(eformsHistory())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(eformsHistoryService, times(1)).createEformsHistory(eformsHistory);
    }


    @Test
    void shouldSuccessfullyGetAllEformHistoryForGivenUSN() throws Exception {
        EformsHistory eformsHistory = EformsHistory.builder().id(2).usn(123).action("Get").userCreated("test").build();
        List<EformsHistory> eformsHistoryList = List.of(eformsHistory);
        when(eformsHistoryService.getAllEformsHistory(123))
                .thenReturn(eformsHistoryList);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+ "/" +123)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("["+eformsHistory()+"]"));
        verify(eformsHistoryService, times(1)).getAllEformsHistory(123);
    }

    private String eformsHistory(){
        return "{\n" +
                "  \"id\": 2,\n" +
                "  \"usn\": 123,\n" +
                "  \"repId\": null,\n" +
                "  \"action\": \"Get\",\n" +
                "  \"keyId\": null,\n" +
                "  \"dateCreated\": null,\n" +
                "  \"userCreated\": \"test\"\n" +
                "}";
    }

}
