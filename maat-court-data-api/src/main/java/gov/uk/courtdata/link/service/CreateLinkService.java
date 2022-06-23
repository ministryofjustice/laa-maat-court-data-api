package gov.uk.courtdata.link.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.link.impl.SaveAndLinkImpl;
import gov.uk.courtdata.link.validator.ValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.processor.OffenceCodeRefDataProcessor;
import gov.uk.courtdata.processor.ResultCodeRefDataProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <code>CreateLinkService</code> front handler for save and link with transaction boundary.
 */
@Slf4j
@Service
@XRayEnabled
@AllArgsConstructor
public class CreateLinkService {

    private final SaveAndLinkImpl saveAndLinkImpl;

    private final ValidationProcessor validationProcessor;
    private final OffenceCodeRefDataProcessor offenceCodeRefDataProcessor;
    private final ResultCodeRefDataProcessor resultCodeRefDataProcessor;

    /**
     * @param caseDetails
     * @throws ValidationException
     * @throws MAATCourtDataException
     */
    public void saveAndLink(final CaseDetails caseDetails) {

        final CourtDataDTO courtDataDTO = validationProcessor.validate(caseDetails);
        log.info("Validation completed!!!");
        processStaticRefData(courtDataDTO);
        saveAndLinkImpl.execute(courtDataDTO);
    }


    private void processStaticRefData(CourtDataDTO courtDataDTO) {
        courtDataDTO.getCaseDetails().getDefendant().getOffences()
                .forEach(offence -> {
                    offenceCodeRefDataProcessor.processOffenceCode(offence.getOffenceCode());
                    if (offence.getResults() != null && !offence.getResults().isEmpty()) {
                        offence.getResults()
                                .forEach(result ->
                                        resultCodeRefDataProcessor.processResultCode(Integer.parseInt(result.getResultCode())));
                    }
                });
    }

}
