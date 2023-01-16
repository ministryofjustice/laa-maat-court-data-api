package gov.uk.courtdata.wqhearing.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.wqhearing.service.WQHearingService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WQHearingController.class)
class WQHearingControllerTest {

    @MockBean
    private WQHearingService wqHearingService;
    @Autowired
    private MockMvc mvc;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/wq-hearing/";

    @Test
    void givenIncorrectParameters_whenFindByMaatIdAndHearingUUIDIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenFindByMaatIdAndHearingUUIDIsInvoked_thenReturnOffence() throws Exception {
        List wqHearingDTOList = List.of(TestModelDataBuilder.getWQHearingDTO(313123));
        when(wqHearingService.findByMaatIdAndHearingUUID(TestModelDataBuilder.REP_ID, TestModelDataBuilder.TEST_OFFENCE_ID)).thenReturn(wqHearingDTOList);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + TestModelDataBuilder.TEST_OFFENCE_ID + "/maatId/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].maatId").value(String.valueOf(TestModelDataBuilder.REP_ID)));
    }

}