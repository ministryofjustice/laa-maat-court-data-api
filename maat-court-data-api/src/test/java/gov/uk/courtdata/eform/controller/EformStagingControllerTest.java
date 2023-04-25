package gov.uk.courtdata.eform.controller;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EformStagingController.class)
class EformStagingControllerTest {

    private static final String ENDPOINT_FORMAT = "/api/eform/";

    @Autowired
    private MockMvc mvc;

    @Test
    void updateEformApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(createURL(123))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getEformsApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(createURL(123))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteEformsApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(createURL(123))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createEformsApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(createURL(123))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @NotNull
    private String createURL(int usn) {
        return ENDPOINT_FORMAT + usn;
    }
}