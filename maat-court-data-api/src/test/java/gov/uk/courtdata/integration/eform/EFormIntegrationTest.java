package gov.uk.courtdata.integration.eform;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
@WebAppConfiguration
class EFormIntegrationTest {

    private static final int USN = 7000001;
    private static final String TYPE = "CRM14";
    private static final int MAAT_REF = 5676558;
    private static final String USER_CREATED = "MLA";
    private static final String BASE_URL = "/api";
    private static final String EFORM_URL = BASE_URL + "/eform/";
    private static final String EFORM_USN_NOT_PROVIDED_URL = EFORM_URL;
    private static final String EFORM_USN_PROVIDED_URL = EFORM_URL + USN;
    private static final String NONEXISTENT_USN_MESSAGE = String.format("The USN [%d] does not exist in the data store.", USN);
    private static final String ALREADY_EXISTS_USN_MESSAGE = String.format("The USN [%d] already exists in the data store.", USN);
    private static final String NONEXISTENT_USN_RETURN = "{\"code\":\"NOT_FOUND\",\"message\":\"" + NONEXISTENT_USN_MESSAGE + "\"}";
    private static final String ALREADY_EXISTS_USN_RETURN = "{\"code\":\"BAD_REQUEST\",\"message\":\"" + ALREADY_EXISTS_USN_MESSAGE + "\"}";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private EformStagingRepository eformStagingRepository;

    private String xmlDoc;
    private EformsStagingEntity eformsStagingEntity;

    @BeforeEach
    public void setUp() throws Exception {
        eformStagingRepository.deleteAllInBatch();
        eformStagingRepository.flush();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        xmlDoc = readTestResource("eform/request/xmlDoc_default.xml");
        eformsStagingEntity = EformsStagingEntity
                .builder()
                .usn(USN)
                .type(TYPE)
                .maatRef(MAAT_REF)
                .xmlDoc(xmlDoc)
                .userCreated(USER_CREATED)
                .build();
    }

    @NotNull
    private String readTestResource(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(path);
        InputStreamReader streamReader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        StringBuilder fileContents = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            fileContents.append(line);
        }
        bufferedReader.close();
        streamReader.close();
        resourceAsStream.close();
        return fileContents.toString();
    }

    @AfterEach
    public void clearUp() {
        eformStagingRepository.deleteAllInBatch();
        eformStagingRepository.flush();
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
        eformStagingRepository.saveAndFlush(eformsStagingEntity);

        MockHttpServletRequestBuilder requestBuilder = post(EFORM_USN_PROVIDED_URL)
                .content(xmlDoc)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(ALREADY_EXISTS_USN_RETURN));
    }

    @Test
    void givenAUSN_whenGETeformCalled_thenReturnEntryFromDB() throws Exception {
        eformStagingRepository.saveAndFlush(eformsStagingEntity);

        String type = String.format("\"%s\"", TYPE);
        MockHttpServletRequestBuilder requestBuilder = get(EFORM_USN_PROVIDED_URL)
                .contentType(MediaType.APPLICATION_XML);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":" + USN + ",\"type\":" + type + ", \"maatRef\": " + MAAT_REF + "}"));
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
        eformStagingRepository.saveAndFlush(eformsStagingEntity);

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
