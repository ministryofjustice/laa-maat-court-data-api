package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.laaStatus.builder.RepOrderUpdateMessageBuilder;
import gov.uk.courtdata.laaStatus.controller.LaaStatusCDAController;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.laastatus.RootData;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaaStatusPostCDAService {


    private final LaaStatusCDAController laaStatusCDAController;

    private final RepOrderUpdateMessageBuilder repOrderUpdateMessageBuilder;

    private final SolicitorMAATDataRepository solicitorMAATDataRepository;


    public void process(final CaseDetails caseDetails) {

        RootData repOrderData = repOrderUpdateMessageBuilder.build(caseDetails);

        log.debug(repOrderData.toString());
        laaStatusCDAController.updateLaaStatus(repOrderData);

        //  courtDataApiClient.invoke(repOrderData);


    }


}
