package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.ChildWeightHistoryDTO;
import gov.uk.courtdata.entity.ChildWeightHistoryEntity;
import gov.uk.courtdata.repository.ChildWeightHistoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChildWeightHistoryImplTest {

    @InjectMocks
    private ChildWeightHistoryImpl childWeightHistoryImpl;

    @Mock
    private FinancialAssessmentHistoryMapper assessmentHistoryMapper;

    @Mock
    private ChildWeightHistoryRepository childWeightHistoryRepository;

    private ChildWeightHistoryEntity childWeightHistoryEntity;
    private ChildWeightHistoryDTO childWeightHistoryDTO;

    @Before
    public void setup() {
        childWeightHistoryEntity = TestEntityDataBuilder.getChildWeightHistoryEntity();
        childWeightHistoryDTO = TestModelDataBuilder.getChildWeightHistoryDTO();
    }

    @Test
    public void givenFinancialAssessmentsHistoryDTOAndFinancialAssessmentId_whenBuildAndSaveIsInvoked_thenFinancialAssessmentsHistoryEntityIsPersisted() {
        when(assessmentHistoryMapper.ChildWeightHistoryDTOToChildWeightHistoryEntity(childWeightHistoryDTO)).thenReturn(childWeightHistoryEntity);

        childWeightHistoryImpl.buildAndSave(List.of(childWeightHistoryDTO));

        verify(assessmentHistoryMapper).ChildWeightHistoryDTOToChildWeightHistoryEntity(childWeightHistoryDTO);
        verify(childWeightHistoryRepository).saveAll(List.of(childWeightHistoryEntity));
    }

}
