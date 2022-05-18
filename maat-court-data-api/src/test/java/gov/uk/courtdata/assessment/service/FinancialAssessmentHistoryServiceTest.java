package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.*;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.ChildWeightHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
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

import java.util.ArrayList;
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
    private FinancialAssessmentHistoryImpl financialAssessmentHistoryImpl;
    @Mock
    private FinancialAssessmentHistoryMapper assessmentHistoryMapper;
    @Mock
    private FinancialAssessmentMapper assessmentMapper;

    private FinancialAssessmentEntity assessmentEntity;
    private List<FinancialAssessmentDetailEntity> assessmentDetailsEntities;
    private List<ChildWeightingsEntity> childWeightingsEntities;

    private FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO;
    private FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO;
    private ChildWeightHistoryDTO childWeightHistoryDTO;
    private FinancialAssessmentDTO financialAssessmentDTO;

    private static final int MOCK_FINANCIAL_ASSESSMENT_ID = 1000;

    @Before
    public void setup() {
        assessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity = TestEntityDataBuilder.getFinancialAssessmentsHistoryEntity();
        assessmentDetailsEntities = List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity());
        childWeightingsEntities = List.of(TestEntityDataBuilder.getChildWeightingsEntity());

        financialAssessmentsHistoryDTO = TestModelDataBuilder.getFinancialAssessmentsHistoryDTO();
        financialAssessmentDetailsHistoryDTO = TestModelDataBuilder.getFinancialAssessmentDetailsHistoryDTO();
        childWeightHistoryDTO = TestModelDataBuilder.getChildWeightHistoryDTO();
        financialAssessmentDTO = TestModelDataBuilder.getFinancialAssessmentDTO();

        when(financialAssessmentHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(financialAssessmentsHistoryEntity);
        when(financialAssessmentImpl.find(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(assessmentEntity);
        when(assessmentHistoryMapper.FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity)).thenReturn(financialAssessmentsHistoryDTO);
        when(repOrderImpl.findRepOrder(assessmentEntity.getRepId())).thenReturn(TestEntityDataBuilder.getRepOrder());
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryIsCreated() {
        when(financialAssessmentDetailsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(assessmentDetailsEntities);
        when(assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class)))
                .thenReturn(financialAssessmentDetailsHistoryDTO);
        when(childWeightingsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(childWeightingsEntities);
        when(assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class))).thenReturn(childWeightHistoryDTO);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(financialAssessmentDetailsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class));
        verify(childWeightingsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class));
        verify(financialAssessmentHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryWithoutFinAssessmentDetailsIsCreated() {
        when(financialAssessmentDetailsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(null);
        when(childWeightingsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(childWeightingsEntities);
        when(assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class))).thenReturn(childWeightHistoryDTO);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(financialAssessmentDetailsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper, times(0)).FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class));
        verify(childWeightingsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class));

        financialAssessmentsHistoryDTO.setAssessmentDetailsList(new ArrayList<>());
        verify(financialAssessmentHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryWithoutChildWeightingsIsCreated() {
        when(financialAssessmentDetailsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(assessmentDetailsEntities);
        when(assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class)))
                .thenReturn(financialAssessmentDetailsHistoryDTO);
        when(assessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(any(FinancialAssessmentEntity.class)))
                .thenReturn(financialAssessmentDTO);
        when(childWeightingsImpl.findAll(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(null);

        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(assessmentMapper).FinancialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
        verify(financialAssessmentDetailsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(any(FinancialAssessmentDetailEntity.class));
        verify(childWeightingsImpl).findAll(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper, times(0)).ChildWeightingsEntityToChildWeightHistoryDTO(any(ChildWeightingsEntity.class));

        financialAssessmentsHistoryDTO.setChildWeightingsList(new ArrayList<>());
        verify(financialAssessmentHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
    }
}
