package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapperImpl;
import gov.uk.courtdata.assessment.service.FinancialAssessmentHistoryService;
import gov.uk.courtdata.assessment.service.FinancialAssessmentService;
import gov.uk.courtdata.assessment.validator.FinancialAssessmentValidationProcessor;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.FinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinancialAssessmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FinancialAssessmentControllerTest {

    private static final String CHECK_OUTSTANDING_URI = "/check-outstanding/";
    private static final Integer MOCK_ASSESSMENT_ID = 1234;
    private static final Integer FAKE_ASSESSMENT_ID = 7123;
    private static final Integer OUTSTANDING_ASSESSMENT_REP_ID = 9999;
    private static final Integer NO_OUTSTANDING_ASSESSMENTS_REP_ID = 9998;
    private static final String endpointUrl = "/api/internal/v1/assessment/financial-assessments";
    private final FinancialAssessmentMapper financialAssessmentMapper = new FinancialAssessmentMapperImpl();
    private static final Integer MOCK_FINANCIAL_ASSESSMENT_ID = 1234;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FinancialAssessmentValidationProcessor financialAssessmentValidationProcessor;

    @MockBean
    private FinancialAssessmentService financialAssessmentService;

    @MockBean
    private FinancialAssessmentHistoryService financialAssessmentHistoryService;

    @Mock
    private FinancialAssessmentMapper finAssessmentMapper;

    private String financialAssessmentJson;

    @BeforeEach
    public void setUp() {
        financialAssessmentJson = TestModelDataBuilder.getCreateFinancialAssessmentJson();
    }

    @Test
    public void givenCorrectParameters_whenCreateFinancialAssessmentIsInvoked_thenAssessmentIsCreated() throws Exception {
        FinancialAssessmentDTO returnedFinancialAssessment =
                financialAssessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(TestEntityDataBuilder.getFinancialAssessmentEntity());
        returnedFinancialAssessment.setId(MOCK_ASSESSMENT_ID);

        when(financialAssessmentService.create(any())).thenReturn(returnedFinancialAssessment);
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
                financialAssessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(TestEntityDataBuilder.getFinancialAssessmentEntity());

        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenReturn(Optional.empty());
        when(financialAssessmentService.find(MOCK_ASSESSMENT_ID)).thenReturn(returnedFinancialAssessment);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/" + MOCK_ASSESSMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(returnedFinancialAssessment.getId()));
    }

    @Test
    public void givenIncorrectParameters_whenSearchFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        when(financialAssessmentService.find(MOCK_ASSESSMENT_ID)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl + "/" + FAKE_ASSESSMENT_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenUpdateFinancialAssessmentIsInvoked_thenAssessmentIsUpdated() throws Exception {
        FinancialAssessmentDTO returnedFinancialAssessment =
                financialAssessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(TestEntityDataBuilder.getFinancialAssessmentEntity());
        returnedFinancialAssessment.setAssessmentType("FULL");

        when(financialAssessmentValidationProcessor.validate(any(FinancialAssessment.class))).thenReturn(Optional.empty());
        when(financialAssessmentService.update(any())).thenReturn(returnedFinancialAssessment);
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
        doNothing().when(financialAssessmentService).delete(MOCK_ASSESSMENT_ID);
        mvc.perform(MockMvcRequestBuilders.delete(endpointUrl + "/" + MOCK_ASSESSMENT_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void givenIncorrectParameters_whenDeleteFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        doNothing().when(financialAssessmentService).delete(MOCK_ASSESSMENT_ID);
        mvc.perform(MockMvcRequestBuilders.delete(endpointUrl + "/" + FAKE_ASSESSMENT_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenRepIdForOutstandingFinancialAssessment_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingFinancialAssessmentsFoundResultIsReturned() throws Exception {
        OutstandingAssessmentResultDTO result = new OutstandingAssessmentResultDTO(true, FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);

        when(financialAssessmentService.checkForOutstandingAssessments(OUTSTANDING_ASSESSMENT_REP_ID)).thenReturn(result);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + CHECK_OUTSTANDING_URI + OUTSTANDING_ASSESSMENT_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outstandingAssessments").value(result.isOutstandingAssessments()))
                .andExpect(jsonPath("$.message").value(result.getMessage()));
    }

    @Test
    public void givenRepIdForOutstandingPassportAssessment_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingFinancialAssessmentsFoundResultIsReturned() throws Exception {
        OutstandingAssessmentResultDTO result = new OutstandingAssessmentResultDTO(true, FinancialAssessmentImpl.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND);

        when(financialAssessmentService.checkForOutstandingAssessments(OUTSTANDING_ASSESSMENT_REP_ID)).thenReturn(result);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + CHECK_OUTSTANDING_URI + OUTSTANDING_ASSESSMENT_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outstandingAssessments").value(result.isOutstandingAssessments()))
                .andExpect(jsonPath("$.message").value(result.getMessage()));
    }

    @Test
    public void givenRepIdForNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenNotFoundResultIsReturned() throws Exception {
        OutstandingAssessmentResultDTO result = new OutstandingAssessmentResultDTO();

        when(financialAssessmentService.checkForOutstandingAssessments(NO_OUTSTANDING_ASSESSMENTS_REP_ID)).thenReturn(result);
        mvc.perform(MockMvcRequestBuilders.get(endpointUrl + CHECK_OUTSTANDING_URI + NO_OUTSTANDING_ASSESSMENTS_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outstandingAssessments").value(result.isOutstandingAssessments()));
    }

    @Test
    public void givenCorrectParameters_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryIsCreated() throws Exception {
        doNothing().when(financialAssessmentHistoryService).createAssessmentHistory(MOCK_ASSESSMENT_ID, true);

        mvc.perform(MockMvcRequestBuilders.post(endpointUrl + "/history/1234/fullAvailable/true"))
                .andExpect(status().isOk());
        verify(financialAssessmentHistoryService).createAssessmentHistory(MOCK_ASSESSMENT_ID, true);
    }

    @Test
    public void givenIncorrectFullAvailableParameter_whenCreateAssessmentHistoryIsInvoked_then400ErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(endpointUrl + "/history/1234/fullAvailable/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidFinancialAssessmentId_whenFindMeansAssessorDetailsIsInvoked_thenPopulatedAssessorDetailsAreReturned() throws Exception {
        int financialAssessmentId = 1234;
        when(financialAssessmentService.findMeansAssessorDetails(financialAssessmentId))
                .thenReturn(TestModelDataBuilder.getAssessorDetails());

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl +"/"+ financialAssessmentId +"/means-assessor-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Karen Greaves"))
                .andExpect(jsonPath("$.userName").value("grea-k"));
    }

    @Test
    public void givenUnknownFinancialAssessmentId_whenFindMeansAssessorDetailsIsInvoked_thenNotFoundErrorIsReturned() throws Exception {
        int unknownFinancialAssessmentId = 99999;
        when(financialAssessmentService.findMeansAssessorDetails(unknownFinancialAssessmentId))
                .thenThrow(new RequestedObjectNotFoundException("Unable to find AssessorDetails with financialAssessmentId: [99999]"));

        mvc.perform(MockMvcRequestBuilders.get(endpointUrl +"/"+ unknownFinancialAssessmentId +"/means-assessor-details"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Unable to find AssessorDetails with financialAssessmentId: [99999]"));
    }

    @Test
    public void givenCorrectParameters_whenUpdateFinancialAssessmentsIsInvoked_thenAssessmentsIsUpdated() throws Exception {
        UpdateFinancialAssessment financialAssessment = UpdateFinancialAssessment.builder().fassInitStatus("FAIL").build();
        doNothing().when(financialAssessmentService).updateFinancialAssessments(MOCK_FINANCIAL_ASSESSMENT_ID, financialAssessment);
        String requestJson = "{\"fassInitStatus\":\"FAIL\"}";
        mvc.perform(MockMvcRequestBuilders.patch(endpointUrl+ "/" + MOCK_FINANCIAL_ASSESSMENT_ID).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(financialAssessmentService).updateFinancialAssessments(MOCK_FINANCIAL_ASSESSMENT_ID, financialAssessment);
    }

    @Test
    public void givenIncorrectFullAvailableParameter_whenUpdateFinancialAssessmentsIsInvoked_then400ErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(endpointUrl + "/" + "MOCK_FINANCIAL_ASSESSMENT_ID"))
                .andExpect(status().isBadRequest());
    }
}
