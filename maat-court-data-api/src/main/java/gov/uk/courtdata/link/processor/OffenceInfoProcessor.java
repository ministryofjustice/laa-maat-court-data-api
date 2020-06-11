package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static gov.uk.courtdata.util.DateUtil.parse;

@RequiredArgsConstructor
@Component
@Slf4j
public class OffenceInfoProcessor implements Process {

    private final OffenceRepository offenceRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        List<OffenceEntity> offenceEntityList = courtDataDTO.getCaseDetails().getDefendant().getOffences()
                .stream()
                .map(offence -> buildOffences(offence, courtDataDTO))
                .collect(Collectors.toList());
        offenceRepository.saveAll(offenceEntityList);
    }

    protected OffenceEntity buildOffences(Offence offence, CourtDataDTO courtDataDTO) {
        return OffenceEntity.builder()
                .caseId(courtDataDTO.getCaseId())
                .txId(courtDataDTO.getTxId())
                .asnSeq(String.format(LEADING_ZERO_3, Integer.parseInt(offence.getAsnSeq())))
                .offenceCode(offence.getOffenceCode())
                .offenceClassification(offence.getOffenceClassification())
                .legalAidStatus(offence.getLegalAidStatus())
                .legalAidStatusDate(DateUtil.parse(offence.getLegalAidStatusDate()))
                .legalaidReason(offence.getLegalAidReason())
                .offenceDate(parse((offence.getOffenceDate())))
                .offenceShortTitle(offence.getOffenceShortTitle())
                .modeOfTrial(offence.getModeOfTrial())
                .offenceWording(offence.getOffenceWording())
                .iojDecision(PENDING_IOJ_DECISION)
                .wqOffence(G_NO)
                .applicationFlag(G_NO)
                .offenceId(offence.getOffenceId())
                .build();
    }


}
