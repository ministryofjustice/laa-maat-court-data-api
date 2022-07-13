package gov.uk.courtdata.assessment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.assessment.service.PostProcessingService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.PostProcessing;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostProcessingController.class)
public class PostProcessingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MaatIdValidator maatIdValidator;

    @MockBean
    private PostProcessingService postProcessingService;

    @Value("${api-endpoints.assessments-domain}")
    private String ASSESSMENTS_DOMAIN;

    private static final String POST_PROCESSING_ENDPOINT = "/post-processing/";

    private static final PostProcessing postProcessing = TestModelDataBuilder.getPostProcessing();

    public ResultActions performApiCall() throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post(ASSESSMENTS_DOMAIN + POST_PROCESSING_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postProcessing)));
    }

    @Test
    public void givenValidRepId_whenDoPostProcessingIsInvoked_thenPostProcessingIsPerformed() throws Exception {
        when(maatIdValidator.validate(anyInt())).thenReturn(Optional.empty());

        performApiCall()
                .andExpect(status().isOk());

        verify(postProcessingService).execute(postProcessing);
    }

    @Test
    public void givenInvalidRepId_whenDoPostProcessingIsInvoked_thenErrorIsThrown() throws Exception {
        when(maatIdValidator.validate(anyInt())).thenThrow(new ValidationException(TestModelDataBuilder.MAAT_ID + " is invalid."));

        performApiCall()
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()));
    }

    @Test
    public void givenRequiredParameters_whenDoPostProcessingFails_thenErrorIsThrown() throws Exception {
        doThrow(new JpaSystemException(new RuntimeException("Problem accessing DB")))
                .when(postProcessingService).execute(postProcessing);

        performApiCall()
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }
}
