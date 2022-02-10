package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND;
import static gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentImplTest {

    @Spy
    @InjectMocks
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Spy
    private FinancialAssessmentRepository financialAssessmentRepository;

    @Spy
    private PassportAssessmentRepository passportAssessmentRepository;

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Captor
    private ArgumentCaptor<FinancialAssessmentEntity> financialAssessmentEntityArgumentCaptor;


    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(financialAssessmentRepository.getById(any())).thenReturn(FinancialAssessmentEntity.builder().id(1000).build());
        FinancialAssessmentEntity returned = financialAssessmentImpl.find(1000);
        assertThat(returned.getId()).isEqualTo(1000);
    }

    @Test
    public void whenCreateIsInvoked_thenAssessmentIsSaved() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();

        when(financialAssessmentMapper.FinancialAssessmentDtoToFinancialAssessmentEntity(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.create(financialAssessment);

        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getRepId()).isEqualTo(financialAssessment.getRepId());
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("INIT");
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        financialAssessment.setFullAssessmentDate(LocalDateTime.now());

        when(financialAssessmentRepository.getById(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());

        assertThat(financialAssessment.getAssessmentType()).isEqualTo("INIT");
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(1000);
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("FULL");

    }

    @Test
    public void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentImpl.delete(1000);
        verify(financialAssessmentRepository).deleteById(any());
    }

    @Test
    public void givenOutstandingFinancialAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingAssessmentFoundResultIsRetrieved() {
        when(financialAssessmentRepository.findOutstandingFinancialAssessments(any())).thenReturn(1l);
        OutstandingAssessmentResultDTO result = financialAssessmentImpl.checkForOutstandingAssessments(1000);
        assertThat(result.isOutstandingAssessments()).isEqualTo(true);
        assertThat(result.getMessage()).isEqualTo(MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
    }

    @Test
    public void givenOutstandingPassportAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingAssessmentFoundResultIsRetrieved() {
        when(financialAssessmentRepository.findOutstandingFinancialAssessments(any())).thenReturn(0l);
        when(passportAssessmentRepository.findOutstandingPassportAssessments(any())).thenReturn(1l);
        OutstandingAssessmentResultDTO result = financialAssessmentImpl.checkForOutstandingAssessments(1000);
        assertThat(result.isOutstandingAssessments()).isEqualTo(true);
        assertThat(result.getMessage()).isEqualTo(MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND);
    }

    @Test
    public void givenNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingAssessmentNotFoundResultIsRetrieved() {
        when(financialAssessmentRepository.findOutstandingFinancialAssessments(any())).thenReturn(0l);
        when(passportAssessmentRepository.findOutstandingPassportAssessments(any())).thenReturn(0l);
        OutstandingAssessmentResultDTO result = financialAssessmentImpl.checkForOutstandingAssessments(1000);
        assertThat(result.isOutstandingAssessments()).isEqualTo(false);
    }
}
