package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentServiceTest {

    private static final Integer TEST_REP_ID = 1000;

    @InjectMocks
    private FinancialAssessmentService financialAssessmentService;

    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(any()))
                .thenReturn(FinancialAssessmentDTO.builder().id(1000).build());
        when(financialAssessmentImpl.find(any())).thenReturn(FinancialAssessmentEntity.builder().id(1000).build());
        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.find(1000);

        verify(financialAssessmentImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentService.delete(1000);
        verify(financialAssessmentImpl).delete(any(Integer.class));
    }

    @Test
    public void whenCreateIsInvoked_thenAssessmentIsCreated() {
        FinancialAssessmentDTO financialAssessmentDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        CreateFinancialAssessment financialAssessment = TestModelDataBuilder.getCreateFinancialAssessment();

        when(financialAssessmentMapper.CreateFinancialAssessmentToFinancialAssessmentDTO(any()))
                .thenReturn(financialAssessmentDTO);
        when(financialAssessmentImpl.create(any())).thenReturn(FinancialAssessmentEntity.builder().id(1000).build());
        when(financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(any()))
                .thenReturn(FinancialAssessmentDTO.builder().id(1000).build());

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.create(financialAssessment);
        verify(financialAssessmentImpl).setOldAssessmentReplaced(any(FinancialAssessmentEntity.class));

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessmentDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        UpdateFinancialAssessment financialAssessment = TestModelDataBuilder.getUpdateFinancialAssessment();

        when(financialAssessmentMapper.UpdateFinancialAssessmentToFinancialAssessmentDTO(any(UpdateFinancialAssessment.class)))
                .thenReturn(financialAssessmentDTO);
        when(financialAssessmentImpl.update(any())).thenReturn(FinancialAssessmentEntity.builder().id(1000).build());
        when(financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(any()))
                .thenReturn(FinancialAssessmentDTO.builder().id(1000).build());

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.update(financialAssessment);

        verify(financialAssessmentImpl).update(any(FinancialAssessmentDTO.class));
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void givenOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenResultIsReturned() {
        when(financialAssessmentImpl.checkForOutstandingAssessments(any(Integer.class))).thenReturn(
                OutstandingAssessmentResultDTO.builder().outstandingAssessments(true).message(MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND).build()
        );
        OutstandingAssessmentResultDTO result = financialAssessmentService.checkForOutstandingAssessments(TEST_REP_ID);
        verify(financialAssessmentImpl).checkForOutstandingAssessments(any());
        assertThat(result.isOutstandingAssessments()).isEqualTo(true);
        assertThat(result.getMessage()).isEqualTo(MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
    }

    @Test
    public void givenNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenNotFoundResultIsReturned() {
        when(financialAssessmentImpl.checkForOutstandingAssessments(any(Integer.class))).thenReturn(
                OutstandingAssessmentResultDTO.builder().build()
        );
        OutstandingAssessmentResultDTO result = financialAssessmentService.checkForOutstandingAssessments(TEST_REP_ID);
        verify(financialAssessmentImpl).checkForOutstandingAssessments(any());
        assertThat(result.isOutstandingAssessments()).isEqualTo(false);
    }
}
