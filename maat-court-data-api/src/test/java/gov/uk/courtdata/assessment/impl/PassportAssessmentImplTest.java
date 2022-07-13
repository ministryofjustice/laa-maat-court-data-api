package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private static final int MOCK_REP_ID = 5678;
    
    private static final int MOCK_ASSESSMENT_ID = 1000;


    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(passportAssessmentRepository.getById(any())).thenReturn(PassportAssessmentEntity.builder().id(MOCK_ASSESSMENT_ID).build());
        PassportAssessmentEntity returned = passportAssessmentImpl.find(MOCK_ASSESSMENT_ID);
        assertThat(returned.getId()).isEqualTo(MOCK_ASSESSMENT_ID);
    }

    @Test
    public void whenFindByRepIdIsInvoked_thenAssessmentIsRetrieved() {
        when(passportAssessmentRepository.findByRepId(MOCK_REP_ID))
                .thenReturn(PassportAssessmentEntity.builder().id(MOCK_ASSESSMENT_ID).repId(MOCK_REP_ID).build());

        PassportAssessmentEntity returned = passportAssessmentImpl.findByRepId(MOCK_REP_ID);

        assertThat(returned.getId()).isEqualTo(MOCK_ASSESSMENT_ID);
        assertThat(returned.getRepId()).isEqualTo(MOCK_REP_ID);
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
        assertThat(passportAssessmentEntityArgumentCaptor.getValue().getId()).isEqualTo(MOCK_ASSESSMENT_ID);
    }

    @Test
    public void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        Integer id = MOCK_ASSESSMENT_ID;
        passportAssessmentImpl.delete(id);
        verify(passportAssessmentRepository).deleteById(id);
    }
}
