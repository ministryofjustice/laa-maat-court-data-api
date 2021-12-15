package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapperImpl;
import gov.uk.courtdata.assessment.service.FinancialAssessmentService;
import gov.uk.courtdata.assessment.validator.FinancialAssessmentValidationProcessor;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.FinancialAssessment;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FinancialAssessmentController.class)
public class FinancialAssessmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FinancialAssessmentValidationProcessor financialAssessmentValidationProcessor;

    @MockBean
    private FinancialAssessmentService financialAssessmentService;

    private final FinancialAssessmentMapper financialAssessmentMapper = new FinancialAssessmentMapperImpl();

    private String financialAssessmentJson;

    private final Integer MOCK_ASSESSMENT_ID = 1234;
    private final Integer FAKE_ASSESSMENT_ID = 7123;

    private final String endpointUrl = "/financial-assessments";

    @Before
    public void setUp() {
        financialAssessmentJson = TestModelDataBuilder.getCreateFinancialAssessmentJson();
    }

    @Test
    public void givenCorrectParameters_whenCreateFinancialAssessmentIsInvoked_thenAssessmentIsCreated() throws Exception {
        FinancialAssessmentDTO returnedFinancialAssessment =
                financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(TestEntityDataBuilder.getFinancialAssessmentEntity());
        returnedFinancialAssessment.setId(MOCK_ASSESSMENT_ID);

        when(financialAssessmentService.createAssessment(any())).thenReturn(returnedFinancialAssessment);
        when(financialAssessmentValidationProcessor.validate(any(FinancialAssessment.class))).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content(financialAssessmentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_ASSESSMENT_ID)));
    }

    @Test
    public void givenIncorrectParameters_whenCreateFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(FinancialAssessment.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenSearchFinancialAssessmentIsInvoked_thenAssessmentIsReturned() throws Exception {
        FinancialAssessmentDTO returnedFinancialAssessment =
                financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(TestEntityDataBuilder.getFinancialAssessmentEntity());

        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenReturn(Optional.empty());
        when(financialAssessmentService.getAssessment(MOCK_ASSESSMENT_ID)).thenReturn(returnedFinancialAssessment);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + MOCK_ASSESSMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(returnedFinancialAssessment.getId()));

    }

    @Test
    public void givenIncorrectParameters_whenSearchFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        when(financialAssessmentService.getAssessment(MOCK_ASSESSMENT_ID)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl + "/" + FAKE_ASSESSMENT_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenUpdateFinancialAssessmentIsInvoked_thenAssessmentIsUpdated() throws Exception {
        FinancialAssessmentDTO returnedFinancialAssessment =
                financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(TestEntityDataBuilder.getFinancialAssessmentEntity());
        returnedFinancialAssessment.setAssessmentType("FULL");

        when(financialAssessmentValidationProcessor.validate(any(FinancialAssessment.class))).thenReturn(Optional.empty());
        when(financialAssessmentService.updateAssessment(any())).thenReturn(returnedFinancialAssessment);
        String requestJson = TestModelDataBuilder.getUpdateFinancialAssessmentJson();


        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.assessmentType").value("FULL"));
    }

    @Test
    public void givenIncorrectParameters_whenUpdateFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(FinancialAssessment.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.put(endpointUrl).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenDeleteFinancialAssessmentIsInvoked_thenAssessmentIsDeleted() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenReturn(Optional.empty());
        doNothing().when(financialAssessmentService).deleteAssessment(MOCK_ASSESSMENT_ID);
        mvc.perform(MockMvcRequestBuilders.delete(endpointUrl + "/" + MOCK_ASSESSMENT_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void givenIncorrectParameters_whenDeleteFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        doNothing().when(financialAssessmentService).deleteAssessment(MOCK_ASSESSMENT_ID);
        mvc.perform(MockMvcRequestBuilders.delete(endpointUrl + "/" + FAKE_ASSESSMENT_ID))
                .andExpect(status().is4xxClientError());
    }
}
