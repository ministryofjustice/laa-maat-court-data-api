package gov.uk.courtdata.integration.eform;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
@WebAppConfiguration
class EFormIntegrationTest {

    private static final int USN = 12334455;
    private static final int NEW_USN = 3321;
    private static final String TYPE = "CRM14";
    private static final int MAAT_ID = 12334455;
    private static final EformsStagingEntity EFORMS_STAGING_ENTITY = EformsStagingEntity
            .builder()
            .usn(USN)
            .type(TYPE)
            .maat_id(MAAT_ID)
            .build();
    private static final String BASE_URL = "/api";
    private static final String EFORM_URL = BASE_URL + "/eform/";
    private static final String EFORM_USN_NOT_PROVIDED_URL = EFORM_URL;
    private static final String EFORM_USN_PROVIDED_URL = EFORM_URL + USN;
    private static final String NOT_VALID_USN_MESSAGE = String.format("The USN [%d] is not valid.", USN);
    private static final String NOT_VALID_USN_RETURN = "{\"code\":\"BAD_REQUEST\",\"message\":\"" + NOT_VALID_USN_MESSAGE + "\"}";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private EformStagingRepository eformStagingRepository;


    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        eformStagingRepository.deleteAll();
    }

    @Test
    void givenAUSN_whenPOSTeformCalled_thenNewFieldIsInDB() throws Exception {
        mockMvc.perform(post(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenNoUSN_whenPOSTeformCalled_thenErrorReturned() throws Exception {
        mockMvc.perform(post(EFORM_USN_NOT_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenExistingUSN_whenPOSTeformCalled_thenErrorReturned() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        mockMvc.perform(post(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(NOT_VALID_USN_RETURN));
    }

    @Test
    void givenAUSN_whenGETeformCalled_thenReturnEntryFromDB() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        mockMvc.perform(get(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":" + USN + ",\"type\":" + TYPE + ", \"maat_id\": " + MAAT_ID + "}"));
    }

    @Test
    void givenNoUSN_whenGETeformCalled_thenReturnError() throws Exception {
        mockMvc.perform(get(EFORM_USN_NOT_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenANonExistingUSN_whenGETeformCalled_thenReturnError() throws Exception {
        mockMvc.perform(get(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(NOT_VALID_USN_RETURN));
    }

    @Test
    void givenAUSN_whenDELETEeformCalled_thenEntryRemovedFromDB() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        mockMvc.perform(delete(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenNoUSN_whenDELETEeFormCalled_thenErrorReturned() throws Exception {
        mockMvc.perform(delete(EFORM_USN_NOT_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenANonExistentUSN_whenDELETEeFormCalled_thenReturnError() throws Exception {
        mockMvc.perform(delete(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(NOT_VALID_USN_RETURN));
    }
}
