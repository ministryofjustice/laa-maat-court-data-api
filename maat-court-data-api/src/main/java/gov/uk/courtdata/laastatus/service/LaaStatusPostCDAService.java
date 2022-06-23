package gov.uk.courtdata.laastatus.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laastatus.builder.RepOrderUpdateMessageBuilder;
import gov.uk.courtdata.laastatus.client.CourtDataAdapterClient;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@XRayEnabled
@RequiredArgsConstructor
public class LaaStatusPostCDAService {

    private final RepOrderUpdateMessageBuilder repOrderUpdateMessageBuilder;

    private final CourtDataAdapterClient courtDataAdapterClient;

    public void process(final CourtDataDTO courtDataDTO) {

        LaaStatusUpdate repOrderData =
                repOrderUpdateMessageBuilder.build(courtDataDTO.getCaseDetails());
        Map<String,String> headers = repOrderUpdateMessageBuilder.buildHeaders(courtDataDTO);

        courtDataAdapterClient.postLaaStatus(repOrderData,headers);
    }
}
