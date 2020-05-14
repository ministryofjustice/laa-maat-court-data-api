package gov.uk.courtdata.link.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.link.impl.SaveAndLinkImpl;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.link.validator.ValidationProcessor;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.processor.OffenceCodesProcessor;
import gov.uk.courtdata.processor.ResultCodesProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <code>CreateLinkService</code> front handler for save and link with transaction boundary.
 */
@Slf4j
@AllArgsConstructor
@Service
public class CreateLinkService {

    private final SaveAndLinkImpl saveAndLinkImpl;

    private final ValidationProcessor validationProcessor;
    private final OffenceCodesProcessor offenceCodesProcessor;
    private final ResultCodesProcessor resultCodesProcessor;

    /**
     * @param linkMessage
     * @throws ValidationException
     * @throws MaatCourtDataException
     */
    public void saveAndLink(final CaseDetails linkMessage) {

        final CourtDataDTO courtDataDTO = validationProcessor.validate(linkMessage);
        log.info("Validation completed!!!");
        processStaticRefData(courtDataDTO);
        saveAndLinkImpl.execute(courtDataDTO);
    }


    private void processStaticRefData(CourtDataDTO courtDataDTO) {
        courtDataDTO.getCaseDetails().getDefendant().getOffences()
                .forEach(offence -> {
                    offenceCodesProcessor.processOffenceCode(offence.getOffenceCode());
                    if (offence.getResults() != null && !offence.getResults().isEmpty()) {
                        offence.getResults()
                                .forEach(result ->
                                        resultCodesProcessor.processResultCode(Integer.parseInt(result.getResultCode())));
                    }
                });
    }

}
