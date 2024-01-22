package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.exception.USNExceptionUtil;
import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.eform.service.EformStagingService;
import gov.uk.courtdata.eform.validator.UsnValidator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EformStagingController.class)
@AutoConfigureMockMvc(addFilters = false)
class EformStagingControllerTest {

    private static final String ENDPOINT_FORMAT = "/api/eform/";
    private static final int USN = 123;
    private static final String CA_USER = "causer";
    private static final String TYPE = "CRM14";
    private static final EformStagingResponse EFORM_STAGING_RESPONSE = EformStagingResponse.builder().usn(USN).type(TYPE).build();
    private static final EformStagingDTO EFORM_STAGING_DTO = EformStagingDTO.builder().usn(USN).type(TYPE).userCreated(CA_USER).build();
    private static final UsnException USN_VALIDATION_EXCEPTION = USNExceptionUtil.nonexistent(987);

    @MockBean
    private EformStagingService mockEFormStagingService;

    @MockBean
    private EformStagingDTOMapper mockEformStagingDTOMapper;

    @MockBean
    private UsnValidator mockUsnValidator;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        // Setup some happy path stubbing behaviour, to be overridden when needed in non-happy path scenario tests
        when(mockEformStagingDTOMapper.toEformsStagingEntity(any(EformStagingDTO.class)))
                .thenReturn(EformsStagingEntity.builder().usn(USN).type(TYPE).build());
        when(mockEformStagingDTOMapper.toEformStagingDTO(any(EformsStagingEntity.class)))
                .thenReturn(EFORM_STAGING_DTO);
        when(mockEformStagingDTOMapper.toEformStagingResponse(any(EformStagingDTO.class)))
                .thenReturn(EFORM_STAGING_RESPONSE);
    }

    @Test
    void shouldSuccessfullyGetEformApplication() throws Exception {
        when(mockEFormStagingService.retrieve(USN))
                .thenReturn(EFORM_STAGING_DTO);
        when(mockEformStagingDTOMapper.toEformStagingResponse(EFORM_STAGING_DTO))
                .thenReturn(EFORM_STAGING_RESPONSE);

        mvc.perform(MockMvcRequestBuilders.get(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":123,\"type\":\"CRM14\"}"));
    }

    @Test
    void shouldFailToFindEformApplicationWhenItDoesNotExistInTheRepo() throws Exception {
        when(mockEFormStagingService.retrieve(USN))
                .thenThrow(USN_VALIDATION_EXCEPTION);

        mvc.perform(MockMvcRequestBuilders.get(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"code\":\"NOT_FOUND\",\"message\":\"The USN [987] does not exist in the data store.\"}"));
    }

    @Test
    void shouldSuccessfullyDeleteEformApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockEFormStagingService, times(1)).delete(USN);
    }

    @Test
    void shouldSuccessfullyCreateEformApplication() throws Exception {
        String requestBodyXML = "<formData xmlns=\"http://eforms.legalservices.gov.uk/lscservice\"></formData>";
        mvc.perform(MockMvcRequestBuilders.post(url()).content(requestBodyXML)
                        .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSuccessfullyVerifyAndInsertUsn() throws Exception {
        when(mockEFormStagingService.createOrRetrieve(USN, CA_USER))
                .thenReturn(EFORM_STAGING_DTO);
        when(mockEformStagingDTOMapper.toEformStagingResponse(EFORM_STAGING_DTO))
                .thenReturn(EformStagingResponse.builder().usn(USN).type(TYPE).userCreated(CA_USER).build());

        mvc.perform(MockMvcRequestBuilders.post("/api/eform/initialise/123?userCreated=causer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"usn\":123,\"type\":\"CRM14\",\"userCreated\":\"causer\"}"));
    }

    @NotNull
    private String url() {
        return ENDPOINT_FORMAT + USN;
    }

    @Test
    void shouldSuccessfullyUpdateEformDecisionHistory() throws Exception {
        EformsStagingEntity eformsStaging = EformsStagingEntity.builder().maatRef(458658).build();
        String requestJson = "{\"maatRef\":458658}";
        mvc.perform(MockMvcRequestBuilders.patch(url()).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mockEFormStagingService, times(1)).updateEformStagingFields(USN, eformsStaging);
    }

}