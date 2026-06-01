package gov.uk.courtdata.reporder.service;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.model.reporder.LinkingDetail;
import gov.uk.courtdata.model.reporder.MaatSearchRequest;
import gov.uk.courtdata.model.reporder.MaatSearchResponse;
import gov.uk.courtdata.reporder.impl.RepOrderImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepOrderServiceTest {

    @Mock
    private RepOrderImpl repOrderImpl;

    @Mock
    private RepOrderRepository repOrderRepository;

    @Mock
    private RepOrderMapper repOrderMapper;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    private RepOrderService repOrderService;

    @BeforeEach
    void setup() {
        repOrderService = new RepOrderService(
                repOrderImpl, repOrderMapper, repOrderRepository, wqLinkRegisterRepository, repOrderCPDataRepository);
    }

    @Test
    void givenValidRepIdAndSentenceOrderFlagIsFalse_whenFindIsInvoked_thenRepOrderIsReturned() {
        when(repOrderImpl.find(anyInt())).thenReturn(TestEntityDataBuilder.getRepOrder());

        repOrderService.find(TestModelDataBuilder.REP_ID, false);
        verify(repOrderImpl).find(anyInt());
    }

    @Test
    void givenExistingRepOrder_whenUpdateIsInvoked_thenEntityIsUpdatedAndFlushed() {
        Integer repId = 123;
        LocalDateTime dateModified = LocalDateTime.now();
        Map<String, Object> updates = Map.of("dateModified", dateModified, "iojResult", "PASS");

        RepOrderEntity existingRepOrder = new RepOrderEntity();
        existingRepOrder.setId(repId);

        RepOrderEntity savedRepOrder = new RepOrderEntity();
        savedRepOrder.setId(repId);

        RepOrderDTO expectedResponse = new RepOrderDTO();
        expectedResponse.setId(repId);

        when(repOrderRepository.findById(repId)).thenReturn(Optional.of(existingRepOrder));
        when(repOrderRepository.saveAndFlush(any(RepOrderEntity.class))).thenReturn(savedRepOrder);
        when(repOrderMapper.repOrderEntityToRepOrderDTO(any(RepOrderEntity.class)))
                .thenReturn(expectedResponse);

        ArgumentCaptor<RepOrderEntity> captor = ArgumentCaptor.forClass(RepOrderEntity.class);
        RepOrderDTO result = repOrderService.update(repId, updates);

        assertThat(result).isEqualTo(expectedResponse);

        verify(repOrderRepository).findById(repId);
        verify(repOrderRepository).saveAndFlush(captor.capture());
        verify(repOrderMapper).repOrderEntityToRepOrderDTO(savedRepOrder);
        verify(repOrderRepository, never()).save(any());

        RepOrderEntity capturedRepOrder = captor.getValue();

        assertThat(capturedRepOrder).isSameAs(existingRepOrder);
        assertThat(capturedRepOrder.getIojResult()).isEqualTo("PASS");
        assertThat(capturedRepOrder.getDateModified()).isEqualTo(dateModified);
    }

    @Test
    void givenRepOrderDoesNotExist_whenUpdateIsInvoked_thenRequestedObjectNotFoundExceptionIsThrown() {
        Integer repId = 123;
        Map<String, Object> updates = Map.of("dateModified", LocalDateTime.now());

        when(repOrderRepository.findById(repId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repOrderService.update(repId, updates))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessage("Rep Order not found for id 123");

        verifyNoInteractions(repOrderMapper);
        verify(repOrderRepository).findById(repId);
        verify(repOrderRepository, never()).saveAndFlush(any());
    }

    @Test
    void givenValidRepIdAndSentenceOrderFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() {
        when(repOrderImpl.findWithSentenceOrderDate(anyInt())).thenReturn(TestEntityDataBuilder.getRepOrder());

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
    void givenAllInputs_whenFdcFastTrackingIsInvoked_thenReturnList() {
        Set<Integer> idList = Set.of(5, 6);
        when(repOrderImpl.findEligibleForFdcFastTracking(anyInt(), any(), anyInt()))
                .thenReturn(idList);
        assertThat(repOrderService.findEligibleForFdcFastTracking(5, LocalDate.now(), 5))
                .isEqualTo(idList);
    }

    @Test
    void givenAllInputs_whenFdcDelayedPickupIsInvoked_thenReturnList() {
        Set<Integer> idList = Set.of(5, 6);
        when(repOrderImpl.findEligibleForFdcDelayedPickup(anyInt(), any(), anyInt()))
                .thenReturn(idList);
        assertThat(repOrderService.findEligibleForFdcDelayedPickup(5, LocalDate.now(), 5))
                .isEqualTo(idList);
    }

    @Test
    void givenAValidRepIdAndNoSentenceOrderDate_whenExistsIsInvoked_thenReturnFalse() {
        when(repOrderImpl.exists(any())).thenReturn(false);
        assertThat(repOrderService.exists(TestModelDataBuilder.REP_ID)).isFalse();
    }

    @Test
    void givenValidRepId_whenFindIOJAssessorDetailsIsInvoked_thenIOJAssessorDetailsAreReturned() {
        RepOrderEntity repOrder = TestEntityDataBuilder.getPopulatedRepOrder(TestModelDataBuilder.REP_ID);
        AssessorDetails expectedDetails = AssessorDetails.builder()
                .fullName("Karen Greaves")
                .userName("grea-k")
                .build();

        when(repOrderRepository.findById(TestModelDataBuilder.REP_ID)).thenReturn(Optional.of(repOrder));
        when(repOrderMapper.createIOJAssessorDetails(any(RepOrderEntity.class))).thenReturn(expectedDetails);

        AssessorDetails actualAssessorDetails = repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID);

        assertThat(actualAssessorDetails).isEqualTo(expectedDetails);

        verify(repOrderMapper).createIOJAssessorDetails(repOrder);
        verify(repOrderRepository).findById(TestModelDataBuilder.REP_ID);
    }

    @Test
    void givenRepOrderDoesNotExist_whenFindIOJAssessorDetailsIsInvoked_thenRequestedObjectNotFoundExceptionIsThrown() {
        when(repOrderRepository.findById(TestModelDataBuilder.REP_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining(
                        String.format("Unable to find AssessorDetails for repId: [%s]", TestModelDataBuilder.REP_ID));

        verifyNoInteractions(repOrderMapper);
        verify(repOrderRepository).findById(TestModelDataBuilder.REP_ID);
    }

    @Test
    void givenAInvalidRequest_whenSearchMaatApplicationIsInvoked_thenRequestedObjectNotFoundExceptionIsThrown() {
        var request = TestModelDataBuilder.getMaatSearchRequest();
        when(repOrderRepository.findRepId(any(MaatSearchRequest.class))).thenReturn(Collections.emptySet());
        assertThatThrownBy(() -> repOrderService.searchMaatApplication(request))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessage("Representation order not found");
    }

    @Test
    void
            givenAInvalidRequestAndMissingRepOrder_whenSearchMaatApplicationIsInvoked_thenRequestedObjectNotFoundExceptionIsThrown() {
        var request = TestModelDataBuilder.getMaatSearchRequest();
        when(repOrderRepository.findRepId(any(MaatSearchRequest.class))).thenReturn(null);
        assertThatThrownBy(() -> repOrderService.searchMaatApplication(request))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessage("Representation order not found");
    }

    @Test
    void givenAValidRequest_whenSearchMaatApplicationIsInvoked_thenReturnCorrectResponse() {

        when(repOrderRepository.findRepId(any(MaatSearchRequest.class)))
                .thenReturn(Set.of(TestModelDataBuilder.REP_ID));
        List<WqLinkRegisterEntity> linkList = List.of(TestEntityDataBuilder.getWQLinkRegisterEntity(1234));
        when(wqLinkRegisterRepository.findBymaatId(anyInt())).thenReturn(linkList);
        repOrderService.searchMaatApplication(TestModelDataBuilder.getMaatSearchRequest());
        verify(repOrderRepository).findRepId(any(MaatSearchRequest.class));
        verify(wqLinkRegisterRepository).findBymaatId(anyInt());
    }

    @Test
    void givenInvalidRequestAndFoundMultipleRepOrder_whenSearchMaatApplicationIsInvoked_thenReturnCorrectResponse() {

        when(repOrderRepository.findRepId(any(MaatSearchRequest.class)))
                .thenReturn(Set.of(TestModelDataBuilder.REP_ID, TestModelDataBuilder.REP_ID + 1));
        List<WqLinkRegisterEntity> linkList = List.of(TestEntityDataBuilder.getWQLinkRegisterEntity(1234));
        when(wqLinkRegisterRepository.findBymaatId(anyInt())).thenReturn(linkList);
        List<MaatSearchResponse> maatSearchResponseList =
                repOrderService.searchMaatApplication(TestModelDataBuilder.getMaatSearchRequest());
        verify(repOrderRepository).findRepId(any(MaatSearchRequest.class));
        verify(wqLinkRegisterRepository, times(2)).findBymaatId(anyInt());
        assertThat(maatSearchResponseList).hasSize(2);
    }

    @Test
    void givenAValidRequest_whenMaatIdReturned_thenResponseContainsCaseUrn() {
        WqLinkRegisterEntity linkWithoutCaseUrn = TestEntityDataBuilder.getWQLinkRegisterEntityWithoutCaseUrn();
        RepOrderCPDataEntity repOrder = TestEntityDataBuilder.getRepOrderEntity(REP_ID);

        when(repOrderRepository.findRepId(any(MaatSearchRequest.class))).thenReturn(Set.of(REP_ID));
        when(wqLinkRegisterRepository.findBymaatId(REP_ID)).thenReturn(List.of(linkWithoutCaseUrn));
        when(repOrderCPDataRepository.findByrepOrderId(REP_ID)).thenReturn(Optional.of(repOrder));
        when(repOrderMapper.mapMaatSearchResponse(REP_ID, List.of(linkWithoutCaseUrn), repOrder.getCaseUrn()))
                .thenReturn(MaatSearchResponse.builder()
                        .linkingDetail(LinkingDetail.builder()
                                .caseUrn(repOrder.getCaseUrn())
                                .build())
                        .build());

        List<MaatSearchResponse> maatSearchResponseList =
                repOrderService.searchMaatApplication(TestModelDataBuilder.getMaatSearchRequest());

        assertThat(maatSearchResponseList).hasSize(1);
        MaatSearchResponse response = maatSearchResponseList.getFirst();
        assertThat(response.getLinkingDetail().getCaseUrn()).isEqualTo(repOrder.getCaseUrn());

        verify(repOrderRepository).findRepId(any(MaatSearchRequest.class));
        verify(wqLinkRegisterRepository).findBymaatId(REP_ID);
        verify(repOrderCPDataRepository).findByrepOrderId(REP_ID);
        verify(repOrderMapper).mapMaatSearchResponse(REP_ID, List.of(linkWithoutCaseUrn), repOrder.getCaseUrn());
    }

    @Test
    void givenRepOrderDoesNotExist_whenSearchMaatApplicationIsInvoked_thenRequestedObjectNotFoundExceptionIsThrown() {
        when(repOrderRepository.findRepId(any(MaatSearchRequest.class))).thenReturn(Set.of());

        MaatSearchRequest searchRequest = MaatSearchRequest.builder().build();
        assertThatThrownBy(() -> repOrderService.searchMaatApplication(searchRequest))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessage("Representation order not found");

        verify(repOrderRepository).findRepId(any(MaatSearchRequest.class));
        verifyNoInteractions(wqLinkRegisterRepository);
        verifyNoInteractions(repOrderCPDataRepository);
        verifyNoInteractions(repOrderMapper);
    }
}
