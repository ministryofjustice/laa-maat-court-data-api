package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.IOJAssessorDetails;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinancialAssessmentServiceTest {

    private static final Integer TEST_REP_ID = 1000;

    @InjectMocks
    private FinancialAssessmentService financialAssessmentService;

    @Mock
    private FinancialAssessmentImpl financialAssessmentImpl;

    @Mock
    private FinancialAssessmentMapper financialAssessmentMapper;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        FinancialAssessmentDTO financialAssessmentDTO = FinancialAssessmentDTO.builder().id(1000).build();
        when(financialAssessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(any()))
                .thenReturn(financialAssessmentDTO);

        FinancialAssessmentEntity financialAssessment = FinancialAssessmentEntity.builder().id(1000).build();
        when(financialAssessmentImpl.find(1000))
                .thenReturn(Optional.of(financialAssessment));

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.find(1000);

        verify(financialAssessmentImpl).find(1000);
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

        when(financialAssessmentMapper.createFinancialAssessmentToFinancialAssessmentDTO(any()))
                .thenReturn(financialAssessmentDTO);
        when(financialAssessmentImpl.create(any())).thenReturn(FinancialAssessmentEntity.builder().id(1000).build());
        when(financialAssessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(any()))
                .thenReturn(FinancialAssessmentDTO.builder().id(1000).build());

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.create(financialAssessment);
        verify(financialAssessmentImpl).setOldAssessmentReplaced(any(FinancialAssessmentEntity.class));

        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void whenUpdateIsInvoked_thenAssessmentIsUpdated() {
        FinancialAssessmentDTO financialAssessmentDTO = TestModelDataBuilder.getFinancialAssessmentDTO();
        UpdateFinancialAssessment financialAssessment = TestModelDataBuilder.getUpdateFinancialAssessment();

        when(financialAssessmentMapper.updateFinancialAssessmentToFinancialAssessmentDTO(any(UpdateFinancialAssessment.class)))
                .thenReturn(financialAssessmentDTO);
        when(financialAssessmentImpl.update(any())).thenReturn(FinancialAssessmentEntity.builder().id(1000).build());
        when(financialAssessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(any()))
                .thenReturn(FinancialAssessmentDTO.builder().id(1000).build());

        FinancialAssessmentDTO returnedAssessment = financialAssessmentService.update(financialAssessment);

        verify(financialAssessmentImpl).update(any(FinancialAssessmentDTO.class));
        assertThat(returnedAssessment.getId()).isEqualTo(1000);
    }

    @Test
    public void givenOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenResultIsReturned() {
        when(financialAssessmentImpl.checkForOutstandingAssessments(any(Integer.class))).thenReturn(
                OutstandingAssessmentResultDTO.builder().outstandingAssessments(true).message(MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND).build()
        );
        OutstandingAssessmentResultDTO result = financialAssessmentService.checkForOutstandingAssessments(TEST_REP_ID);
        verify(financialAssessmentImpl).checkForOutstandingAssessments(any());
        assertThat(result.isOutstandingAssessments()).isEqualTo(true);
        assertThat(result.getMessage()).isEqualTo(MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
    }

    @Test
    public void givenNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_thenNotFoundResultIsReturned() {
        when(financialAssessmentImpl.checkForOutstandingAssessments(TEST_REP_ID)).thenReturn(
                OutstandingAssessmentResultDTO.builder().build()
        );
        OutstandingAssessmentResultDTO result = financialAssessmentService.checkForOutstandingAssessments(TEST_REP_ID);
        verify(financialAssessmentImpl).checkForOutstandingAssessments(TEST_REP_ID);
        assertThat(result.isOutstandingAssessments()).isEqualTo(false);
    }

    @Test
    public void givenValidFinancialAssessmentId_whenFindIOJAssessorDetailsIsInvoked_thenPopulatedIOJAssessorDetailsAreReturned() throws Exception {
        int financialAssessmentId = 1234;
        final String username = TestEntityDataBuilder.IOJ_USER_NAME;
        FinancialAssessmentEntity financialAssessment = FinancialAssessmentEntity.builder()
                .userCreated(username)
                .userCreatedEntity(TestEntityDataBuilder.getUserEntity())
                .build();
        when(financialAssessmentImpl.find(financialAssessmentId))
                .thenReturn(Optional.of(financialAssessment));
        when(financialAssessmentMapper.createIOJAssessorDetails(financialAssessment))
                .thenReturn(TestModelDataBuilder.getIOJAssessorDetails());

        IOJAssessorDetails iojAssessorDetails = financialAssessmentService.findIOJAssessorDetails(financialAssessmentId);

        assertEquals("Karen Greaves", iojAssessorDetails.getFullName());
        assertEquals(username, iojAssessorDetails.getUserName());
    }

    @Test
    public void givenUnknownFinancialAssessmentId_whenFindIOJAssessorDetailsIsInvoked_thenNotFoundErrorIsReturned() {
        int unknownFinancialAssessmentId = 99999;
        when(financialAssessmentImpl.find(unknownFinancialAssessmentId))
                .thenReturn(Optional.empty());

        RequestedObjectNotFoundException actualException = Assertions.assertThrows(RequestedObjectNotFoundException.class,
                () -> financialAssessmentService.findIOJAssessorDetails(unknownFinancialAssessmentId));

        assertEquals("No Financial Assessment found for financial assessment Id: [99999]", actualException.getMessage());
    }
}
