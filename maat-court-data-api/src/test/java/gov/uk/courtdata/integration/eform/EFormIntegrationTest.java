package gov.uk.courtdata.integration.eform;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.testutils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class EFormIntegrationTest extends MockMvcIntegrationTest {

    private static final int USN = 7000001;
    private static final String TYPE = "CRM14";
    private static final int MAAT_REF = 5676558;
    private static final String USER_CREATED = "MLA";
    private static final String EFORM_URL = "/api/eform/";
    private static final String EFORM_USN_NOT_PROVIDED_URL = EFORM_URL;
    private static final String EFORM_USN_PROVIDED_URL = EFORM_URL + USN;
    private static final String NONEXISTENT_USN_MESSAGE = String.format("The USN [%d] does not exist in the data store.", USN);
    private static final String ALREADY_EXISTS_USN_MESSAGE = String.format("The USN [%d] already exists in the data store.", USN);
    private static final String NONEXISTENT_USN_RETURN = "{\"code\":\"NOT_FOUND\",\"message\":\"" + NONEXISTENT_USN_MESSAGE + "\"}";
    private static final String ALREADY_EXISTS_USN_RETURN = "{\"code\":\"BAD_REQUEST\",\"message\":\"" + ALREADY_EXISTS_USN_MESSAGE + "\"}";

    private String xmlDoc;
    private EformsStagingEntity eformsStagingEntity;

    @BeforeEach
    public void setUp() throws Exception {
        xmlDoc = FileUtils.readResourceToString("eform/request/xmlDoc_default.xml");
        eformsStagingEntity = EformsStagingEntity
                .builder()
                .usn(USN)
                .type(TYPE)
                .maatRef(MAAT_REF)
                .xmlDoc(xmlDoc)
                .userCreated(USER_CREATED)
                .build();
    }

    @Test
    void givenAUSN_whenPOSTeformCalled_thenNewFieldI55sInDB() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(EFORM_USN_PROVIDED_URL)
                .content("<InvalidXML//<<")
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenAUSN_whenPOSTeformCalled_thenNewFieldIsInDB() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(EFORM_USN_PROVIDED_URL)
                .content(xmlDoc)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenNoUSN_whenPOSTeformCalled_thenErrorReturned() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(EFORM_USN_NOT_PROVIDED_URL)
                .content(xmlDoc)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenExistingUSN_whenPOSTeformCalled_thenErrorReturned() throws Exception {
        repos.eformStaging.saveAndFlush(eformsStagingEntity);

        MockHttpServletRequestBuilder requestBuilder = post(EFORM_USN_PROVIDED_URL)
                .content(xmlDoc)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(ALREADY_EXISTS_USN_RETURN));
    }

    @Test
    void givenAUSN_whenGETeformCalled_thenReturnEntryFromDB() throws Exception {
        repos.eformStaging.saveAndFlush(eformsStagingEntity);

        String type = String.format("\"%s\"", TYPE);
        MockHttpServletRequestBuilder requestBuilder = get(EFORM_USN_PROVIDED_URL)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":" + USN + ",\"type\":" + type + ", \"maatRef\": " + MAAT_REF + "}"))
                .andExpect(content().json("{\"userCreated\":" + USER_CREATED + "}"));
    }

    @Test
    void givenNoUSN_whenGETeformCalled_thenReturnError() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(EFORM_USN_NOT_PROVIDED_URL)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenANonExistingUSN_whenGETeformCalled_thenReturnError() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(EFORM_USN_PROVIDED_URL)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(NONEXISTENT_USN_RETURN));
    }

    @Test
    void givenAUSN_whenDELETEeformCalled_thenEntryRemovedFromDB() throws Exception {
        repos.eformStaging.saveAndFlush(eformsStagingEntity);

        MockHttpServletRequestBuilder requestBuilder = delete(EFORM_USN_PROVIDED_URL)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenNoUSN_whenDELETEeFormCalled_thenErrorReturned() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(EFORM_USN_NOT_PROVIDED_URL)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void givenANonExistentUSN_whenDELETEeFormCalled_thenReturnError() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(EFORM_USN_PROVIDED_URL)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(NONEXISTENT_USN_RETURN));
    }
}
