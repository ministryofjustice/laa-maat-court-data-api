package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentServiceTest {

    @InjectMocks
    private FinancialAssessmentService financialAssessmentService;

    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Test
    public void testFinancialAssessmentService_whenGetAssessmentIsInvoked_thenAssessmentIsRetrieved() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(1000);
        when(financialAssessmentImpl.getAssessment(any())).thenReturn(financialAssessment);
        when(financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(any())).thenReturn(FinancialAssessmentDTO.builder().id(1000).build());

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.getAssessment(1000);

        verify(financialAssessmentImpl).getAssessment(any());

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void testFinancialAssessmentService_whenDeleteAssessmentIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentService.deleteAssessment(1000);
        verify(financialAssessmentImpl).deleteAssessment(any());
    }

    @Test
    public void testFinancialAssessmentService_whenCreateAssessmentIsInvoked_thenAssessmentIsCreated() {
        FinancialAssessmentDTO financialAssessmentDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        CreateFinancialAssessment financialAssessment = TestModelDataBuilder.getCreateFinancialAssessment();
        when(financialAssessmentMapper.CreateFinancialAssessmentToFinancialAssessmentDTO(any())).thenReturn(financialAssessmentDTO);

        financialAssessmentDTO.setId(1000);
        when(financialAssessmentImpl.createAssessment(any())).thenReturn(financialAssessmentDTO);

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.createAssessment(financialAssessment);

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void testFinancialAssessmentService_whenUpdateAssessmentIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessmentDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        UpdateFinancialAssessment financialAssessment = TestModelDataBuilder.getUpdateFinancialAssessment();

        when(financialAssessmentMapper.UpdateFinancialAssessmentToFinancialAssessmentDTO(any(UpdateFinancialAssessment.class))).thenReturn(financialAssessmentDTO);

        financialAssessmentDTO.setFullAssessmentDate(LocalDateTime.now());
        financialAssessmentDTO.setAssessmentType("FULL");

        when(financialAssessmentImpl.updateAssessment(any())).thenReturn(financialAssessmentDTO);

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.updateAssessment(financialAssessment);

        assertThat(returnedAssessment.getAssessmentType()).isEqualTo("FULL");
    }
}
