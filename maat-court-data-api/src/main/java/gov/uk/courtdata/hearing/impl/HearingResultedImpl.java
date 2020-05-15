package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.processor.HearingWQProcessor;
import gov.uk.courtdata.hearing.mapper.HearingDTOMapper;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.processor.OffenceCodeRefDataProcessor;
import gov.uk.courtdata.processor.ResultCodeRefDataProcessor;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedImpl {


    private final HearingDTOMapper magsCourtDTOMapper;
    private final IdentifierRepository identifierRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final HearingWQProcessor hearingWQProcessor;
    private final ResultCodeRefDataProcessor resultCodeRefDataProcessor;
    private final OffenceCodeRefDataProcessor offenceCodeRefDataProcessor;


    /**
     * @param hearingResulted
     */
    public void execute(final HearingResulted hearingResulted) {

        List<WqLinkRegisterEntity> wqLinkRegisterEntities =
                wqLinkRegisterRepository.findBymaatId(hearingResulted.getMaatId());

        WqLinkRegisterEntity wqLinkReg = wqLinkRegisterEntities.iterator().next();

        log.info("Begin Magistrate Courts Notification...");
        hearingResulted.getDefendant().getOffences()
                .forEach(offence -> {

                    offenceCodeRefDataProcessor.processOffenceCode(offence.getOffenceCode());

                    offence.getResults().forEach(result -> {

                        HearingDTO magsCourtDto =
                                magsCourtDTOMapper.toHearingDTO(hearingResulted,
                                        wqLinkReg.getCaseId(), wqLinkReg.getProceedingId(),
                                        getNextTxId(), offence, result);

                        log.debug("Hearing resulted mapped to Mags Court DTO: {0}", magsCourtDto.toString());

                        final Integer resultCode = magsCourtDto.getResult().getResultCode();
                        resultCodeRefDataProcessor.processResultCode(resultCode);

                        log.info("Start process offence code {0} and result code {1}", offence.getOffenceCode(), resultCode);
                        hearingWQProcessor.process(magsCourtDto);
                        log.info("Completed  offence code {0} and result code {1}", offence.getOffenceCode(), resultCode);

                    });
                });
        log.info("Finished Magistrate Courts Notification processing.");

    }


    /**
     * Get next transaction id in sequence.
     *
     * @return
     */
    private Integer getNextTxId() {
        return identifierRepository.getTxnID();
    }
}
