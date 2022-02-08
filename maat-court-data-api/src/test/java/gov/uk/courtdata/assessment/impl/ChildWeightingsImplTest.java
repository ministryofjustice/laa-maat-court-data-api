package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.repository.ChildWeightingsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChildWeightingsImplTest {

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Mock
    private ChildWeightingsRepository childWeightingsRepository;

    @InjectMocks
    private ChildWeightingsImpl childWeightingsImpl;

    @Captor
    private ArgumentCaptor<List<ChildWeightingsEntity>> childWeightingsEntityArgumentCaptor;

    @Test
    public void shouldCreateChildWeightingsOnSave() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        List<ChildWeightings> childWeightingsList = List.of(TestModelDataBuilder.getChildWeightings());

        when(financialAssessmentMapper.ChildWeightingsToChildWeightingsEntity(any())).thenReturn(
                ChildWeightingsEntity.builder()
                        .childWeightingId(2)
                        .noOfChildren(1)
                        .build()
        );

        childWeightingsImpl.save(financialAssessment, childWeightingsList);

        verify(childWeightingsRepository).saveAll(childWeightingsEntityArgumentCaptor.capture());
        assertThat(childWeightingsEntityArgumentCaptor.getValue().get(0).getId()).isNull();
        assertThat(childWeightingsEntityArgumentCaptor.getValue().get(0).getChildWeightingId()).isEqualTo(2);
        assertThat(childWeightingsEntityArgumentCaptor.getValue().get(0).getNoOfChildren()).isEqualTo(1);
        assertThat(childWeightingsEntityArgumentCaptor.getValue().get(0).getFinancialAssessmentId()).isEqualTo(1000);
        assertThat(childWeightingsEntityArgumentCaptor.getValue().get(0).getUserCreated()).isEqualTo("test-f");
    }

}
