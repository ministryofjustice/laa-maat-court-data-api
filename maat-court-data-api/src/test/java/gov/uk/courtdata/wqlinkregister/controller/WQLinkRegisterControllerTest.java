package gov.uk.courtdata.wqlinkregister.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.wqlinkregister.service.WQLinkRegisterService;
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
@WebMvcTest(WQLinkRegisterController.class)
class WQLinkRegisterControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WQLinkRegisterService wqLinkRegisterService;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/wq-link-register";

    @Test
    void givenIncorrectParameters_whenFindOffenceByMaatIdIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenFindOffenceByMaatIdInvoked_thenReturnOffence() throws Exception {
        List wqLinkRegisterDTOList = List.of(TestModelDataBuilder.getWQLinkRegisterDTO(313123));
        when(wqLinkRegisterService.findByMaatId(TestModelDataBuilder.REP_ID)).thenReturn(wqLinkRegisterDTOList);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].maatId").value(String.valueOf(TestModelDataBuilder.REP_ID)));
    }
}