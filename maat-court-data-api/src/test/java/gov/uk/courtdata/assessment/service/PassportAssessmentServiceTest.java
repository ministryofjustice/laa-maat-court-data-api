package gov.uk.courtdata.assessment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.assessment.impl.PassportAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static gov.uk.courtdata.assessment.service.PassportAssessmentService.STATUS_COMPLETE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PassportAssessmentServiceTest {

    private static final Integer MOCK_ASSESSMENT_ID = 1000;
    private static final int MOCK_REP_ID = TestEntityDataBuilder.REP_ID;

    @Mock
    private PassportAssessmentImpl passportAssessmentImpl;

    @Mock
    private PassportAssessmentMapper passportAssessmentMapper;

    @Mock
    private PassportAssessmentRepository passportAssessmentRepository;

    @Mock
    private AssessmentReplacementService assessmentReplacementService;

    @InjectMocks
    private PassportAssessmentService passportAssessmentService;

    @Test
    void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(MOCK_ASSESSMENT_ID).build());
        when(passportAssessmentImpl.find(any())).thenReturn(
                PassportAssessmentEntity.builder().id(MOCK_ASSESSMENT_ID).build());

        PassportAssessmentDTO returnedAssessment = passportAssessmentService.find(MOCK_ASSESSMENT_ID);

        verify(passportAssessmentImpl).find(any());
        assertThat(returnedAssessment.getId()).isEqualTo(MOCK_ASSESSMENT_ID);
    }

    @Test
    void whenFindIsInvokedWithInvalidId_thenNotFoundExceptionIsThrown() {
        when(passportAssessmentImpl.find(MOCK_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> passportAssessmentService.find(MOCK_REP_ID))
                .withMessageContaining(String.format("No Passport Assessment found for ID: %d", MOCK_REP_ID));
    }

    @Test
    void whenFindByRepIdIsInvoked_thenAssessmentIsRetrieved() {
        PassportAssessmentEntity passportAssessmentEntity = PassportAssessmentEntity.builder()
                .id(MOCK_ASSESSMENT_ID)
                .repOrder(TestEntityDataBuilder.getRepOrder())
                .build();
        when(passportAssessmentImpl.findByRepId(MOCK_REP_ID)).thenReturn(passportAssessmentEntity);
        when(passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(passportAssessmentEntity))
                .thenReturn(PassportAssessmentDTO.builder().id(MOCK_ASSESSMENT_ID).repId(MOCK_REP_ID).build());

        PassportAssessmentDTO returnedAssessment = passportAssessmentService.findByRepId(MOCK_REP_ID);

        verify(passportAssessmentImpl).findByRepId(MOCK_REP_ID);
        verify(passportAssessmentMapper).passportAssessmentEntityToPassportAssessmentDTO(passportAssessmentEntity);
        assertThat(returnedAssessment.getId()).isEqualTo(MOCK_ASSESSMENT_ID);
        assertThat(returnedAssessment.getRepId()).isEqualTo(MOCK_REP_ID);
    }

    @Test
    void whenFindByRepIdIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(passportAssessmentImpl.findByRepId(MOCK_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> passportAssessmentService.findByRepId(MOCK_REP_ID))
                .withMessageContaining(String.format("No Passport Assessment found for REP ID: %d", MOCK_REP_ID));
    }

    @Test
    void whenDeleteIsInvoked_thenAssessmentIsDeleted() {
        passportAssessmentService.delete(MOCK_ASSESSMENT_ID);
        verify(passportAssessmentImpl).delete(any(Integer.class));
    }

    @Test
    void whenCreateIsInvoked_thenAssessmentIsCreated() {
        PassportAssessmentDTO passportAssessmentDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        CreatePassportAssessment passportAssessment = TestModelDataBuilder.getCreatePassportAssessment();

        when(passportAssessmentMapper.createPassportAssessmentToPassportAssessmentDTO(any())).thenReturn(passportAssessmentDTO);
        when(passportAssessmentImpl.create(any())).thenReturn(
                PassportAssessmentEntity.builder().id(MOCK_ASSESSMENT_ID).build());
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(MOCK_ASSESSMENT_ID).build());

        PassportAssessmentDTO returnedAssessment = passportAssessmentService.create(passportAssessment);

        verify(passportAssessmentImpl).create(any(PassportAssessmentDTO.class));
        verify(assessmentReplacementService).replacePreviousAssessments(any(PassportAssessmentEntity.class));

        assertThat(returnedAssessment.getId()).isEqualTo(MOCK_ASSESSMENT_ID);
    }

    @Test
    void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        PassportAssessmentDTO passportAssessmentDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        passportAssessmentDTO.setId(MOCK_ASSESSMENT_ID);
        UpdatePassportAssessment passportAssessment = TestModelDataBuilder.getUpdatePassportAssessment();
        PassportAssessmentEntity existingPassportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        existingPassportAssessmentEntity.setPastStatus("NEW");
        when(passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(any(UpdatePassportAssessment.class))).thenReturn(passportAssessmentDTO);
        when(passportAssessmentImpl.find(any(Integer.class))).thenReturn(existingPassportAssessmentEntity);
        when(passportAssessmentImpl.update(any())).thenReturn(
                PassportAssessmentEntity.builder().id(MOCK_ASSESSMENT_ID).build());
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(MOCK_ASSESSMENT_ID).build());

        PassportAssessmentDTO returnedAssessment = passportAssessmentService.update(passportAssessment);

        verify(passportAssessmentImpl).update(any(PassportAssessmentDTO.class));
        assertThat(returnedAssessment.getId()).isEqualTo(MOCK_ASSESSMENT_ID);
    }

    @Test
    void whenUpdateIsInvokedOnCompletedPassportAssessment_thenValidationExceptionIsThrown() {
        PassportAssessmentDTO passportAssessmentDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        passportAssessmentDTO.setId(MOCK_ASSESSMENT_ID);
        passportAssessmentDTO.setPastStatus(STATUS_COMPLETE);
        UpdatePassportAssessment passportAssessment = TestModelDataBuilder.getUpdatePassportAssessment();
        PassportAssessmentEntity existingPassportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        when(passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(any(UpdatePassportAssessment.class))).thenReturn(passportAssessmentDTO);
        when(passportAssessmentImpl.find(any(Integer.class))).thenReturn(existingPassportAssessmentEntity);
        when(passportAssessmentService.buildPassportAssessmentDTO(any())).thenReturn(
                PassportAssessmentDTO.builder().id(MOCK_ASSESSMENT_ID).build());

        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentService.update(passportAssessment));
        assertThat(validationException.getMessage()).isEqualTo("User cannot modify a completed assessment");
    }

    @Test
    void whenBuildPassportAssessmentDTOIsInvoked_thenDTOIsReturned() {
        PassportAssessmentEntity passportAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        when(passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(any())).thenReturn(TestModelDataBuilder.getPassportAssessmentDTO());
        PassportAssessmentDTO expectedDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        PassportAssessmentDTO actualDTO = passportAssessmentService.buildPassportAssessmentDTO(passportAssessment);
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    void whenBuildPassportAssessmentDTOIsInvokedWithNoAssessmentDetails_thenDTOWithNoDetailsIsReturned() {
        PassportAssessmentEntity passportAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        when(passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(any())).thenReturn(TestModelDataBuilder.getPassportAssessmentDTO());
        PassportAssessmentDTO expectedDTO = TestModelDataBuilder.getPassportAssessmentDTO();
        PassportAssessmentDTO actualDTO = passportAssessmentService.buildPassportAssessmentDTO(passportAssessment);
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    void givenValidPassportAssessmentId_whenFindPassportAssessorDetailsIsInvoked_thenPopulatedAssessorDetailsAreReturned() {
        int passportAssessmentId = 1234;
        final String username = TestEntityDataBuilder.ASSESSOR_USER_NAME;
        PassportAssessmentEntity passportAssessment = PassportAssessmentEntity.builder()
                .userCreated(username)
                .userCreatedEntity(TestEntityDataBuilder.getUserEntity())
                .build();
        when(passportAssessmentImpl.find(passportAssessmentId))
                .thenReturn(passportAssessment);

        AssessorDetails passportAssessorDetails = passportAssessmentService.findPassportAssessorDetails(passportAssessmentId);

        assertEquals("Karen Greaves", passportAssessorDetails.getFullName());
        assertEquals(username, passportAssessorDetails.getUserName());
    }

    @Test
    void givenValidPassportAssessment_whenPatchIsInvoked_thenAssessmentIsUpdated() throws JsonProcessingException {
        PassportAssessmentEntity passportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        String requestJson = "{\"replaced\":\"Y\"}";
        Map<String, Object> updateFields = new ObjectMapper().readValue(requestJson, HashMap.class);
        when(passportAssessmentRepository.findById(anyInt())).thenReturn(Optional.of(passportAssessmentEntity));
        passportAssessmentService.patch(anyInt(), updateFields);
        verify(passportAssessmentRepository).save(passportAssessmentEntity);
    }
}
