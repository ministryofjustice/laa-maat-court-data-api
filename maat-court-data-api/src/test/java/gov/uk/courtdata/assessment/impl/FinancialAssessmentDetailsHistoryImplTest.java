package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDetailsHistoryDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailsHistoryEntity;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsHistoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentDetailsHistoryImplTest {

    @InjectMocks
    private FinancialAssessmentDetailsHistoryImpl financialAssessmentDetailsHistoryImpl;

    @Mock
    private FinancialAssessmentHistoryMapper assessmentHistoryMapper;

    @Mock
    private FinancialAssessmentDetailsHistoryRepository financialAssessmentDetailsHistoryRepository;

    private FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO;
    private FinancialAssessmentDetailsHistoryEntity financialAssessmentDetailsHistoryEntity;

    @Before
    public void setup() {
        financialAssessmentDetailsHistoryEntity = TestEntityDataBuilder.getFinancialAssessmentDetailsHistoryEntity();
        financialAssessmentDetailsHistoryDTO = TestModelDataBuilder.getFinancialAssessmentDetailsHistoryDTO();
    }

    @Test
    public void givenFinancialAssessmentsHistoryDTOAndFinancialAssessmentId_whenBuildAndSaveIsInvoked_thenFinancialAssessmentsHistoryEntityIsPersisted() {
        when(assessmentHistoryMapper.FinancialAssessmentDetailsHistoryDTOToFinancialAssessmentDetailsHistoryEntity(financialAssessmentDetailsHistoryDTO))
                .thenReturn(financialAssessmentDetailsHistoryEntity);

        financialAssessmentDetailsHistoryImpl.buildAndSave(Arrays.asList(financialAssessmentDetailsHistoryDTO),1000);

        verify(assessmentHistoryMapper).FinancialAssessmentDetailsHistoryDTOToFinancialAssessmentDetailsHistoryEntity(financialAssessmentDetailsHistoryDTO);
        verify(financialAssessmentDetailsHistoryRepository).saveAll(Arrays.asList(financialAssessmentDetailsHistoryEntity));
    }
}
