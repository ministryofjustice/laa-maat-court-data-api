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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND;
import static gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialAssessmentImplTest {

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

    private static final Integer MOCK_FINANCIAL_ASSESSMENT_ID = 1000;


    @Test
    void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        FinancialAssessmentEntity financialAssessment = FinancialAssessmentEntity.builder().id(MOCK_FINANCIAL_ASSESSMENT_ID).build();
        when(financialAssessmentRepository.findById(any())).thenReturn(Optional.of(financialAssessment));

        Optional<FinancialAssessmentEntity> returned = financialAssessmentImpl.find(MOCK_FINANCIAL_ASSESSMENT_ID);

        assertTrue(returned.isPresent());
        assertThat(returned.get().getId()).isEqualTo(MOCK_FINANCIAL_ASSESSMENT_ID);
    }

    @Test
    void whenCreateIsInvoked_thenAssessmentIsSaved() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();

        when(financialAssessmentMapper.financialAssessmentDtoToFinancialAssessmentEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.create(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(financialAssessmentEntityArgumentCaptor.capture());
    }

    @Test
    void givenInitAssessmentWithNoRelationships_whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();

        when(financialAssessmentRepository.getReferenceById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(financialAssessmentEntityArgumentCaptor.capture());
        verify(financialAssessmentImpl, never()).updateAssessmentDetails(any(), any());
        verify(financialAssessmentImpl, never()).updateChildWeightings(any(), any());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("INIT");
    }

    @Test
    void givenFullAssessmentWithNoRelationships_whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        financialAssessment.setFullAssessmentDate(LocalDateTime.now());

        when(financialAssessmentRepository.getReferenceById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(financialAssessmentEntityArgumentCaptor.capture());
        verify(financialAssessmentImpl, never()).updateAssessmentDetails(any(), any());
        verify(financialAssessmentImpl, never()).updateChildWeightings(any(), any());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("FULL");
    }

    @Test
    void givenInitAssessmentWithDetails_whenUpdateIsInvoked_thenAssessmentDetailsAreUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTOWithDetails();
        when(financialAssessmentRepository.getReferenceById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntityWithDetails());
        when(financialAssessmentMapper.financialAssessmentDetailsToFinancialAssessmentDetailsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(any());
        verify(financialAssessmentImpl).updateAssessmentDetails(any(), any());
    }

    @Test
    void givenInitAssessmentWithChildWeightings_whenUpdateIsInvoked_thenChildWeightingsAreUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentWithChildWeightings();
        when(financialAssessmentRepository.getReferenceById(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntityWithChildWeightings());
        when(financialAssessmentMapper.childWeightingsToChildWeightingsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getChildWeightingsEntity());

        financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(any());
        verify(financialAssessmentImpl).updateChildWeightings(any(), any());
    }

    @Test
    void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentImpl.delete(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(financialAssessmentRepository).deleteById(any());
    }

    @Test
    void givenOutstandingFinancialAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingAssessmentFoundResultIsRetrieved() {
        when(financialAssessmentRepository.findOutstandingFinancialAssessments(any())).thenReturn(1L);
        OutstandingAssessmentResultDTO result = financialAssessmentImpl.checkForOutstandingAssessments(MOCK_FINANCIAL_ASSESSMENT_ID);
        assertThat(result.isOutstandingAssessments()).isTrue();
        assertThat(result.getMessage()).isEqualTo(MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
    }

    @Test
    void givenOutstandingPassportAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingAssessmentFoundResultIsRetrieved() {
        when(financialAssessmentRepository.findOutstandingFinancialAssessments(any())).thenReturn(0L);
        when(passportAssessmentRepository.findOutstandingPassportAssessments(any())).thenReturn(1L);
        OutstandingAssessmentResultDTO result = financialAssessmentImpl.checkForOutstandingAssessments(MOCK_FINANCIAL_ASSESSMENT_ID);
        assertThat(result.isOutstandingAssessments()).isTrue();
        assertThat(result.getMessage()).isEqualTo(MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND);
    }

    @Test
    void givenFinancialAssessment_whenSetOldAssessmentReplacedIsInvoked_thenOldAssessmentsAreReplaced() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        financialAssessmentImpl.setOldAssessmentReplaced(financialAssessment);

        Integer repId = financialAssessment.getRepOrder().getId();

        verify(hardshipReviewRepository).replaceOldHardshipReviews(repId, MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(passportAssessmentRepository).updateAllPreviousPassportAssessmentsAsReplaced(repId);
        verify(financialAssessmentRepository).updatePreviousFinancialAssessmentsAsReplaced(repId, MOCK_FINANCIAL_ASSESSMENT_ID);
    }

    @Test
    void givenNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenOutstandingAssessmentNotFoundResultIsRetrieved() {
        when(financialAssessmentRepository.findOutstandingFinancialAssessments(any())).thenReturn(0L);
        when(passportAssessmentRepository.findOutstandingPassportAssessments(any())).thenReturn(0L);
        OutstandingAssessmentResultDTO result = financialAssessmentImpl.checkForOutstandingAssessments(MOCK_FINANCIAL_ASSESSMENT_ID);
        assertThat(result.isOutstandingAssessments()).isFalse();
    }

    @Test
    void givenExistingDetailsToBeRemoved_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addAssessmentDetail(
                FinancialAssessmentDetailEntity.builder().criteriaDetailId(20).build()
        );

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);
        assertThat(existingAssessment.getAssessmentDetails()).asList().isEmpty();
    }

    @Test
    void givenExistingDetailsToBeUpdated_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreUpdated() {
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
    void givenNewDetails_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreAdded() {
        FinancialAssessmentDTO financialAssessment =
                TestModelDataBuilder.getFinancialAssessmentDTOWithDetails();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        when(financialAssessmentMapper.financialAssessmentDetailsToFinancialAssessmentDetailsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity());

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);

        FinancialAssessmentDetails passed = financialAssessment.getAssessmentDetails().get(0);
        FinancialAssessmentDetailEntity updated = existingAssessment.getAssessmentDetails().get(0);

        assertThat(updated.getCriteriaDetailId()).isEqualTo(passed.getCriteriaDetailId());
    }

    @Test
    void givenExistingChildWeightingsToBeRemoved_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addChildWeighting(
                ChildWeightingsEntity.builder().childWeightingId(20).build()
        );

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);
        assertThat(existingAssessment.getAssessmentDetails()).asList().isEmpty();
    }

    @Test
    void givenExistingChildWeightingsToBeUpdated_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreUpdated() {
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
    void givenNewChildWeightings_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreAdded() {
        FinancialAssessmentDTO financialAssessment =
                TestModelDataBuilder.getFinancialAssessmentWithChildWeightings();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        when(financialAssessmentMapper.childWeightingsToChildWeightingsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getChildWeightingsEntity());

        financialAssessmentImpl.updateChildWeightings(financialAssessment, existingAssessment);

        assertThat(existingAssessment.getChildWeightings().get(0).getChildWeightingId())
                .isEqualTo(financialAssessment.getChildWeightings().get(0).getChildWeightingId());
    }
}
