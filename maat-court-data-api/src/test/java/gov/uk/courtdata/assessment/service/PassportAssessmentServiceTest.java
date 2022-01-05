package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.PassportAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
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
public class PassportAssessmentServiceTest {

    @InjectMocks
    private PassportAssessmentService passportAssessmentService;

    @Mock
    private PassportAssessmentImpl passportAssessmentImpl;

    @Mock
    private PassportAssessmentMapper passportAssessmentMapper;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(1000).build()
        );
        PassportAssessmentDTO returnedAssessment = passportAssessmentService.find(1000);

        verify(passportAssessmentImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        passportAssessmentService.delete(1000);
        verify(passportAssessmentImpl).delete(any(Integer.class));
    }

    @Test
    public void whenCreateIsInvoked_thenAssessmentIsCreated() {
        PassportAssessmentDTO passportAssessmentDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        CreatePassportAssessment passportAssessment = TestModelDataBuilder.getCreatePassportAssessment();

        when(passportAssessmentMapper.createPassportAssessmentToPassportAssessmentDTO(any())).thenReturn(passportAssessmentDTO);
        when(passportAssessmentImpl.create(any())).thenReturn(
                PassportAssessmentEntity.builder().id(1000).build()
        );
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(1000).build()
        );

        PassportAssessmentDTO returnedAssessment = passportAssessmentService.create(passportAssessment);

        verify(passportAssessmentImpl).create(any(PassportAssessmentDTO.class));
        verify(passportAssessmentImpl).setOldPassportAssessmentAsReplaced(any(PassportAssessmentDTO.class));

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        PassportAssessmentDTO passportAssessmentDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        UpdatePassportAssessment passportAssessment = TestModelDataBuilder.getUpdatePassportAssessment();

        when(passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(any(UpdatePassportAssessment.class))).thenReturn(passportAssessmentDTO);
        when(passportAssessmentImpl.update(any())).thenReturn(
                PassportAssessmentEntity.builder().id(1000).build()
        );
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(1000).build()
        );

        PassportAssessmentDTO returnedAssessment = passportAssessmentService.update(passportAssessment);

        verify(passportAssessmentImpl).update(any(PassportAssessmentDTO.class));
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenBuildPassportAssessmentDTOIsInvoked_thenDTOIsReturned() {
        PassportAssessmentEntity passportAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        when(passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(any())).thenReturn(TestModelDataBuilder.getPassportAssessmentDTO());
        PassportAssessmentDTO expectedDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        PassportAssessmentDTO actualDTO = passportAssessmentService.buildPassportAssessmentDTO(passportAssessment);
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void whenBuildPassportAssessmentDTOIsInvokedWithNoAssessmentDetails_thenDTOWithNoDetailsIsReturned() {
        PassportAssessmentEntity passportAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        when(passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(any())).thenReturn(TestModelDataBuilder.getPassportAssessmentDTO());
        PassportAssessmentDTO expectedDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        PassportAssessmentDTO actualDTO = passportAssessmentService.buildPassportAssessmentDTO(passportAssessment);
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }
}
