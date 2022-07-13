package gov.uk.courtdata.laastatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.laastatus.builder.CourtDataDTOBuilder;
import gov.uk.courtdata.model.CaseDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaaStatusServiceUpdateTest {

    @InjectMocks
    private LaaStatusServiceUpdate laaStatusServiceUpdate;

    @Mock
    private LaaStatusService laaStatusService;

    @Mock
    private LaaStatusPostCDAService laaStatusPostCDAService;

    @Mock
    private CourtDataDTOBuilder courtDataDTOBuilder;

    @Mock
    private Gson gson;

    @Test
    public void givenACaseDetail_whenRestCallReceived_thenUpdateMlaAndCDA() {

        //given
        CaseDetails caseDetails = CaseDetails.builder()
                .laaTransactionId(UUID.randomUUID())
                .onlyForCDAService(true)
                .maatId(12121)
                .build();

        when(courtDataDTOBuilder.build(caseDetails))
                .thenReturn(
                        CourtDataDTO.builder()
                                .caseId(111)
                                .libraId("2222")
                                .caseDetails(CaseDetails.builder().onlyForCDAService(false).build())
                                .build()
                );
        laaStatusServiceUpdate.updateMlaAndCDA(caseDetails);

        //then
        verify(laaStatusPostCDAService, times(1)).process(any());
        verify(laaStatusService, times(1)).execute(any());
    }

    @Test
    public void givenACaseDetail_whenRestCallReceived_thenUpdateCDA() {

        //given
        CaseDetails caseDetails = CaseDetails.builder()
                .laaTransactionId(UUID.randomUUID())
                .onlyForCDAService(true)
                .maatId(12121)
                .build();

        when(courtDataDTOBuilder.build(caseDetails))
                .thenReturn(
                        CourtDataDTO.builder()
                                .caseId(111)
                                .libraId("2222")
                                .proceedingId(111)
                                .defendantMAATDataEntity(DefendantMAATDataEntity.builder().build())
                                .caseDetails(CaseDetails.builder().onlyForCDAService(true).build())
                                .build()
                );

        laaStatusServiceUpdate.updateMlaAndCDA(caseDetails);

        //then
        verify(laaStatusPostCDAService, times(1)).process(any());
        verify(laaStatusService, times(0)).execute(any());
    }
}