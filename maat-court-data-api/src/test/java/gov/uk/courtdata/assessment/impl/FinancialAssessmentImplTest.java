package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
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
    private HardshipReviewRepository hardshipReviewRepository;

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

        when(financialAssessmentMapper.FinancialAssessmentDtoToFinancialAssessmentEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.create(financialAssessment);

        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());
    }

    @Test
    public void givenInitAssessmentWithNoRelationships_whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();

        when(financialAssessmentRepository.getById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(financialAssessmentEntityArgumentCaptor.capture());
        verify(financialAssessmentImpl, never()).updateAssessmentDetails(any(), any());
        verify(financialAssessmentImpl, never()).updateChildWeightings(any(), any());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("INIT");
    }

    @Test
    public void givenFullAssessmentWithNoRelationships_whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        financialAssessment.setFullAssessmentDate(LocalDateTime.now());

        when(financialAssessmentRepository.getById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(financialAssessmentEntityArgumentCaptor.capture());
        verify(financialAssessmentImpl, never()).updateAssessmentDetails(any(), any());
        verify(financialAssessmentImpl, never()).updateChildWeightings(any(), any());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("FULL");
    }

    @Test
    public void givenInitAssessmentWithDetails_whenUpdateIsInvoked_thenAssessmentDetailsAreUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTOWithDetails();
        when(financialAssessmentRepository.getById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntityWithDetails());
        when(financialAssessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(any());
        verify(financialAssessmentImpl).updateAssessmentDetails(any(), any());
    }

    @Test
    public void givenInitAssessmentWithChildWeightings_whenUpdateIsInvoked_thenChildWeightingsAreUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentWithChildWeightings();
        when(financialAssessmentRepository.getById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntityWithChildWeightings());
        when(financialAssessmentMapper.ChildWeightingsToChildWeightingsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getChildWeightingsEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(any());
        verify(financialAssessmentImpl).updateChildWeightings(any(), any());
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
    public void givenFinancialAssessment_whenSetOldAssessmentReplacedIsInvoked_thenOldAssessmentsAreReplaced() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();

        financialAssessment.setId(1000);
        financialAssessmentImpl.setOldAssessmentReplaced(financialAssessment);

        verify(hardshipReviewRepository).updateOldHardshipReviews(anyInt(), anyInt());
        verify(passportAssessmentRepository).updateAllPreviousPassportAssessmentsAsReplaced(anyInt());
        verify(financialAssessmentRepository).updatePreviousFinancialAssessmentsAsReplaced(anyInt(), anyInt());
    }

    @Test
    public void givenNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingAssessmentNotFoundResultIsRetrieved() {
        when(financialAssessmentRepository.findOutstandingFinancialAssessments(any())).thenReturn(0l);
        when(passportAssessmentRepository.findOutstandingPassportAssessments(any())).thenReturn(0l);
        OutstandingAssessmentResultDTO result = financialAssessmentImpl.checkForOutstandingAssessments(1000);
        assertThat(result.isOutstandingAssessments()).isEqualTo(false);
    }

    @Test
    public void givenExistingDetailsToBeRemoved_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addAssessmentDetail(
                FinancialAssessmentDetailEntity.builder().criteriaDetailId(20).build()
        );

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);
        assertThat(existingAssessment.getAssessmentDetails().size()).isEqualTo(0);
    }

    @Test
    public void givenExistingDetailsToBeUpdated_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreUpdated() {
        FinancialAssessmentDTO financialAssessment =
                TestModelDataBuilder.getFinancialAssessmentDTOWithDetails();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addAssessmentDetail(
                FinancialAssessmentDetailEntity.builder().criteriaDetailId(40).build()
        );
        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);

        FinancialAssessmentDetails passed = financialAssessment.getAssessmentDetails().get(0);
        FinancialAssessmentDetailEntity updated = existingAssessment.getAssessmentDetails().get(0);

        assertThat(updated.getApplicantAmount()).isEqualTo(passed.getApplicantAmount());
        assertThat(updated.getApplicantFrequency()).isEqualTo(passed.getApplicantFrequency());
        assertThat(updated.getPartnerAmount()).isEqualTo(passed.getPartnerAmount());
        assertThat(updated.getPartnerFrequency()).isEqualTo(passed.getPartnerFrequency());
    }

    @Test
    public void givenNewDetails_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreAdded() {
        FinancialAssessmentDTO financialAssessment =
                TestModelDataBuilder.getFinancialAssessmentDTOWithDetails();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        when(financialAssessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity());

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);

        FinancialAssessmentDetails passed = financialAssessment.getAssessmentDetails().get(0);
        FinancialAssessmentDetailEntity updated = existingAssessment.getAssessmentDetails().get(0);

        assertThat(updated.getCriteriaDetailId()).isEqualTo(passed.getCriteriaDetailId());
    }

    @Test
    public void givenExistingChildWeightingsToBeRemoved_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addChildWeighting(
                ChildWeightingsEntity.builder().childWeightingId(20).build()
        );

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);
        assertThat(existingAssessment.getAssessmentDetails().size()).isEqualTo(0);
    }

    @Test
    public void givenExistingChildWeightingsToBeUpdated_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreUpdated() {
        FinancialAssessmentDTO financialAssessment =
                TestModelDataBuilder.getFinancialAssessmentWithChildWeightings();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addChildWeighting(
                ChildWeightingsEntity.builder().childWeightingId(12).noOfChildren(3).build()
        );
        financialAssessmentImpl.updateChildWeightings(financialAssessment, existingAssessment);

        assertThat(existingAssessment.getChildWeightings().get(0).getNoOfChildren())
                .isEqualTo(financialAssessment.getChildWeightings().get(0).getNoOfChildren());
    }

    @Test
    public void givenNewChildWeightings_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreAdded() {
        FinancialAssessmentDTO financialAssessment =
                TestModelDataBuilder.getFinancialAssessmentWithChildWeightings();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        when(financialAssessmentMapper.ChildWeightingsToChildWeightingsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getChildWeightingsEntity());

        financialAssessmentImpl.updateChildWeightings(financialAssessment, existingAssessment);

        assertThat(existingAssessment.getChildWeightings().get(0).getChildWeightingId())
                .isEqualTo(financialAssessment.getChildWeightings().get(0).getChildWeightingId());
    }
}
