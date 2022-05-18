package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.*;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.ChildWeightHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDetailsHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.FinancialAssessmentsHistoryEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentHistoryServiceTest {

    @InjectMocks
    private FinancialAssessmentHistoryService financialAssessmentHistoryService;

    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Mock
    private RepOrderImpl repOrderImpl;

    @Mock
    private FinancialAssessmentHistoryImpl financialAssessmentsHistoryImpl;

    @Mock
    private FinancialAssessmentDetailsHistoryImpl financialAssessmentDetailsHistoryImpl;

    @Mock
    private ChildWeightHistoryImpl childWeightHistoryImpl;

    @Mock
    private FinancialAssessmentHistoryMapper assessmentHistoryMapper;

    private FinancialAssessmentEntity assessmentEntity;
    private FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO;
    private FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO;
    private ChildWeightHistoryDTO childWeightHistoryDTO;

    private static final int MOCK_FINANCIAL_ASSESSMENT_ID = 1000;

    @Before
    public void setup() {
        assessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntityWithRelationships();
        FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity = TestEntityDataBuilder.getFinancialAssessmentsHistoryEntity();
        financialAssessmentsHistoryDTO = TestModelDataBuilder.getFinancialAssessmentsHistoryDTO();
        financialAssessmentDetailsHistoryDTO = TestModelDataBuilder.getFinancialAssessmentDetailsHistoryDTO();
        childWeightHistoryDTO = TestModelDataBuilder.getChildWeightHistoryDTO();

        when(financialAssessmentImpl.find(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(assessmentEntity);
        when(assessmentHistoryMapper.FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity)).thenReturn(financialAssessmentsHistoryDTO);
        when(financialAssessmentsHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID))
                .thenReturn(financialAssessmentsHistoryEntity);
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryIsCreated() {
        when(assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class)))
                .thenReturn(financialAssessmentDetailsHistoryDTO);
        when(assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class))).thenReturn(childWeightHistoryDTO);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(financialAssessmentsHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryWithoutFinAssessmentDetailsIsCreated() {
        when(assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class)))
                .thenReturn(financialAssessmentDetailsHistoryDTO);
        when(assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class))).thenReturn(childWeightHistoryDTO);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryWithoutChildWeightingsIsCreated() {
        when(assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class)))
                .thenReturn(financialAssessmentDetailsHistoryDTO);
        when(assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class))).thenReturn(childWeightHistoryDTO);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(financialAssessmentsHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class));
    }
}
