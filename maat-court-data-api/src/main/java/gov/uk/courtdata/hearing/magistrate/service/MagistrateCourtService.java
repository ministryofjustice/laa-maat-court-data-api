package gov.uk.courtdata.hearing.magistrate.service;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.entity.XLATResult;
import gov.uk.courtdata.enums.WQType;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.hearing.magistrate.dto.ResultDTO;
import gov.uk.courtdata.hearing.magistrate.impl.MagistrateCourtImpl;
import gov.uk.courtdata.hearing.magistrate.mapper.MagistrateCourtDTOMapper;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MagistrateCourtService {

    private static final String AUTO_USER = "AUTO";
    private static final String RESULT_CODE_DESCRIPTION = "Automatically added result";
    private final MagistrateCourtDTOMapper magsCourtDTOMapper;
    private final IdentifierRepository identifierRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final MagistrateCourtImpl magistrateCourtImpl;
    private final XLATResultRepository xlatResultRepository;

    /**
     * @param hearingResulted
     */
    public void execute(final HearingResulted hearingResulted) {

        List<WqLinkRegisterEntity> wqLinkRegisterEntities =
                wqLinkRegisterRepository.findBymaatId(hearingResulted.getMaatId());

        WqLinkRegisterEntity wqLinkReg = wqLinkRegisterEntities.iterator().next();


        hearingResulted.getDefendant().getOffences()
                .forEach(offence -> {
                    offence.getResults().forEach(result -> {

                        MagistrateCourtDTO magsCourtDto =
                                magsCourtDTOMapper.toMagsCourtDTO(hearingResulted,
                                        wqLinkReg.getCaseId(), wqLinkReg.getProceedingId(),
                                        getNextTxId(), offence, result);

                        processResultCode(magsCourtDto.getResult());

                        magistrateCourtImpl.execute(magsCourtDto);

                        log.debug(magsCourtDto.toString());
                    });
                });

    }


    /**
     * If the result code is available in xlat_result return the relevant WQ type
     * else create a new xlat result with intervention queue type.
     *
     * @param resultDTO
     * @return
     */
    private void processResultCode(final ResultDTO resultDTO) {

        Optional<XLATResult> xlatResult =
                xlatResultRepository.findById(resultDTO.getResultCode());

        if (!xlatResult.isPresent())
            createNewXLATResult(resultDTO);


    }

    /**
     * Create new xlat result.
     *
     * @param magsCourtDTO
     * @return
     */
    private XLATResult createNewXLATResult(final ResultDTO magsCourtDTO) {

        XLATResult xlatResult = XLATResult.builder()
                .cjsResultCode(magsCourtDTO.getResultCode())
                .resultDescription(RESULT_CODE_DESCRIPTION)
                .resultType(null)
                .englandAndWales("Y")
                .flag(null)
                .notes(buildNotesContent(magsCourtDTO.getResultCode()))
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

    /**
     * Get next transaction id in sequence.
     *
     * @return
     */
    private Integer getNextTxId() {
        return identifierRepository.getTxnID();
    }
}
