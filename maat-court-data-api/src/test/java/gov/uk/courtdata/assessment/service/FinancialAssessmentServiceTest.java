package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentDTOMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentServiceTest {

    @InjectMocks
    private FinancialAssessmentService financialAssessmentService;

    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Mock
    private FinancialAssessmentDTOMapper financialAssessmentDTOMapper;

    @Test
    public void testFinancialAssessmentService_whenGetAssessmentIsInvoked_thenAssessmentIsRetrieved() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(1000);
        when(financialAssessmentImpl.getAssessment(any())).thenReturn(financialAssessment);
        when(financialAssessmentDTOMapper.toFinancialAssessmentDTO(any())).thenReturn(FinancialAssessmentDTO.builder().id(1000).build());

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.getAssessment(1000);

        verify(financialAssessmentDTOMapper).toFinancialAssessmentDTO(any());

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void testFinancialAssessmentService_whenDeleteAssessmentIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentService.deleteAssessment(1000);
        verify(financialAssessmentImpl).deleteAssessment(any());
    }

    @Test
    public void testFinancialAssessmentService_whenCreateAssessmentIsInvoked_thenAssessmentIsCreated() {
        CreateFinancialAssessment financialAssessment = TestModelDataBuilder.getCreateFinancialAssessment();
        FinancialAssessmentEntity financialAssessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        when(financialAssessmentDTOMapper.toFinancialAssessmentEntity(any(CreateFinancialAssessment.class))).thenReturn(financialAssessmentEntity);
        when(financialAssessmentDTOMapper.toFinancialAssessmentDTO(any())).thenReturn(FinancialAssessmentDTO.builder().id(1000).build());
        when(financialAssessmentImpl.createAssessment(any())).thenReturn(financialAssessmentEntity);

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.createAssessment(financialAssessment);

        verify(financialAssessmentDTOMapper).toFinancialAssessmentDTO(any());

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void testFinancialAssessmentService_whenUpdateAssessmentIsInvoked_thenAssessmentIsUpdated() {
        UpdateFinancialAssessment financialAssessment = TestModelDataBuilder.getUpdateFinancialAssessment();
        FinancialAssessmentEntity financialAssessmentEntity = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessmentEntity.setId(1000);
        when(financialAssessmentDTOMapper.toFinancialAssessmentEntity(any(UpdateFinancialAssessment.class))).thenReturn(financialAssessmentEntity);
        when(financialAssessmentDTOMapper.toFinancialAssessmentDTO(any())).thenReturn(FinancialAssessmentDTO.builder().id(1000).build());
        when(financialAssessmentImpl.updateAssessment(any())).thenReturn(financialAssessmentEntity);

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.updateAssessment(financialAssessment);

        verify(financialAssessmentDTOMapper).toFinancialAssessmentDTO(any());

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }
}
