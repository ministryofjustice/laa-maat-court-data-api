package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.dto.AssessorDetails;
import gov.uk.courtdata.reporder.impl.RepOrderImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import gov.uk.courtdata.reporder.testutils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RepOrderServiceTest {

    @Mock
    private RepOrderImpl repOrderImpl;

    @Autowired
    private RepOrderMapper repOrderMapper;

    private RepOrderService repOrderService;

    @BeforeEach
    public void setup(){
        repOrderService = new RepOrderService(repOrderImpl, repOrderMapper);
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
    void givenValidRepId_whenFindAssessorDetailsIsInvoked_thenAssessorDetailsAreReturned() {
        when(repOrderImpl.findRepOrderCreator(TestModelDataBuilder.REP_ID))
                .thenReturn(TestDataBuilder.getRepOrderCreatorDetails());

        AssessorDetails actualAssessorDetails = repOrderService.findAssessorDetails(TestModelDataBuilder.REP_ID);

        assertAll("verifyActualAssessorDetails",
                () -> assertEquals(TestDataBuilder.REP_ORDER_CREATOR_NAME, actualAssessorDetails.getName()),
                () -> assertEquals(TestDataBuilder.REP_ORDER_CREATOR_USER_NAME, actualAssessorDetails.getUserName()));
    }
}