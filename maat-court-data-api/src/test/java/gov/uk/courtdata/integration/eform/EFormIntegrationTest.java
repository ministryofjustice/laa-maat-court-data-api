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
public class EFormIntegrationTest {
    private static final int USN = 12334455;
    private static final int NEWUSN = 3321;
    private static final String TYPE = "CRM14";
    private static final EformsStagingEntity EFORMS_STAGING_ENTITY = EformsStagingEntity
            .builder()
            .usn(USN)
            .type(TYPE)
            .build();
    private static final EformsStagingEntity EFORMS_STAGING_ENTITY_NO_USN = EformsStagingEntity
            .builder()
            .type(TYPE)
            .build();
    private static final EformsStagingEntity NEW_EFORMS_STAGING_ENTITY = EformsStagingEntity
            .builder()
            .usn(NEWUSN)
            .type(TYPE)
            .build();
    private final String BASE_URL = "/api";
    private final String EFORM_URL = BASE_URL + "/eform/";
    private final String EFORM_USN_NOTPROVIDED_URL = EFORM_URL;
    private final String EFORM_USN_PROVIDED_URL = EFORM_URL + USN;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private EformStagingRepository eformStagingRepository;
    private String NOT_VALID_USN_MESSAGE = String.format("The USN number [%d] is not valid.", USN);
    private String NOT_VALID_USN_RETURN = "{\"code\":\"BAD_REQUEST\",\"message\":\"" + NOT_VALID_USN_MESSAGE + "\"}";

    @BeforeEach
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        eformStagingRepository.deleteAll();
    }

    //Create - Happy Path - USN and Type should appear in the DB
    //Create - Sad Path - USN appears in the DB already and error thrown

    //Update - Happy Path - USN should be changed to the new on provided in the parameter
    //Update - Sad Path - USN does not appear in the DB and error should be thrown

    //Retrieve - Happy Path - USN and Type should be returned from the DB to the caller
    //Retrieve - Sad Path - USN does not appear in the DB and should return an error

    //Delete - Happy Path - USN that was in the DB should no longer be there
    //Delete - Sad Path - USN not found in DB and should return nothing
    @Test
    public void givenAUSN_whenPOSTeformCalled_thenNewFieldIsInDB() throws Exception {
        this.mockMvc.perform(post(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoUSN_whenPOSTeformCalled_thenErrorReturned() throws Exception {
        this.mockMvc.perform(post(EFORM_USN_NOTPROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenExistingUSN_whenPOSTeformCalled_thenErrorReturned() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        this.mockMvc.perform(post(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(NOT_VALID_USN_RETURN));
    }

    @Test
    public void givenAUSNAndNewUSNProvided_whenPATCHefromCalled_thenUpdateUSNInDB() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        this.mockMvc.perform(patch(EFORM_USN_PROVIDED_URL)
                        .param("newUsn", String.valueOf(NEWUSN))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAUSNandNoNewUSNProvided_whenPATCHeformCalled_thenReturnError() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        this.mockMvc.perform(patch(EFORM_USN_NOTPROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenANonExsistingUSN_whenPATCHefromCalled_thenReturnError() throws Exception {
        this.mockMvc.perform(patch(EFORM_USN_PROVIDED_URL)
                        .param("newUsn", String.valueOf(NEWUSN))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(NOT_VALID_USN_RETURN));
    }

    @Test
    public void givenAUSN_whenGETeformCalled_thenReturnEnteryFromDB() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        this.mockMvc.perform(get(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":" + USN + ",\"type\":\"CRM14\"}"));
    }

    @Test
    public void givenNoUSN_whenGETeformCalled_thenReturnError() throws Exception {
        this.mockMvc.perform(get(EFORM_USN_NOTPROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenANonExsistingUSN_whenGETeformCalled_thenReturnError() throws Exception {
        this.mockMvc.perform(get(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(NOT_VALID_USN_RETURN));
    }

    @Test
    public void givenAUSN_whenDELETEeformCalled_thenEntryRemovedFromDB() throws Exception {
        eformStagingRepository.saveAndFlush(EFORMS_STAGING_ENTITY);

        this.mockMvc.perform(delete(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoUSN_whenDELETEeformCalled_thenErrorReturned() throws Exception {
        this.mockMvc.perform(delete(EFORM_USN_NOTPROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenANonExsistantUSN_whenDELETEeformCalled_thenReturnError() throws Exception {
        this.mockMvc.perform(delete(EFORM_USN_PROVIDED_URL)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(NOT_VALID_USN_RETURN));
    }
}
