package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PassportAssessmentImplTest {

    @Spy
    @InjectMocks
    private PassportAssessmentImpl passportAssessmentImpl;

    @Spy
    private PassportAssessmentRepository passportAssessmentRepository;

    @Mock
    private PassportAssessmentMapper passportAssessmentMapper;

    @Captor
    private ArgumentCaptor<PassportAssessmentEntity> passportAssessmentEntityArgumentCaptor;


    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(passportAssessmentRepository.getById(any())).thenReturn(PassportAssessmentEntity.builder().id(1000).build());
        PassportAssessmentEntity returned = passportAssessmentImpl.find(1000);
        assertThat(returned.getId()).isEqualTo(1000);
    }

    @Test
    public void whenCreateIsInvoked_thenAssessmentIsSaved() {
        PassportAssessmentDTO passportAssessment = TestModelDataBuilder.getPassportAssessmentDTO();

        when(passportAssessmentMapper.passportAssessmentDtoToPassportAssessmentEntity(any())).thenReturn(TestEntityDataBuilder.getPassportAssessmentEntity());

        passportAssessmentImpl.create(passportAssessment);

        verify(passportAssessmentRepository).save(passportAssessmentEntityArgumentCaptor.capture());

        assertThat(passportAssessmentEntityArgumentCaptor.getValue().getRepId()).isEqualTo(passportAssessment.getRepId());
        assertThat(passportAssessmentEntityArgumentCaptor.getValue().getPartnerBenefitClaimed()).isEqualTo("Y");
        assertThat(passportAssessmentEntityArgumentCaptor.getValue().getUserCreated()).isEqualTo("test-f");
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        LocalDateTime now = LocalDateTime.now();
        PassportAssessmentDTO passportAssessment = TestModelDataBuilder.getPassportAssessmentDTO();
        passportAssessment.setDateCompleted(now);

        when(passportAssessmentRepository.getById(any())).thenReturn(TestEntityDataBuilder.getPassportAssessmentEntity());

        passportAssessmentImpl.update(passportAssessment);

        verify(passportAssessmentRepository).save(passportAssessmentEntityArgumentCaptor.capture());

        assertThat(passportAssessment.getDateCompleted()).isEqualTo(now);
        assertThat(passportAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(1000);
    }

    @Test
    public void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        Integer id = 1000;
        passportAssessmentImpl.delete(id);
        verify(passportAssessmentRepository).deleteById(id);
    }
}
