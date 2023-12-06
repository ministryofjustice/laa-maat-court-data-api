package gov.uk.courtdata.laastatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laastatus.builder.RepOrderUpdateMessageBuilder;
import gov.uk.courtdata.courtdataadapter.client.CourtDataAdapterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class LaaStatusPostCDAServiceTest {

    @InjectMocks
    private LaaStatusPostCDAService laaStatusPostCDAService;

    @Mock
    private RepOrderUpdateMessageBuilder repOrderUpdateMessageBuilder;

    @Mock
    private CourtDataAdapterClient courtDataAdapterClient;

    @Test
    public void process() {
        //given
        CourtDataDTO courtDataDTO = CourtDataDTO.builder()
                .txId(1212)
                .libraId("libraID11")
                .build();

        //when
        laaStatusPostCDAService.process(courtDataDTO);

        //then
        verify(repOrderUpdateMessageBuilder).build(courtDataDTO.getCaseDetails());
        verify(repOrderUpdateMessageBuilder).buildHeaders(courtDataDTO);
        verify(courtDataAdapterClient).postLaaStatus(any(), any());
    }
}