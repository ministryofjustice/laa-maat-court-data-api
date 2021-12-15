package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentDetailsImpl;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentServiceTest {

    @InjectMocks
    private FinancialAssessmentService financialAssessmentService;

    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Mock
    private FinancialAssessmentDetailsImpl financialAssessmentDetailsImpl;

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(financialAssessmentService.buildFinancialAssessmentDTO(any())).thenReturn(
                FinancialAssessmentDTO.builder().id(1000).build()
        );
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

        when(financialAssessmentMapper.CreateFinancialAssessmentToFinancialAssessmentDTO(any())).thenReturn(financialAssessmentDTO);
        when(financialAssessmentImpl.create(any())).thenReturn(
                FinancialAssessmentEntity.builder().id(1000).build()
        );
        when(financialAssessmentService.buildFinancialAssessmentDTO(any())).thenReturn(
                FinancialAssessmentDTO.builder().id(1000).build()
        );

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.create(financialAssessment);

        verify(financialAssessmentDetailsImpl).save(any(FinancialAssessmentEntity.class), isNull());
        verify(financialAssessmentImpl).setOldAssessmentReplaced(any(FinancialAssessmentDTO.class));

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessmentDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        UpdateFinancialAssessment financialAssessment = TestModelDataBuilder.getUpdateFinancialAssessment();

        when(financialAssessmentMapper.UpdateFinancialAssessmentToFinancialAssessmentDTO(any(UpdateFinancialAssessment.class))).thenReturn(financialAssessmentDTO);
        when(financialAssessmentImpl.update(any())).thenReturn(
                FinancialAssessmentEntity.builder().id(1000).build()
        );
        when(financialAssessmentService.buildFinancialAssessmentDTO(any())).thenReturn(
                FinancialAssessmentDTO.builder().id(1000).build()
        );

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.update(financialAssessment);

        verify(financialAssessmentImpl).update(any(FinancialAssessmentDTO.class));
        verify(financialAssessmentDetailsImpl).deleteStaleAssessmentDetails(any(FinancialAssessmentDTO.class));
        verify(financialAssessmentDetailsImpl).save(any(FinancialAssessmentEntity.class), isNull());

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
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

        FinancialAssessmentDTO expectedDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        expectedDTO.setAssessmentDetailsList(List.of(mockFinancialAssessmentDetails));
        FinancialAssessmentDTO actualDTO = financialAssessmentService.buildFinancialAssessmentDTO(financialAssessment, List.of(financialAssessmentDetails));

        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void whenBuildFinancialAssessmentDTOIsInvokedWithNoAssessmentDetails_thenDTOWithNoDetailsIsReturned() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        when(financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(any())).thenReturn(TestModelDataBuilder.getFinancialAssessmentDTO());

        FinancialAssessmentDTO expectedDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        FinancialAssessmentDTO actualDTO = financialAssessmentService.buildFinancialAssessmentDTO(financialAssessment);

        assertThat(actualDTO).isEqualTo(expectedDTO);
        assertThat(actualDTO.getAssessmentDetailsList()).isNull();


    }
}
