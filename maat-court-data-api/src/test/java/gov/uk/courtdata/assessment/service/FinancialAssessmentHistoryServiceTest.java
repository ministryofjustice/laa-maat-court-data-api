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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentHistoryServiceTest {

    @InjectMocks
    private FinancialAssessmentHistoryService financialAssessmentHistoryService;
    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;
    @Mock
    private FinancialAssessmentDetailsImpl financialAssessmentDetailsImpl;
    @Mock
    private ChildWeightingsImpl childWeightingsImpl;
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
    private FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity;
    private List<FinancialAssessmentDetailEntity> assessmentDetailsEntities;
    private ChildWeightingsEntity childWeightingsEntity;

    private FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO;
    private FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO;
    private ChildWeightHistoryDTO childWeightHistoryDTO;

    private static final int MOCK_FINANCIAL_ASSESSMENT_ID = 1000;

    @Before
    public void setup() {
        assessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessmentsHistoryEntity = TestEntityDataBuilder.getFinancialAssessmentsHistoryEntity();
        assessmentDetailsEntities = Arrays.asList(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity());
        childWeightingsEntity = TestEntityDataBuilder.getChildWeightingsEntity();

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
        when(financialAssessmentDetailsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(assessmentDetailsEntities);
        when(assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class)))
                .thenReturn(financialAssessmentDetailsHistoryDTO);
        when(childWeightingsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(Arrays.asList(childWeightingsEntity));
        when(assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class))).thenReturn(childWeightHistoryDTO);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(financialAssessmentsHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(financialAssessmentDetailsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class));
        verify(childWeightingsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class));
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryWithoutFinAssessmentDetailsIsCreated() {
        when(financialAssessmentDetailsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(null);
        when(childWeightingsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(Arrays.asList(childWeightingsEntity));
        when(assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class))).thenReturn(childWeightHistoryDTO);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(financialAssessmentsHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(financialAssessmentDetailsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper, times(0)).FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class));
        verify(childWeightingsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class));
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryWithoutChildWeightingsIsCreated() {
        when(financialAssessmentDetailsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(assessmentDetailsEntities);
        when(assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class)))
                .thenReturn(financialAssessmentDetailsHistoryDTO);
        when(childWeightingsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(null);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(financialAssessmentsHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(financialAssessmentDetailsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class));
        verify(childWeightingsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper, times(0)).ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class));
    }
}
