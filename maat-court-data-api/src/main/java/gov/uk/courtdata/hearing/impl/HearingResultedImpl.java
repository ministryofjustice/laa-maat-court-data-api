package gov.uk.courtdata.hearing.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.mapper.HearingDTOMapper;
import gov.uk.courtdata.hearing.processor.HearingWQProcessor;
import gov.uk.courtdata.hearing.processor.WQCoreProcessor;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.processor.OffenceCodeRefDataProcessor;
import gov.uk.courtdata.processor.ResultCodeRefDataProcessor;
import gov.uk.courtdata.prosecutionconcluded.helper.OffenceHelper;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class HearingResultedImpl {


    private final HearingDTOMapper hearingDTOMapper;
    private final IdentifierRepository identifierRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final HearingWQProcessor hearingWQProcessor;
    private final ResultCodeRefDataProcessor resultCodeRefDataProcessor;
    private final OffenceCodeRefDataProcessor offenceCodeRefDataProcessor;
    private final WQCoreProcessor wqCoreProcessor;
    private final OffenceHelper offenceHelper;


    /**
     * @param hearingResulted
     */
    public void execute(final HearingResulted hearingResulted) {

        List<WqLinkRegisterEntity> wqLinkRegisterEntities =
                wqLinkRegisterRepository.findBymaatId(hearingResulted.getMaatId());

        WqLinkRegisterEntity wqLinkReg = wqLinkRegisterEntities.iterator().next();

        log.info("Begin Work Queue  Notification...");
        hearingResulted.getDefendant().getOffences()
                .forEach(offence -> {
                    offenceCodeRefDataProcessor.processOffenceCode(offence.getOffenceCode());
                    offence.getResults().forEach(result -> {
                        final Integer resultCode = Integer.parseInt(result.getResultCode());
                        resultCodeRefDataProcessor.processResultCode(resultCode);
                        if (isWorkQueueProcessingRequired(resultCode, hearingResulted, offence, wqLinkReg)) {

                            processResults(hearingResulted, wqLinkReg, offence, result);
                        }
                    });

                });
        log.info("Finished Work Queue Notification processing.");
    }


    private void processResults(HearingResulted hearingResulted, WqLinkRegisterEntity wqLinkReg, Offence offence, Result result) {

        HearingDTO hearingDTO =
                hearingDTOMapper.toHearingDTO(hearingResulted,
                        wqLinkReg.getCaseId(), wqLinkReg.getProceedingId(),
                        getNextTxId(), offence, result);

        log.debug("Hearing resulted mapped to Hearing Court DTO: {}", hearingDTO.toString());

        final Integer resultCode = hearingDTO.getResult().getResultCode();
        log.info("Start process offence code {} and result code {}", offence.getOffenceCode(), resultCode);
        hearingWQProcessor.process(hearingDTO);
        log.info("Completed  offence code {} and result code {}", offence.getOffenceCode(), resultCode);
    }


    /**
     * Get next transaction id in sequence.
     *
     * @return
     */
    private Integer getNextTxId() {
        return identifierRepository.getTxnID();
    }

    private boolean isWorkQueueProcessingRequired(Integer resultCode, HearingResulted hearingResulted,
                                                  Offence offence, WqLinkRegisterEntity wqLink) {
        return JurisdictionType.CROWN != hearingResulted.getJurisdictionType() ||
                !WQType.isActionableQueue(wqCoreProcessor.findWQType(resultCode))
                || isConcludingNewOffence(resultCode, offence, wqLink.getCaseId());
    }

    private boolean isConcludingNewOffence(Integer resultCode, Offence offence,
                                             Integer caseId) {
        boolean myReturn = false;
        if (WQType.CONCLUSION_QUEUE.value() == wqCoreProcessor.findWQType(resultCode)
                && offenceHelper.isNewOffence(caseId, offence.getAsnSeq())) {
            myReturn = true;
        }
        return myReturn;
    }


}