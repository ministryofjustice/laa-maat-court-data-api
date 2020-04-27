package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.XLATResult;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.link.processor.Process;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static gov.uk.courtdata.enums.WQStatus.WAITING;
import static gov.uk.courtdata.enums.WQType.PRE_STEERING;

@Component
@RequiredArgsConstructor
public class WQCoreProcessor {

    private final WqCoreRepository wqCoreRepository;

    private final XLATResultRepository xlatResultRepository;

    public void process(MagistrateCourtDTO magsCourtDTO) {

        //TODO: if no xlat create new.
        Optional<XLATResult> xlatResult =
                xlatResultRepository.findById(Integer.parseInt(magsCourtDTO.getResult().getResultCode()));

        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(magsCourtDTO.getTxId())
                .caseId(magsCourtDTO.getCaseId())
                .createdTime(LocalDate.now())
                .createdUserId(magsCourtDTO.getCreatedUser())
                .extendedProcessing(1) // TODO: count of case id & offence & asn seq.
                .wqType(xlatResult.get().getWqType())
                .wqStatus(WAITING.value())
                .maatUpdateStatus(2)
                .build();
        wqCoreRepository.save(wqCoreEntity);

    }


}
