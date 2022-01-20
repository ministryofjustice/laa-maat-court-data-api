package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentDetailsImplTest {

    @Spy
    @InjectMocks
    private FinancialAssessmentDetailsImpl financialAssessmentDetailsImpl;

    @Spy
    private FinancialAssessmentDetailsRepository financialAssessmentDetailsRepository;

    @Captor
    private ArgumentCaptor<ArrayList<Integer>> idListCaptor;

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Captor
    private ArgumentCaptor<List<FinancialAssessmentDetailEntity>> financialAssessmentDetailsEntityArgumentCaptor;


    @Test
    public void whenDeleteStaleAssessmentDetailsIsInvoked_thenStaleAssessmentDetailsAreDeleted() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentWithDetails();

        when(financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(any())).thenReturn(
                List.of(
                        FinancialAssessmentDetailEntity.builder()
                                .id(12345)
                                .criteriaDetailId(41)
                                .financialAssessmentId(financialAssessment.getId())
                                .userCreated("test-f")
                                .applicantAmount(BigDecimal.valueOf(1000))
                                .applicantFrequency(Frequency.MONTHLY)
                                .build(),

                        FinancialAssessmentDetailEntity.builder()
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

        financialAssessmentDetailsImpl.deleteStaleAssessmentDetails(financialAssessment);

        verify(financialAssessmentDetailsRepository).deleteAllByIdInBatch(any());

        assertThat(idListCaptor.getValue().get(0)).isEqualTo(12345);
        assertThat(idListCaptor.getValue().get(1)).isEqualTo(67890);
    }

    @Test
    public void whenSaveIsInvoked_thenAssessmentDetailsAreCreated() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        List<FinancialAssessmentDetails> financialAssessmentDetails = List.of(TestModelDataBuilder.getFinancialAssessmentDetails());

        when(financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(any())).thenReturn(List.of());
        when(financialAssessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(any())).thenReturn(
                FinancialAssessmentDetailEntity.builder()
                        .criteriaDetailId(40)
                        .applicantAmount(BigDecimal.valueOf(1650.00))
                        .applicantFrequency(Frequency.MONTHLY)
                        .partnerAmount(BigDecimal.valueOf(1650.00))
                        .partnerFrequency(Frequency.TWO_WEEKLY)
                        .build()
        );

        financialAssessmentDetailsImpl.save(financialAssessment, financialAssessmentDetails);

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

        List<FinancialAssessmentDetailEntity> financialAssessmentDetailsEntities = List.of(
                FinancialAssessmentDetailEntity.builder()
                        .criteriaDetailId(40)
                        .applicantAmount(BigDecimal.valueOf(1650.00))
                        .applicantFrequency(Frequency.MONTHLY)
                        .partnerAmount(BigDecimal.valueOf(1650.00))
                        .partnerFrequency(Frequency.TWO_WEEKLY)
                        .build(),

                FinancialAssessmentDetailEntity.builder()
                        .criteriaDetailId(41)
                        .applicantAmount(BigDecimal.valueOf(500))
                        .applicantFrequency(Frequency.WEEKLY)
                        .partnerAmount(BigDecimal.valueOf(19000.00))
                        .partnerFrequency(Frequency.ANNUALLY)
                        .build()
        );

        when(financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(any())).thenReturn(List.of(
                FinancialAssessmentDetailEntity.builder()
                        .id(1234)
                        .criteriaDetailId(41)
                        .build()
        ));

        when(financialAssessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(any()))
                .thenReturn(financialAssessmentDetailsEntities.get(0))
                .thenReturn(financialAssessmentDetailsEntities.get(1));

        financialAssessmentDetailsImpl.save(financialAssessment, financialAssessmentDetails);

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
