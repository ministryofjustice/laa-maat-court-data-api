package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentImplTest {

    @Spy
    @InjectMocks
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Spy
    private FinancialAssessmentRepository financialAssessmentRepository;

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

//        doReturn(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity())).when(financialAssessmentImpl).storeAssessmentDetails(any(), any());
//        doReturn(TestModelDataBuilder.getFinancialAssessmentWithDetails()).when(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());

        when(financialAssessmentMapper.FinancialAssessmentDtoToFinancialAssessmentEntity(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.create(financialAssessment);

//        verify(financialAssessmentRepository).updateOldAssessments(any());
//        verify(passportAssessmentRepository).updateOldPassportAssessments(any());
//        verify(hardshipReviewRepository).updateOldHardshipReviews(any(), any());
//        verify(financialAssessmentImpl).storeAssessmentDetails(any(), any());
        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());

        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getRepId()).isEqualTo(financialAssessment.getRepId());
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("INIT");
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
        financialAssessment.setFullAssessmentDate(LocalDateTime.now());

//        doReturn(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity())).when(financialAssessmentImpl).storeAssessmentDetails(any(), any());
//        doReturn(TestModelDataBuilder.getFinancialAssessmentWithDetails()).when(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());

        when(financialAssessmentRepository.getById(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());

        financialAssessmentImpl.update(financialAssessment);

//        verify(financialAssessmentImpl).storeAssessmentDetails(any(), any());
//        verify(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());
//        verify(financialAssessmentImpl).deleteStaleAssessmentDetails(any());
        verify(financialAssessmentRepository).save(financialAssessmentEntityArgumentCaptor.capture());

        assertThat(financialAssessment.getAssessmentType()).isEqualTo("INIT");
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(1000);
        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("FULL");

    }

//    @Test
//    public void whenUpdateAssessmentIsInvokedWithFullAssessmentDate_thenAssessmentTypeIsSet() {
//        FinancialAssessmentDTO financialAssessment = TestModelDataBuilder.getFinancialAssessmentDTO();
//        financialAssessment.setFullAssessmentDate(LocalDateTime.now());
//
//        doReturn(List.of(TestEntityDataBuilder.getFinancialAssessmentDetailsEntity())).when(financialAssessmentImpl).storeAssessmentDetails(any(), any());
//        doReturn(TestModelDataBuilder.getFinancialAssessmentWithDetails()).when(financialAssessmentImpl).buildFinancialAssessmentDTO(any(), any());
//
//        when(financialAssessmentRepository.getById(any())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());
//        when(financialAssessmentRepository.save(financialAssessmentEntityArgumentCaptor.capture())).thenReturn(TestEntityDataBuilder.getFinancialAssessmentEntity());
//
//        financialAssessmentImpl.update(financialAssessment);
//
//        assertThat(financialAssessment.getAssessmentType()).isEqualTo("INIT");
//        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(1000);
//        assertThat(financialAssessmentEntityArgumentCaptor.getValue().getAssessmentType()).isEqualTo("FULL");
//    }

    @Test
    public void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        financialAssessmentImpl.delete(1000);
        verify(financialAssessmentRepository).deleteById(any());
    }
}
