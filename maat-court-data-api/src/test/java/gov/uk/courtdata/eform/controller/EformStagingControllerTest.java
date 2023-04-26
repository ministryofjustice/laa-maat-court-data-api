package gov.uk.courtdata.eform.controller;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.mapper.EformStagingDTOMapper;
import gov.uk.courtdata.eform.model.EformStagingResponse;
import gov.uk.courtdata.eform.repository.entity.EformsStagingEntity;
import gov.uk.courtdata.eform.service.EformStagingDAO;
import gov.uk.courtdata.eform.validator.TypeValidator;
import gov.uk.courtdata.eform.validator.UsnValidator;
import gov.uk.courtdata.exception.USNValidationException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EformStagingController.class)
class EformStagingControllerTest {

    private static final String ENDPOINT_FORMAT = "/api/eform/";
    private static final int USN = 123;
    private static final String TYPE = "CRM14";
    private static final EformStagingResponse EFORM_STAGING_RESPONSE = EformStagingResponse.builder().usn(USN).type(TYPE).build();
    private static final EformStagingDTO EFORM_STAGING_DTO = EformStagingDTO.builder().usn(USN).type(TYPE).build();
    private static final USNValidationException USN_VALIDATION_EXCEPTION =
            new USNValidationException("The USN number is not valid as it is not present in the eForm Repository");

    @MockBean
    private EformStagingDAO mockEFormStagingDAO;

    @MockBean
    private EformStagingDTOMapper mockEformStagingDTOMapper;

    @MockBean
    private UsnValidator mockUsnValidator;

    @MockBean
    private TypeValidator mockTypeValidator;

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
    void shouldSuccessfullyUpdateEformApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailToUpdateEformApplicationAsUsnValidationFails() throws Exception {
        doThrow(USN_VALIDATION_EXCEPTION)
                .when(mockUsnValidator).verifyUsnExists(USN);

        mvc.perform(MockMvcRequestBuilders.patch(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"BAD_REQUEST\",\"message\":\"The USN number is not valid as it is not present in the eForm Repository\"}"));
    }

    @Test
    void shouldSuccessfullyGetEformApplication() throws Exception {
        when(mockEFormStagingDAO.retrieve(USN))
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
        when(mockEFormStagingDAO.retrieve(USN))
                .thenThrow(USN_VALIDATION_EXCEPTION);

        mvc.perform(MockMvcRequestBuilders.get(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":\"BAD_REQUEST\",\"message\":\"The USN number is not valid as it is not present in the eForm Repository\"}"));
    }

    @Test
    void shouldSuccessfullyDeleteEformApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(mockEFormStagingDAO, times(1)).delete(USN);
    }

    @Test
    void shouldSuccessfullyCreateEformApplication() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(url())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @NotNull
    private String url() {
        return ENDPOINT_FORMAT + USN;
    }
}