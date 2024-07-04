package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.impl.RepOrderImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
class RepOrderServiceTest {

    @Mock
    private RepOrderImpl repOrderImpl;

    @Mock
    private RepOrderRepository repOrderRepository;

    private RepOrderMapper repOrderMapper;
    private RepOrderService repOrderService;

    @BeforeEach
    public void setup() {
        repOrderMapper = Mappers.getMapper(RepOrderMapper.class);
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
    void givenAllInputs_whenFdcFastTrackingIsInvoked_thenReturnList() {
        Set<Integer> idList = Set.of(5,6);
        when(repOrderImpl.findEligibleForFdcFastTracking(anyInt(), any(), anyInt()))
                .thenReturn(idList);
        assertThat(repOrderService.findEligibleForFdcFastTracking(5, LocalDate.now(), 5)).isEqualTo(idList);
    }
    @Test
    void givenAllInputs_whenFdcDelayedPickupIsInvoked_thenReturnList() {
        Set<Integer> idList = Set.of(5,6);
        when(repOrderImpl.findEligibleForFdcDelayedPickup(anyInt(), any(), anyInt()))
                .thenReturn(idList);
        assertThat(repOrderService.findEligibleForFdcDelayedPickup(5, LocalDate.now(), 5)).isEqualTo(idList);
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
        repOrder.setUserCreatedEntity(TestEntityDataBuilder.getUserEntity());

        when(repOrderRepository.findById(TestModelDataBuilder.REP_ID))
                .thenReturn(Optional.of(repOrder));

        AssessorDetails actualIOJAssessorDetails = repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID);

        assertAll("verify actual AssessorDetails",
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

        AssessorDetails actualIOJAssessorDetails = repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID);

        assertAll("verify actual AssessorDetails",
                () -> assertEquals(StringUtils.EMPTY, actualIOJAssessorDetails.getFullName()),
                () -> assertEquals("grea-k", actualIOJAssessorDetails.getUserName()));
    }

    @Test
    void givenUnknownRepId_whenFindIOJAssessorDetailsIsInvoked_thenRequestedObjectNotFoundExceptionIsThrown() {
        when(repOrderRepository.findById(1245))
                .thenThrow(new RequestedObjectNotFoundException("Unable to find AssessorDetails for repId: [1245]"));

        RequestedObjectNotFoundException expectedException = assertThrows(RequestedObjectNotFoundException.class,
                () -> repOrderService.findIOJAssessorDetails(1245));

        assertEquals("Unable to find AssessorDetails for repId: [1245]", expectedException.getMessage());
    }

    @Test
    void givenAValidInput_whenUpdateIsInvoked_thenUpdateIsSuccess() {
        RepOrderEntity repOrder = TestEntityDataBuilder.getPopulatedRepOrder(TestModelDataBuilder.REP_ID);
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrder));
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("iojResult", "PASS");
        repOrderService.update(TestModelDataBuilder.REP_ID, inputMap);
        verify(repOrderRepository, atLeastOnce()).findById(any());
        verify(repOrderRepository, atLeastOnce()).save(any());
    }
}