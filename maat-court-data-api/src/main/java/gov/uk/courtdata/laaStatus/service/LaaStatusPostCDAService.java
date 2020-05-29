package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laaStatus.builder.RepOrderUpdateMessageBuilder;
import gov.uk.courtdata.laaStatus.client.CourtDataAdapterClient;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaaStatusPostCDAService {

    private final RepOrderUpdateMessageBuilder repOrderUpdateMessageBuilder;

    private final CourtDataAdapterClient courtDataAdapterClient;

    public void process(final CourtDataDTO courtDataDTO) {

        LaaStatusUpdate repOrderData =
                repOrderUpdateMessageBuilder.build(courtDataDTO.getCaseDetails());

        log.debug(repOrderData.toString());
        courtDataAdapterClient.postLaaStatus(repOrderData);

    }


}
