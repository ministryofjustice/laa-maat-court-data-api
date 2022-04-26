package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.FinancialAssessmentsHistoryEntity;
import gov.uk.courtdata.repository.FinancialAssessmentsHistoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentHistoryImplTest {

    private static final int MOCK_FINANCIAL_ASSESSMENT_ID = 1000;

    @InjectMocks
    private FinancialAssessmentHistoryImpl financialAssessmentHistoryImpl;

    @Mock
    private FinancialAssessmentHistoryMapper assessmentHistoryMapper;

    @Mock
    private FinancialAssessmentsHistoryRepository financialAssessmentsHistoryRepository;

    private FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity;
    private FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO;

    @Before
    public void setup() {
        financialAssessmentsHistoryEntity = TestEntityDataBuilder.getFinancialAssessmentsHistoryEntity();
        financialAssessmentsHistoryDTO = TestModelDataBuilder.getFinancialAssessmentsHistoryDTO();
    }

    @Test
    public void givenFinancialAssessmentsHistoryDTOAndFinancialAssessmentId_whenBuildAndSaveIsInvoked_thenFinancialAssessmentsHistoryEntityIsPersisted() {
        when(assessmentHistoryMapper.FinancialAssessmentsHistoryDTOToFinancialAssessmentsHistoryEntity(financialAssessmentsHistoryDTO))
                .thenReturn(financialAssessmentsHistoryEntity);

        financialAssessmentHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, MOCK_FINANCIAL_ASSESSMENT_ID);

        verify(assessmentHistoryMapper).FinancialAssessmentsHistoryDTOToFinancialAssessmentsHistoryEntity(financialAssessmentsHistoryDTO);
        verify(financialAssessmentsHistoryRepository).save(financialAssessmentsHistoryEntity);
    }

}
