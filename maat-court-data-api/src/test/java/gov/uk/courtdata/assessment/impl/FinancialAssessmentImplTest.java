package gov.uk.courtdata.assessment.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinAssIncomeEvidenceDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FinancialAssessmentImplTest {

    private static final Integer MOCK_FINANCIAL_ASSESSMENT_ID = 1000;

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
    void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        FinancialAssessmentEntity financialAssessment = FinancialAssessmentEntity.builder()
                .id(MOCK_FINANCIAL_ASSESSMENT_ID)
                .build();
        when(financialAssessmentRepository.findById(any())).thenReturn(Optional.of(financialAssessment));

        Optional<FinancialAssessmentEntity> returned = financialAssessmentImpl.find(MOCK_FINANCIAL_ASSESSMENT_ID);

        assertThat(returned)
                .isPresent()
                .map(FinancialAssessmentEntity::getId)
                .get()
                .isEqualTo(MOCK_FINANCIAL_ASSESSMENT_ID);
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

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType())
                .isEqualTo("INIT");
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

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType())
                .isEqualTo("FULL");
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
    void givenInitAssessmentWithIncomeEvidence_whenUpdateIsInvoked_thenIncomeEvidenceAreUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentWithIncomeEvidence();
        FinancialAssessmentEntity financialAssessmentEntity =
                TestEntityDataBuilder.getFinancialAssessmentEntityWithIncomeEvidence();
        financialAssessmentEntity.getFinAssIncomeEvidences().getFirst().setId(123);
        when(financialAssessmentRepository.getReferenceById(any())).thenReturn(financialAssessmentEntity);
        when(financialAssessmentMapper.finAssIncomeEvidenceDTOToFinAssIncomeEvidenceEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinAssIncomeEvidenceEntity());
        when(financialAssessmentRepository.saveAndFlush(any())).thenReturn(financialAssessmentEntity);

        FinancialAssessmentEntity response = financialAssessmentImpl.update(financialAssessment);

        verify(financialAssessmentRepository).saveAndFlush(any());

        assertThat(response.getFinAssIncomeEvidences())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .hasSize(1);
    }

    @Test
    void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentImpl.delete(MOCK_FINANCIAL_ASSESSMENT_ID);
        verify(financialAssessmentRepository).deleteById(any());
    }

    @Test
    void givenExistingDetailsToBeRemoved_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addAssessmentDetail(
                FinancialAssessmentDetailEntity.builder().criteriaDetailId(20).build());

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);
        assertThat(existingAssessment.getAssessmentDetails())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .isEmpty();
    }

    @Test
    void givenExistingDetailsToBeUpdated_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTOWithDetails();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addAssessmentDetail(
                FinancialAssessmentDetailEntity.builder().criteriaDetailId(40).build());
        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);

        FinancialAssessmentDetails passed =
                financialAssessment.getAssessmentDetails().getFirst();
        FinancialAssessmentDetailEntity updated =
                existingAssessment.getAssessmentDetails().getFirst();

        assertThat(updated.getApplicantAmount()).isEqualTo(passed.getApplicantAmount());
        assertThat(updated.getApplicantFrequency()).isEqualTo(passed.getApplicantFrequency());
        assertThat(updated.getPartnerAmount()).isEqualTo(passed.getPartnerAmount());
        assertThat(updated.getPartnerFrequency()).isEqualTo(passed.getPartnerFrequency());
    }

    @Test
    void givenNewDetails_whenUpdateAssessmentDetailsIsInvoked_thenDetailsAreAdded() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTOWithDetails();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        when(financialAssessmentMapper.financialAssessmentDetailsToFinancialAssessmentDetailsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity());

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);

        FinancialAssessmentDetails passed =
                financialAssessment.getAssessmentDetails().getFirst();
        FinancialAssessmentDetailEntity updated =
                existingAssessment.getAssessmentDetails().getFirst();

        assertThat(updated.getCriteriaDetailId()).isEqualTo(passed.getCriteriaDetailId());
    }

    @Test
    void givenExistingChildWeightingsToBeRemoved_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addChildWeighting(
                ChildWeightingsEntity.builder().childWeightingId(20).build());

        financialAssessmentImpl.updateAssessmentDetails(financialAssessment, existingAssessment);
        assertThat(existingAssessment.getAssessmentDetails())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .isEmpty();
    }

    @Test
    void givenExistingChildWeightingsToBeUpdated_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentWithChildWeightings();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        existingAssessment.addChildWeighting(ChildWeightingsEntity.builder()
                .childWeightingId(12)
                .noOfChildren(3)
                .build());
        financialAssessmentImpl.updateChildWeightings(financialAssessment, existingAssessment);

        assertThat(existingAssessment.getChildWeightings().getFirst().getNoOfChildren())
                .isEqualTo(financialAssessment.getChildWeightings().getFirst().getNoOfChildren());
    }

    @Test
    void givenNewChildWeightings_whenUpdateChildWeightingsIsInvoked_thenWeightingsAreAdded() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentWithChildWeightings();
        FinancialAssessmentEntity existingAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        when(financialAssessmentMapper.childWeightingsToChildWeightingsEntity(any()))
                .thenReturn(TestEntityDataBuilder.getChildWeightingsEntity());

        financialAssessmentImpl.updateChildWeightings(financialAssessment, existingAssessment);

        assertThat(existingAssessment.getChildWeightings().getFirst().getChildWeightingId())
                .isEqualTo(financialAssessment.getChildWeightings().getFirst().getChildWeightingId());
    }

    @Test
    void
            givenNewFinancialEvidenceDTOWithNullMandatoryField_whenPopulateMandatoryFlagIsInvoked_MandatoryFieldIsNotSet() {
        FinAssIncomeEvidenceDTO finAssIncomeEvidenceDTO = TestModelDataBuilder.getFinAssIncomeEvidenceDTO();
        finAssIncomeEvidenceDTO.setId(123);
        List<FinAssIncomeEvidenceDTO> finAssIncomeEvidenceDTOS = List.of(finAssIncomeEvidenceDTO);

        FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity = TestEntityDataBuilder.getFinAssIncomeEvidenceEntity();
        finAssIncomeEvidenceEntity.setId(234);
        List<FinAssIncomeEvidenceEntity> finAssIncomeEvidences = List.of(finAssIncomeEvidenceEntity);

        financialAssessmentImpl.populateMandatoryFlag(finAssIncomeEvidenceDTOS, finAssIncomeEvidences);
        assertThat(finAssIncomeEvidenceDTO.getMandatory()).isNull();
    }

    @Test
    void
            givenExistingFinancialEvidenceDTOWithNullMandatoryField_whenPopulateMandatoryFlagIsInvoked_MandatoryFieldIsUpdated() {
        int evidenceId = 123;

        FinAssIncomeEvidenceDTO finAssIncomeEvidenceDTO = TestModelDataBuilder.getFinAssIncomeEvidenceDTO();
        finAssIncomeEvidenceDTO.setId(evidenceId);
        List<FinAssIncomeEvidenceDTO> finAssIncomeEvidenceDTOS = List.of(finAssIncomeEvidenceDTO);

        FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity = TestEntityDataBuilder.getFinAssIncomeEvidenceEntity();
        finAssIncomeEvidenceEntity.setId(evidenceId);
        finAssIncomeEvidenceEntity.setMandatory("Y");
        List<FinAssIncomeEvidenceEntity> finAssIncomeEvidences = List.of(finAssIncomeEvidenceEntity);

        financialAssessmentImpl.populateMandatoryFlag(finAssIncomeEvidenceDTOS, finAssIncomeEvidences);
        assertThat(finAssIncomeEvidenceDTO.getMandatory()).isEqualTo(finAssIncomeEvidenceEntity.getMandatory());
    }
}
