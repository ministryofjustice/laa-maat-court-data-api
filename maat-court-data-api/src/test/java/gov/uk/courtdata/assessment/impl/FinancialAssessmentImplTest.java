package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentImplTest {

    @InjectMocks
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Spy
    private FinancialAssessmentRepository financialAssessmentRepository;

    @Captor
    private ArgumentCaptor<FinancialAssessmentEntity> financialAssessmentEntityArgumentCaptor;

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;

    @Mock
    private PassportAssessmentRepository passportAssessmentRepository;

    @Test
    public void testFinancialAssessmentImpl_whenGetAssessmentIsInvoked_thenAssessmentIsRetrieved() {
        financialAssessmentImpl.getAssessment(1000);
        verify(financialAssessmentRepository).getById(any());
    }

    @Test
    public void testFinancialAssessmentImpl_whenCreateAssessmentIsInvoked_thenAssessmentIsSaved() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessmentImpl.createAssessment(financialAssessment);

        verify(financialAssessmentRepository).updateOldAssessments(any());
        verify(passportAssessmentRepository).updateOldPassportAssessments(any());
        verify(hardshipReviewRepository).updateOldHardshipReviews(any(), any());
        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getRepId()).isEqualTo(financialAssessment.getRepId());
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("INIT");
    }

    @Test
    public void testFinancialAssessmentImpl_whenUpdateAssessmentIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(1000);
        financialAssessmentImpl.updateAssessment(financialAssessment);
        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(1000);
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("INIT");
    }

    @Test
    public void testFinancialAssessmentImpl_whenUpdateAssessmentIsInvokedWithFullAssessmentDate_thenAssessmentTypeIsSet() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(1000);
        financialAssessment.setFullAssessmentDate(LocalDateTime.now());
        financialAssessmentImpl.updateAssessment(financialAssessment);
        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(1000);
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("FULL");
    }

    @Test
    public void testFinancialAssessmentImpl_whenDeleteAssessmentIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentImpl.deleteAssessment(1000);
        verify(financialAssessmentRepository).deleteById(any());
    }
}
