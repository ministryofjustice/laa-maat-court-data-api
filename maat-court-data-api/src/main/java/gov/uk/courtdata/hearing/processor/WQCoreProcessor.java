package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.XLATResult;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static gov.uk.courtdata.constants.CourtDataConstants.MAGS_PROCESSING_SYSTEM_USER;
import static gov.uk.courtdata.enums.WQStatus.WAITING;

@Component
@RequiredArgsConstructor
public class WQCoreProcessor {

    private static final String AUTO_USER = "AUTO";
    private static final String RESULT_CODE_DESCRIPTION = "Automatically added result";
    private final WqCoreRepository wqCoreRepository;
    private final XLATResultRepository xlatResultRepository;
    private final OffenceRepository offenceRepository;


    public void process(final MagistrateCourtDTO magsCourtDTO) {


        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(magsCourtDTO.getTxId())
                .caseId(magsCourtDTO.getCaseId())
                .createdTime(LocalDate.now())
                .createdUserId(MAGS_PROCESSING_SYSTEM_USER)
                .extendedProcessing(processIfNewOffence(magsCourtDTO))
                .wqType(findWQType(magsCourtDTO))
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
    private int processIfNewOffence(final MagistrateCourtDTO magsCourtDTO) {

        Integer offenceCount =
                offenceRepository.getOffenceCountForAsnSeq(magsCourtDTO.getCaseId(), magsCourtDTO.getOffence().getAsnSeq());

        return offenceCount == 0 ? 0 : 99;
    }

    /**
     * If the result code is available in xlat_result return the relevant WQ type
     * else create a new xlat result with intervention queue type.
     *
     * @param magsCourtDTO
     * @return
     */
    private int findWQType(final MagistrateCourtDTO magsCourtDTO) {

        Optional<XLATResult> xlatResult =
                xlatResultRepository.findById(magsCourtDTO.getResult().getResultCode());

        return xlatResult.isPresent() ? xlatResult.get().getWqType() : createXLATResult(magsCourtDTO).getWqType();


    }

    /**
     * Create new xlat result.
     *
     * @param magsCourtDTO
     * @return
     */
    private XLATResult createXLATResult(final MagistrateCourtDTO magsCourtDTO) {

        XLATResult xlatResult = XLATResult.builder()
                .cjsResultCode(magsCourtDTO.getResult().getResultCode())
                .resultDescription(RESULT_CODE_DESCRIPTION)
                .resultType(null)
                .englandAndWales("Y")
                .flag(null)
                .notes(buildNotesContent(magsCourtDTO.getResult().getResultCode()))
                .wqType(WQType.USER_INTERVENTIONS_QUEUE.value())
                .createdUser(AUTO_USER)
                .createdDate(LocalDate.now()).build();

        return xlatResultRepository.save(xlatResult);


    }

    /**
     * Build notes description for the result.
     *
     * @param resultCode
     * @return
     */
    private String buildNotesContent(Integer resultCode) {
        return String.format("New Result code %s  has been received " +
                "and automatically added to the Intervention queue. Please contact support.'", resultCode);
    }
}
