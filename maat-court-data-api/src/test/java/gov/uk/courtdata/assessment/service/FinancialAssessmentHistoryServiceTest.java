package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentHistoryImpl;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.repOrder.impl.RepOrderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinancialAssessmentHistoryServiceTest {

    @InjectMocks
    private FinancialAssessmentHistoryService financialAssessmentHistoryService;

    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Mock
    private RepOrderImpl repOrderImpl;

    @Mock
    private FinancialAssessmentHistoryImpl financialAssessmentHistoryImpl;

    @Mock
    private FinancialAssessmentHistoryMapper assessmentHistoryMapper;

    @Mock
    private FinancialAssessmentMapper assessmentMapper;

    private FinancialAssessmentEntity assessmentEntity;

    private FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO;

    private static final int MOCK_FINANCIAL_ASSESSMENT_ID = 1000;

    @BeforeEach
    public void setup() {
        assessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessmentsHistoryDTO = TestModelDataBuilder.getFinancialAssessmentsHistoryDTO();
        when(financialAssessmentImpl.find(MOCK_FINANCIAL_ASSESSMENT_ID)).thenReturn(assessmentEntity);
        when(assessmentHistoryMapper.FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity))
                .thenReturn(financialAssessmentsHistoryDTO);
        when(financialAssessmentHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentsHistoryEntity());
        when(repOrderImpl.find(assessmentEntity.getRepOrder().getId())).thenReturn(TestEntityDataBuilder.getRepOrder());
    }

    @Test
    public void givenCorrectFinancialAssessmentId_whenCreateAssessmentHistoryIsInvoked_thenAssessmentsHistoryIsCreated() {
        financialAssessmentHistoryService.createAssessmentHistory(MOCK_FINANCIAL_ASSESSMENT_ID, true);

        verify(financialAssessmentImpl).find(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(assessmentHistoryMapper).FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);
        verify(assessmentMapper).FinancialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
        verify(financialAssessmentHistoryImpl).buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);
    }
}
