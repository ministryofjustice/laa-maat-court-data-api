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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinancialAssessmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FinancialAssessmentControllerTest {

    private static final String BASE_URL = "/api/internal/v1/assessment/financial-assessments";
    private static final String CHECK_OUTSTANDING_URL = BASE_URL + "/check-outstanding/{repId}";
    private static final String MEANS_ASSESSOR_DETAILS_URL = BASE_URL + "/{financialAssessmentId}/means-assessor-details";
    private static final String ROLLBACK_URL = BASE_URL + "/rollback/{financialAssessmentId}";

    private static final Integer MOCK_ASSESSMENT_ID = 1234;
    private static final Integer FAKE_ASSESSMENT_ID = 7123;
    private static final Integer OUTSTANDING_ASSESSMENT_REP_ID = 9999;
    private static final Integer NO_OUTSTANDING_ASSESSMENTS_REP_ID = 9998;
    private static final Integer MOCK_FINANCIAL_ASSESSMENT_ID = 1234;

    private final FinancialAssessmentMapper financialAssessmentMapper = new FinancialAssessmentMapperImpl();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FinancialAssessmentValidationProcessor financialAssessmentValidationProcessor;

    @MockBean
    private FinancialAssessmentService financialAssessmentService;

    @MockBean
    private FinancialAssessmentHistoryService financialAssessmentHistoryService;

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

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(financialAssessmentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(MOCK_ASSESSMENT_ID)));
    }

    @Test
    public void givenIncorrectParameters_whenCreateFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(FinancialAssessment.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(BASE_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenSearchFinancialAssessmentIsInvoked_thenAssessmentIsReturned() throws Exception {
        FinancialAssessmentDTO returnedFinancialAssessment =
                financialAssessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(TestEntityDataBuilder.getFinancialAssessmentEntity());

        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenReturn(Optional.empty());
        when(financialAssessmentService.find(MOCK_ASSESSMENT_ID)).thenReturn(returnedFinancialAssessment);
        mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + MOCK_ASSESSMENT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(returnedFinancialAssessment.getId()));
    }

    @Test
    public void givenIncorrectParameters_whenSearchFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        when(financialAssessmentService.find(MOCK_ASSESSMENT_ID)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/" + FAKE_ASSESSMENT_ID))
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


        mvc.perform(MockMvcRequestBuilders.put(BASE_URL).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.assessmentType").value("FULL"));
    }

    @Test
    public void givenIncorrectParameters_whenUpdateFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(FinancialAssessment.class))).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.put(BASE_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenDeleteFinancialAssessmentIsInvoked_thenAssessmentIsDeleted() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenReturn(Optional.empty());
        doNothing().when(financialAssessmentService).delete(MOCK_ASSESSMENT_ID);
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + MOCK_ASSESSMENT_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void givenIncorrectParameters_whenDeleteFinancialAssessmentIsInvoked_thenErrorIsThrown() throws Exception {
        when(financialAssessmentValidationProcessor.validate(any(Integer.class))).thenThrow(new ValidationException());
        doNothing().when(financialAssessmentService).delete(MOCK_ASSESSMENT_ID);
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + FAKE_ASSESSMENT_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenRepIdForOutstandingFinancialAssessment_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingFinancialAssessmentsFoundResultIsReturned() throws Exception {
        OutstandingAssessmentResultDTO result = new OutstandingAssessmentResultDTO(true, FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);

        when(financialAssessmentService.checkForOutstandingAssessments(OUTSTANDING_ASSESSMENT_REP_ID)).thenReturn(result);
        mvc.perform(MockMvcRequestBuilders.get(CHECK_OUTSTANDING_URL, OUTSTANDING_ASSESSMENT_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outstandingAssessments").value(result.isOutstandingAssessments()))
                .andExpect(jsonPath("$.message").value(result.getMessage()));
    }

    @Test
    public void givenRepIdForOutstandingPassportAssessment_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingFinancialAssessmentsFoundResultIsReturned() throws Exception {
        OutstandingAssessmentResultDTO result = new OutstandingAssessmentResultDTO(true, FinancialAssessmentImpl.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND);

        when(financialAssessmentService.checkForOutstandingAssessments(OUTSTANDING_ASSESSMENT_REP_ID)).thenReturn(result);
        mvc.perform(MockMvcRequestBuilders.get(CHECK_OUTSTANDING_URL, OUTSTANDING_ASSESSMENT_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outstandingAssessments").value(result.isOutstandingAssessments()))
                .andExpect(jsonPath("$.message").value(result.getMessage()));
    }

    @Test
    public void givenRepIdForNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenNotFoundResultIsReturned() throws Exception {
        OutstandingAssessmentResultDTO result = new OutstandingAssessmentResultDTO();

        when(financialAssessmentService.checkForOutstandingAssessments(NO_OUTSTANDING_ASSESSMENTS_REP_ID)).thenReturn(result);
        mvc.perform(MockMvcRequestBuilders.get(CHECK_OUTSTANDING_URL, NO_OUTSTANDING_ASSESSMENTS_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.outstandingAssessments").value(result.isOutstandingAssessments()));
    }

    @Test
    public void givenCorrectParameters_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryIsCreated() throws Exception {
        doNothing().when(financialAssessmentHistoryService).createAssessmentHistory(MOCK_ASSESSMENT_ID, true);

        mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/history/1234/fullAvailable/true"))
                .andExpect(status().isOk());
        verify(financialAssessmentHistoryService).createAssessmentHistory(MOCK_ASSESSMENT_ID, true);
    }

    @Test
    public void givenIncorrectFullAvailableParameter_whenCreateAssessmentHistoryIsInvoked_then400ErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/history/1234/fullAvailable/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidFinancialAssessmentId_whenFindMeansAssessorDetailsIsInvoked_thenPopulatedAssessorDetailsAreReturned() throws Exception {
        int financialAssessmentId = 1234;
        when(financialAssessmentService.findMeansAssessorDetails(financialAssessmentId))
                .thenReturn(TestModelDataBuilder.getAssessorDetails());

        mvc.perform(MockMvcRequestBuilders.get(MEANS_ASSESSOR_DETAILS_URL, financialAssessmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Karen Greaves"))
                .andExpect(jsonPath("$.userName").value("grea-k"));
    }

    @Test
    public void givenUnknownFinancialAssessmentId_whenFindMeansAssessorDetailsIsInvoked_thenNotFoundErrorIsReturned() throws Exception {
        int unknownFinancialAssessmentId = 99999;
        when(financialAssessmentService.findMeansAssessorDetails(unknownFinancialAssessmentId))
                .thenThrow(new RequestedObjectNotFoundException("Unable to find AssessorDetails with financialAssessmentId: [99999]"));

        mvc.perform(MockMvcRequestBuilders.get(MEANS_ASSESSOR_DETAILS_URL, unknownFinancialAssessmentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Unable to find AssessorDetails with financialAssessmentId: [99999]"));
    }

    @Test
    public void givenCorrectParameters_whenUpdateFinancialAssessmentsIsInvoked_thenAssessmentsIsUpdated() throws Exception {
        UpdateFinancialAssessment financialAssessment = UpdateFinancialAssessment.builder().fassInitStatus("FAIL").build();
        doNothing().when(financialAssessmentService).updateFinancialAssessments(MOCK_FINANCIAL_ASSESSMENT_ID, financialAssessment);
        String requestJson = "{\"fassInitStatus\":\"FAIL\"}";
        mvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/" + MOCK_FINANCIAL_ASSESSMENT_ID).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(financialAssessmentService).updateFinancialAssessments(MOCK_FINANCIAL_ASSESSMENT_ID, financialAssessment);
    }

    @Test
    public void givenIncorrectFullAvailableParameter_whenUpdateFinancialAssessmentsIsInvoked_then400ErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/" + "MOCK_FINANCIAL_ASSESSMENT_ID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidFinancialAssessmentId_whenRollbackAssessmentIsInvoked_thenAssessmentIsUpdated() throws Exception {
        doNothing().when(financialAssessmentService).patchFinancialAssessment(MOCK_FINANCIAL_ASSESSMENT_ID, Map.of("fassInitStatus", "IN PROGRESS"));

        String requestJson = "{\"fassInitStatus\":\"IN PROGRESS\"}";
        mvc.perform(MockMvcRequestBuilders.patch(ROLLBACK_URL, MOCK_ASSESSMENT_ID).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(financialAssessmentService).patchFinancialAssessment(MOCK_FINANCIAL_ASSESSMENT_ID, Map.of("fassInitStatus", "IN PROGRESS"));
    }
}
