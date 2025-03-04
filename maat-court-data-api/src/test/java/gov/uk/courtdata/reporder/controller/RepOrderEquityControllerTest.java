package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.entity.RepOrderEquityEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.service.RepOrderEquityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepOrderEquityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RepOrderEquityControllerTest {

    private static final String BASE_ENDPOINT_FORMAT = "/api/internal/v1/assessment/rep-order-equity";
    private static final int ID = 123;
    private static final int NON_EXISTENT_ID = 888;
    private static final int REP_ID = 777;
    private static final RepOrderEquityEntity REP_ORDER_EQUITY = RepOrderEquityEntity.builder().id(ID).build();

    @MockitoBean
    RepOrderEquityService mockRepOrderEquityService;

    @Autowired
    private MockMvc mvc;

    @Test
    void givenID_whenGetRepOrderEquityCalled_thenReturnRepOrderEquity() throws Exception {
        when(mockRepOrderEquityService.retrieve(ID))
                .thenReturn(REP_ORDER_EQUITY);

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+"/"+ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":"+ID+"}"));
    }

    @Test
    void givenNonExistentID_whenGetRepOrderEquityCalled_thenReturnAnError() throws Exception {
        when(mockRepOrderEquityService.retrieve(NON_EXISTENT_ID))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order Equity found with ID: " + NON_EXISTENT_ID));

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_FORMAT+"/"+NON_EXISTENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"code\":\"NOT_FOUND\",\"message\":\"No Rep Order Equity found with ID: "+NON_EXISTENT_ID+"\"}"));
    }

    @Test
    void givenValidData_whenCreateRepOrderEquityCalled_thenSuccessfullyRepOrderEquity() throws Exception {

        String repOrderEquityData = repOrderEquityData();

        RepOrderEquityEntity repOrderEquityEntity = RepOrderEquityEntity.builder()
                .repId(REP_ID)
                .undeclared("N")
                .userCreated("test-s")
                .active("Y")
                .build();

        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_FORMAT)
                        .content(repOrderEquityData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepOrderEquityService, times(1)).create(repOrderEquityEntity);
    }

    @Test
    void givenValidData_whenUpdateRepOrderEquityCalled_thenSuccessfullyUpdateRepOrderEquity() throws Exception {
        String repOrderEquityData = repOrderEquityData();

        RepOrderEquityEntity repOrderEquityEntity = RepOrderEquityEntity.builder()
                .repId(REP_ID)
                .undeclared("N")
                .userCreated("test-s")
                .active("Y")
                .build();

        mvc.perform(MockMvcRequestBuilders.patch(BASE_ENDPOINT_FORMAT + "/" + ID)
                        .content(repOrderEquityData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepOrderEquityService, times(1)).update(ID, repOrderEquityEntity);
    }

    @Test
    void givenID_whenDeleteRepOrderEquityCalled_thenSuccessfullyDeleteRepOrderEquity() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_ENDPOINT_FORMAT + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockRepOrderEquityService, times(1)).delete(ID);
    }

    private String repOrderEquityData() {
        return "{\n" +
                "\"repId\": "+REP_ID+",\n" +
                "\"asstAssetStatus\": null,\n" +
                "\"verifiedBy\": null,\n" +
                "\"verifiedDate\": null,\n" +
                "\"dateEntered\": null,\n" +
                "\"undeclared\": \"N\",\n" +
                "\"userCreated\": \"test-s\",\n" +
                "\"active\": \"Y\"\n" +
            "}";
    }
}
