package gov.uk.courtdata.iojappeal.service;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_IOJ_APPEAL_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;

@ExtendWith(MockitoExtension.class)
class IOJAppealV2ServiceTest {

    @InjectMocks
    private IOJAppealV2Service iojAppealService;

    @Mock
    private IOJAppealPersistenceService iojAppealPersistenceService;

    @Mock
    private IOJAppealMapper iojAppealMapper;

    @Test
    void whenFindByLegacyIdIsInvoked_thenIOJAppealIsRetrieved() {
        var apiGetIojAppealResponse = TestModelDataBuilder.getApiGetIojAppealResponse();
        when(iojAppealPersistenceService.find(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());
        when(iojAppealMapper.toApiGetIojAppealResponse(any(IOJAppealEntity.class))).thenReturn(apiGetIojAppealResponse);
        var returnedIOJAppeal = iojAppealService.find(LEGACY_IOJ_APPEAL_ID);
        assertEquals(LEGACY_IOJ_APPEAL_ID, returnedIOJAppeal.getLegacyAppealId());
    }

    @Test
    void whenFindByLegacyIdIsInvokedWithInvalidId_thenNotFoundExceptionIsThrown() {
        when(iojAppealPersistenceService.find(LEGACY_IOJ_APPEAL_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
            .isThrownBy(() -> iojAppealService.find(LEGACY_IOJ_APPEAL_ID))
            .withMessageContaining(String.format("No IoJ Appeal found for ID: %d", LEGACY_IOJ_APPEAL_ID));
    }

    @Test
    void whenCreateIsInvoked_thenApiCreateIojAppealResponseIsCreated() {
        var createdApiCreateIojResponse = TestModelDataBuilder.getApiCreateIojAppealResponse();
        var apiCreateIojRequest = TestModelDataBuilder.getApiCreateIojAppealRequest();
        var createdIOJAppealEntity = TestEntityDataBuilder.getIOJAppealEntity();

        when(iojAppealMapper.toApiCreateIojAppealResponse(any(IOJAppealEntity.class))).thenReturn(createdApiCreateIojResponse);
        when(iojAppealMapper.toIojAppealEntity(any(ApiCreateIojAppealRequest.class))).thenReturn(createdIOJAppealEntity);

        var apiCreateIojResponse = iojAppealService.create(apiCreateIojRequest);

        assertEquals(LEGACY_IOJ_APPEAL_ID, apiCreateIojResponse.getLegacyAppealId());

        verify(iojAppealPersistenceService).setOldIOJAppealsReplaced(IOJ_REP_ID, IOJ_APPEAL_ID);
    }
}