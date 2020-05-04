package gov.uk.courtdata.hearing.magistrate.service;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.hearing.magistrate.impl.MagistrateCourtImpl;
import gov.uk.courtdata.hearing.magistrate.mapper.MagistrateCourtDTOMapper;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MagistrateCourtService {

    private final MagistrateCourtDTOMapper magsCourtDTOMapper;
    private final IdentifierRepository identifierRepository;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final MagistrateCourtImpl magistrateCourtImpl;

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

                        magistrateCourtImpl.execute(magsCourtDto);

                        log.info(magsCourtDto.toString());
                    });
                });

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
