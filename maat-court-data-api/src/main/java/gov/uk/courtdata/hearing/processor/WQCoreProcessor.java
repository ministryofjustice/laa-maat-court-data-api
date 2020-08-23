package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.LEADING_ZERO_3;
import static gov.uk.courtdata.constants.CourtDataConstants.MAGS_PROCESSING_SYSTEM_USER;
import static gov.uk.courtdata.enums.WQStatus.WAITING;

@Component
@RequiredArgsConstructor
public class WQCoreProcessor {

    private final WqCoreRepository wqCoreRepository;
    private final XLATResultRepository xlatResultRepository;
    private final OffenceRepository offenceRepository;


    public void process(final HearingDTO magsCourtDTO) {


        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(magsCourtDTO.getTxId())
                .caseId(magsCourtDTO.getCaseId())
                .createdTime(LocalDate.now())
                .createdUserId(MAGS_PROCESSING_SYSTEM_USER)
                .extendedProcessing(processIfNewOffence(magsCourtDTO))
                .wqType(findWQType(magsCourtDTO.getResult().getResultCode()))
                .wqStatus(WAITING.value())
                .maatUpdateStatus(2) // No pre-processing status required here so set to ready for processing.
                .build();
        wqCoreRepository.save(wqCoreEntity);

    }

    /**
     * Check if there is an offence for the case and seq no, if not then a new offence
     * has to be created before processing result. This requires WQ core extended processing
     * with '0'. This be picked up by in bound processing procedure to act accordingly.
     *
     * @param magsCourtDTO
     * @return
     */
    private int processIfNewOffence(final HearingDTO magsCourtDTO) {

        Integer offenceCount =
                offenceRepository.getOffenceCountForAsnSeq(
                        magsCourtDTO.getCaseId(),
                        String.format(LEADING_ZERO_3, Integer.parseInt(magsCourtDTO.getOffence().getAsnSeq())));

        return offenceCount == 0 ? 0 : 99;
    }

    /**
     * If the result code is available in xlat_result return the relevant WQ type.
     *
     * @param resultCode
     * @return
     */
    public int findWQType(final Integer resultCode) {

        Optional<XLATResultEntity> xlatResult =
                xlatResultRepository.findById(resultCode);
        XLATResultEntity xlatResultEntity = xlatResult.orElse(null);

        assert xlatResultEntity != null;
        return xlatResultEntity.getWqType();


    }


}
