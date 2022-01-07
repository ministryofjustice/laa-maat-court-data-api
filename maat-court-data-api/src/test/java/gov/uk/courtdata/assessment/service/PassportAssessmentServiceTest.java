package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.PassportAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static gov.uk.courtdata.assessment.service.PassportAssessmentService.STATUS_COMPLETE;
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
        passportAssessmentDTO.setId(1000);
        UpdatePassportAssessment passportAssessment = TestModelDataBuilder.getUpdatePassportAssessment();
        PassportAssessmentEntity existingPassportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        existingPassportAssessmentEntity.setPastStatus("NEW");
        when(passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(any(UpdatePassportAssessment.class))).thenReturn(passportAssessmentDTO);
        when(passportAssessmentImpl.find(any(Integer.class))).thenReturn(existingPassportAssessmentEntity);
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
    public void whenUpdateIsInvokedWithInvalidId_thenValidationExceptionIsThrown() {
        PassportAssessmentDTO passportAssessmentDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        passportAssessmentDTO.setId(20000);
        UpdatePassportAssessment passportAssessment = TestModelDataBuilder.getUpdatePassportAssessment();
        PassportAssessmentEntity existingPassportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        when(passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(any(UpdatePassportAssessment.class))).thenReturn(passportAssessmentDTO);
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(1000).build()
        );

        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> passportAssessmentService.update(passportAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Passport assessment with id 20000 not found !");
    }

    public void whenUpdateIsInvokedOnCompletedPassportAssessment_thenValidationExceptionIsThrown() {
        PassportAssessmentDTO passportAssessmentDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        passportAssessmentDTO.setId(1000);
        passportAssessmentDTO.setPastStatus(STATUS_COMPLETE);
        UpdatePassportAssessment passportAssessment = TestModelDataBuilder.getUpdatePassportAssessment();
        PassportAssessmentEntity existingPassportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        when(passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(any(UpdatePassportAssessment.class))).thenReturn(passportAssessmentDTO);
        when(passportAssessmentImpl.find(any(Integer.class))).thenReturn(existingPassportAssessmentEntity);
        when(passportAssessmentImpl.update(any())).thenReturn(
                PassportAssessmentEntity.builder().id(1000).build()
        );
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(1000).build()
        );

        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> passportAssessmentService.update(passportAssessment));
        assertThat(validationException.getMessage()).isEqualTo("User cannot modify a completed assessment");
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
