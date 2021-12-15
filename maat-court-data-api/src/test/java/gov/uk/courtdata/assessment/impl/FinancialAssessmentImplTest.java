package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsRepository;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private FinancialAssessmentDetailsRepository financialAssessmentDetailsRepository;

    @Captor
    private ArgumentCaptor<FinancialAssessmentEntity> financialAssessmentEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<ArrayList<Integer>> idListCaptor;

    @Captor
    private ArgumentCaptor<List<FinancialAssessmentDetailsEntity>> financialAssessmentDetailsEntityArgumentCaptor;

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;

    @Mock
    private PassportAssessmentRepository passportAssessmentRepository;

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Test
    public void whenGetAssessmentIsInvoked_thenAssessmentIsRetrieved() {
        when(financialAssessmentRepository.getById(any())).thenReturn(FinancialAssessmentEntity.builder().id(1000).build());
        FinancialAssessmentEntity returned = financialAssessmentImpl.getAssessment(1000);
        assertThat(returned.getId()).isEqualTo(1000);
    }

    @Test
    public void whenCreateAssessmentIsInvoked_thenAssessmentIsSaved() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();

        doReturn(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity())).when(financialAssessmentImpl).storeAssessmentDetails(any(), any());
        doReturn(TestModelDataBuilder.getFinancialAssessmentWithDetails()).when(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());

        when(financialAssessmentMapper.FinancialAssessmentDtoToFinancialAssessmentEntity(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.createAssessment(financialAssessment);

        verify(financialAssessmentRepository).updateOldAssessments(any());
        verify(passportAssessmentRepository).updateOldPassportAssessments(any());
        verify(hardshipReviewRepository).updateOldHardshipReviews(any(), any());
        verify(financialAssessmentImpl).storeAssessmentDetails(any(), any());
        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getRepId()).isEqualTo(financialAssessment.getRepId());
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("INIT");
    }

    @Test
    public void whenUpdateAssessmentIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        financialAssessment.setFullAssessmentDate(LocalDateTime.now());

        doReturn(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity())).when(financialAssessmentImpl).storeAssessmentDetails(any(), any());
        doReturn(TestModelDataBuilder.getFinancialAssessmentWithDetails()).when(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());

        when(financialAssessmentRepository.getById(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.updateAssessment(financialAssessment);

        verify(financialAssessmentImpl).storeAssessmentDetails(any(), any());
        verify(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());
        verify(financialAssessmentImpl).deleteStaleAssessmentDetails(any());
        verify(financialAssessmentRepository).save(any());
    }

    @Test
    public void whenUpdateAssessmentIsInvokedWithFullAssessmentDate_thenAssessmentTypeIsSet() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        financialAssessment.setFullAssessmentDate(LocalDateTime.now());

        doReturn(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity())).when(financialAssessmentImpl).storeAssessmentDetails(any(), any());
        doReturn(TestModelDataBuilder.getFinancialAssessmentWithDetails()).when(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());

        when(financialAssessmentRepository.getById(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());
        when(financialAssessmentRepository.save(financialAssessmentEntityArgumentCaptor.capture())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.updateAssessment(financialAssessment);

        assertThat(financialAssessment.getAssessmentType()).isEqualTo("INIT");
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(1000);
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("FULL");
    }

    @Test
    public void whenDeleteAssessmentIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentImpl.deleteAssessment(1000);
        verify(financialAssessmentRepository).deleteById(any());
    }

    @Test
    public void whenDeleteStaleAssessmentDetailsIsInvoked_thenStaleAssessmentDetailsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentWithDetails();

        when(financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(any())).thenReturn(
                List.of(
                        FinancialAssessmentDetailsEntity.builder()
                                .id(12345)
                                .criteriaDetailId(41)
                                .financialAssessmentId(financialAssessment.getId())
                                .userCreated("test-f")
                                .applicantAmount(BigDecimal.valueOf(1000))
                                .applicantFrequency(Frequency.MONTHLY)
                                .build(),

                        FinancialAssessmentDetailsEntity.builder()
                                .id(67890)
                                .criteriaDetailId(39)
                                .financialAssessmentId(financialAssessment.getId())
                                .userCreated("test-f")
                                .applicantAmount(BigDecimal.valueOf(500))
                                .applicantFrequency(Frequency.TWO_WEEKLY)
                                .build()

                )
        );
        doNothing().when(financialAssessmentDetailsRepository).deleteAllByIdInBatch(idListCaptor.capture());

        financialAssessmentImpl.deleteStaleAssessmentDetails(financialAssessment);

        verify(financialAssessmentDetailsRepository).deleteAllByIdInBatch(any());

        assertThat(idListCaptor.getValue().get(0)).isEqualTo(12345);
        assertThat(idListCaptor.getValue().get(1)).isEqualTo(67890);
    }

    @Test
    public void whenBuildFinancialAssessmentDTOIsInvoked_thenDTOIsReturned() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        FinancialAssessmentDetailsEntity financialAssessmentDetails = TestEntityDataBuilder.getFinancialAssessmentDetailsEntity();

        FinancialAssessmentDetails mockFinancialAssessmentDetails =
                FinancialAssessmentDetails.builder()
                        .criteriaDetailId(40)
                        .applicantAmount(BigDecimal.valueOf(1650.00))
                        .applicantFrequency(Frequency.MONTHLY)
                        .partnerAmount(BigDecimal.valueOf(1650.00))
                        .partnerFrequency(Frequency.TWO_WEEKLY)
                        .build();

        when(financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(any())).thenReturn(TestModelDataBuilder.getFinancialAssessmentDTO());
        when(financialAssessmentMapper.FinancialAssessmentDetailsEntityToFinancialAssessmentDetails(any())).thenReturn(mockFinancialAssessmentDetails);

        FinancialAssessmentDTO actualDTO = financialAssessmentImpl.buildFinancialAssessmentDTO(financialAssessment, List.of(financialAssessmentDetails));

        verify(financialAssessmentMapper).FinancialAssessmentEntityToFinancialAssessmentDTO(any());

        FinancialAssessmentDTO expectedDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        expectedDTO.setAssessmentDetailsList(List.of(mockFinancialAssessmentDetails));
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void whenStoreAssessmentDetailsIsInvoked_thenAssessmentDetailsAreCreated() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        List<FinancialAssessmentDetails> financialAssessmentDetails = List.of(TestModelDataBuilder.getFinancialAssessmentDetails());

        when(financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(any())).thenReturn(List.of());
        when(financialAssessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(any())).thenReturn(
                FinancialAssessmentDetailsEntity.builder()
                        .criteriaDetailId(40)
                        .applicantAmount(BigDecimal.valueOf(1650.00))
                        .applicantFrequency(Frequency.MONTHLY)
                        .partnerAmount(BigDecimal.valueOf(1650.00))
                        .partnerFrequency(Frequency.TWO_WEEKLY)
                        .build()
        );

        financialAssessmentImpl.storeAssessmentDetails(financialAssessment, financialAssessmentDetails);

        verify(financialAssessmentDetailsRepository).saveAll(financialAssessmentDetailsEntityArgumentCaptor.capture());

        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getId()).isNull();
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getCriteriaDetailId()).isEqualTo(40);
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getFinancialAssessmentId()).isEqualTo(1000);
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getUserCreated()).isEqualTo("test-f");
    }

    @Test
    public void whenStoreAssessmentDetailsIsInvoked_thenExistingAssessmentDetailsAreUpdated() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        List<FinancialAssessmentDetails> financialAssessmentDetails = List.of(
                TestModelDataBuilder.getFinancialAssessmentDetails(),
                TestModelDataBuilder.getFinancialAssessmentDetails()
        );

        financialAssessmentDetails.get(1).setCriteriaDetailId(41);

        List<FinancialAssessmentDetailsEntity> financialAssessmentDetailsEntities = List.of(
                FinancialAssessmentDetailsEntity.builder()
                        .criteriaDetailId(40)
                        .applicantAmount(BigDecimal.valueOf(1650.00))
                        .applicantFrequency(Frequency.MONTHLY)
                        .partnerAmount(BigDecimal.valueOf(1650.00))
                        .partnerFrequency(Frequency.TWO_WEEKLY)
                        .build(),

                FinancialAssessmentDetailsEntity.builder()
                        .criteriaDetailId(41)
                        .applicantAmount(BigDecimal.valueOf(500))
                        .applicantFrequency(Frequency.WEEKLY)
                        .partnerAmount(BigDecimal.valueOf(19000.00))
                        .partnerFrequency(Frequency.ANNUALLY)
                        .build()
        );

        when(financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(any())).thenReturn(List.of(
                FinancialAssessmentDetailsEntity.builder()
                        .id(1234)
                        .criteriaDetailId(41)
                        .build()
        ));

        when(financialAssessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(any()))
                .thenReturn(financialAssessmentDetailsEntities.get(0))
                .thenReturn(financialAssessmentDetailsEntities.get(1));

        financialAssessmentImpl.storeAssessmentDetails(financialAssessment, financialAssessmentDetails);

        verify(financialAssessmentDetailsRepository).saveAll(financialAssessmentDetailsEntityArgumentCaptor.capture());

        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getId()).isNull();
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getCriteriaDetailId()).isEqualTo(40);
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getFinancialAssessmentId()).isEqualTo(1000);
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(0).getUserCreated()).isEqualTo("test-f");

        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(1).getId()).isEqualTo(1234);
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(1).getCriteriaDetailId()).isEqualTo(41);
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(1).getFinancialAssessmentId()).isEqualTo(1000);
        assertThat(financialAssessmentDetailsEntityArgumentCaptor.getValue().get(1).getUserModified()).isEqualTo("test-f");

    }


}
