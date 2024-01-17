package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.impl.RepOrderImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import gov.uk.courtdata.dto.IOJAssessorDetails;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RepOrderServiceTest {

    @Mock
    private RepOrderImpl repOrderImpl;

    @Mock
    private RepOrderRepository repOrderRepository;

    @Autowired
    private RepOrderMapper repOrderMapper;

    private RepOrderService repOrderService;

    @BeforeEach
    public void setup(){
        repOrderService = new RepOrderService(repOrderImpl,
                repOrderMapper,
                repOrderRepository);
    }

    @Test
    void givenValidRepIdAndSentenceOrderFlagIsFalse_whenFindIsInvoked_thenRepOrderIsReturned() {
        when(repOrderImpl.find(anyInt()))
                .thenReturn(TestEntityDataBuilder.getRepOrder());

        repOrderService.find(TestModelDataBuilder.REP_ID, false);
        verify(repOrderImpl).find(anyInt());
    }

    @Test
    void givenValidRepIdAndSentenceOrderFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() {
        when(repOrderImpl.findWithSentenceOrderDate(anyInt()))
                .thenReturn(TestEntityDataBuilder.getRepOrder());

        repOrderService.find(TestModelDataBuilder.REP_ID, true);
        verify(repOrderImpl).findWithSentenceOrderDate(anyInt());
    }

    @Test
    void givenInvalidRepId_whenFindIsInvoked_thenErrorIsThrown() {
        assertThatThrownBy(() -> repOrderService.find(TestModelDataBuilder.REP_ID, false))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("No Rep Order found for ID: " + TestModelDataBuilder.REP_ID);
    }

    @Test
    void givenDateCompletedObject_whenUpdateAppDateCompletedIsInvoked_thenAppDateIsUpdated() {
        repOrderService.updateDateCompleted(new UpdateAppDateCompleted());
        verify(repOrderImpl).updateAppDateCompleted(any(), any());
    }

    @Test
    void givenValidRepId_whenExistsIsInvoked_thenReturnTrue() {
        when(repOrderImpl.countWithSentenceOrderDate(any()))
                .thenReturn(1L);
        assertThat(repOrderService.exists(TestModelDataBuilder.REP_ID)).isTrue();
    }

    @Test
    void givenInvalidRepId_whenExistsIsInvoked_thenReturnFalse() {
        when(repOrderImpl.countWithSentenceOrderDate(any()))
                .thenReturn(0L);
        assertThat(repOrderService.exists(TestModelDataBuilder.REP_ID)).isFalse();
    }

    @Test
    void givenValidRepId_whenFindIOJAssessorDetailsIsInvoked_thenIOJAssessorDetailsAreReturnedWithFullName() {
        RepOrderEntity repOrder = TestEntityDataBuilder.getPopulatedRepOrder(TestModelDataBuilder.REP_ID);
        repOrder.setUserCreated("grea-k");
        repOrder.setUserCreatedEntity(TestEntityDataBuilder.getUserEntity("grea-k"));

        when(repOrderRepository.findById(TestModelDataBuilder.REP_ID))
                .thenReturn(Optional.of(repOrder));

        IOJAssessorDetails actualIOJAssessorDetails = repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID);

        assertAll("verify actual IOJAssessorDetails",
                () -> assertEquals("Karen Greaves", actualIOJAssessorDetails.getFullName()),
                () -> assertEquals("grea-k", actualIOJAssessorDetails.getUserName()));
    }

    @Test
    void givenValidRepId_whenFindIOJAssessorDetailsIsInvokedWithNullUserCreatedEntity_thenIOJAssessorDetailsAreReturnedWithoutFullName() {
        RepOrderEntity repOrder = TestEntityDataBuilder.getPopulatedRepOrder(TestModelDataBuilder.REP_ID);
        repOrder.setUserCreated("grea-k");
        repOrder.setUserCreatedEntity(null);

        when(repOrderRepository.findById(TestModelDataBuilder.REP_ID))
                .thenReturn(Optional.of(repOrder));

        IOJAssessorDetails actualIOJAssessorDetails = repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID);

        assertAll("verify actual IOJAssessorDetails",
                () -> assertEquals(StringUtils.EMPTY, actualIOJAssessorDetails.getFullName()),
                () -> assertEquals("grea-k", actualIOJAssessorDetails.getUserName()));
    }

    @Test
    void givenUnknownRepId_whenFindIOJAssessorDetailsIsInvoked_thenRequestedObjectNotFoundExceptionIsThrown() {
        when(repOrderRepository.findById(1245))
                .thenThrow(new RequestedObjectNotFoundException("Unable to find IOJAssessorDetails for repId: [1245]"));

        RequestedObjectNotFoundException expectedException = assertThrows(RequestedObjectNotFoundException.class,
                () -> repOrderService.findIOJAssessorDetails(1245));

        assertEquals("Unable to find IOJAssessorDetails for repId: [1245]", expectedException.getMessage());
    }
}