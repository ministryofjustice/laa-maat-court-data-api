package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapperImpl;
import gov.uk.courtdata.assessment.service.PassportAssessmentService;
import gov.uk.courtdata.assessment.validator.PassportAssessmentValidationProcessor;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.PassportAssessment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PassportAssessmentController.class)
public class PassportAssessmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PassportAssessmentValidationProcessor passportAssessmentValidationProcessor;

    @MockBean
    private PassportAssessmentService passportAssessmentService;

    private final PassportAssessmentMapper passportAssessmentMapper = new PassportAssessmentMapperImpl();

    private String passportAssessmentJson;

    private static final Integer MOCK_ASSESSMENT_ID = 1234;
    private static final Integer FAKE_ASSESSMENT_ID = 7123;
    private static final int MOCK_REP_ID = 5678;
    private static final int INVALID_REP_ID = 3456;

    private final String endpointUrl = "/api/internal/v1/assessments/passport-assessments";

    @Before
    public void setUp() {
        passportAssessmentJson = TestModelDataBuilder.getCreatePassportAssessmentJson();
    }

    @Test
    public void givenCorrectParameters_whenCreatePassportAssessmentIsInvoked_thenPassportAssessmentIsCreated() throws Exception {
        PassportAssessmentDTO returnedPassportAssessment =
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(TestEntityDataBuilder.getPassportAssessmentEntity());
        returnedPassportAssessment.setId(MOCK_ASSESSMENT_ID);

        when(passportAssessmentService.create(any())).thenReturn(returnedPassportAssessment);
        when(passportAssessmentValidationProcessor.validate(any(PassportAssessment.class))).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(passportAssessmentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_ASSESSMENT_ID)));
    }

    @Test
    public void givenIncorrectParameters_whenCreatePassportAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(passportAssessmentValidationProcessor.validate(any(PassportAssessment.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenSearchPassportAssessmentIsInvoked_thenPassportAssessmentIsReturned() throws Exception {
        PassportAssessmentDTO returnedPassportAssessment =
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(TestEntityDataBuilder.getPassportAssessmentEntity());

        when(passportAssessmentValidationProcessor.validate(any(Integer.class))).thenReturn(Optional.empty());
        when(passportAssessmentService.find(MOCK_ASSESSMENT_ID)).thenReturn(returnedPassportAssessment);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + MOCK_ASSESSMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(returnedPassportAssessment.getId()));

    }

    @Test
    public void givenIncorrectParameters_whenSearchPassportAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(passportAssessmentValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        when(passportAssessmentService.find(MOCK_ASSESSMENT_ID)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl + "/" + FAKE_ASSESSMENT_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenGetPassportAssessmentByRepIdIsInvoked_thenPassportAssessmentIsReturned() throws Exception {
        PassportAssessmentDTO returnedPassportAssessment =
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(TestEntityDataBuilder.getPassportAssessmentEntity());

        when(passportAssessmentService.findByRepId(MOCK_REP_ID)).thenReturn(returnedPassportAssessment);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/repId/" + MOCK_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.repId").value(returnedPassportAssessment.getRepId()));

        verify(passportAssessmentService).findByRepId(MOCK_REP_ID);
    }

    @Test
    public void givenInvalidRepId_whenGetPassportAssessmentByRepIdIsInvoked_then404NotFoundErrorIsThrown() throws Exception {
        when(passportAssessmentService.findByRepId(INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException(String.format("Passport Assessment with repId %s not found", INVALID_REP_ID)));

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/repId/" + INVALID_REP_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNullRepId_whenGetPassportAssessmentByRepIdIsInvoked_thenBadRequestIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/repId/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCorrectParameters_whenUpdatePassportAssessmentIsInvoked_thenPassportAssessmentIsUpdated() throws Exception {
        PassportAssessmentDTO returnedPassportAssessment =
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(TestEntityDataBuilder.getPassportAssessmentEntity());

        when(passportAssessmentValidationProcessor.validate(any(PassportAssessment.class))).thenReturn(Optional.empty());
        when(passportAssessmentService.update(any())).thenReturn(returnedPassportAssessment);
        String requestJson = TestModelDataBuilder.getUpdatePassportAssessmentJson();


        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pcobConfirmation").value("DWP"));
    }

    @Test
    public void givenIncorrectParameters_whenUpdatePassportAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(passportAssessmentValidationProcessor.validate(any(PassportAssessment.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}
