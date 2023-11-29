package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.ResultEntity;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static gov.uk.courtdata.util.DateUtil.parse;

@Component
@RequiredArgsConstructor
public class ResultsInfoProcessor implements Process {

    private final ResultRepository resultRepository;


    @Override
    public void process(CourtDataDTO courtDataDTO) {

        courtDataDTO.getCaseDetails().getDefendant().getOffences()
                .forEach(offence -> buildResultsList(offence.getResults(), courtDataDTO));
    }

    private void buildResultsList(List<Result> resultList, CourtDataDTO saveAndLinkModel) {

        if (resultList != null && !resultList.isEmpty()) {
            List<ResultEntity> resultEntityList = resultList
                    .stream()
                    .map(result -> buildResult(result, saveAndLinkModel))
                    .collect(Collectors.toList());
            resultRepository.saveAll(resultEntityList);
        }
    }


    private ResultEntity buildResult(Result result, CourtDataDTO saveAndLinkModel) {

        return ResultEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .asn(saveAndLinkModel.getCaseDetails().getAsn())
                .courtLocation(result.getCourtLocation())
                .asnSeq(result.getAsnSeq())
                .resultText(result.getResultText())
                .contactName(result.getContactName())
                .firmName(result.getFirstName())
                .laaOfficeAccount(result.getLaaOfficeAccount())
                .legalAidWithdrawalDate(parse(result.getLegalAidWithdrawalDate()))
                .nextHearingDate(parse(result.getNextHearingDate()))
                .nextHearingLocation(result.getNextHearingLocation())
                .receivedDate(result.getReceivedDate())
                .resultCode(result.getResultCode())
                .resultCodeQualifiers(result.getResultCodeQualifiers())
                .resultShortTitle(result.getResultShortTitle())
                .sessionValidateDate(parse(result.getSessionValidateDate()))
                .legalAidWithdrawalDate(parse(result.getLegalAidWithdrawalDate()))
                .dateOfHearing(parse(result.getDateOfHearing()))
                .wqResult(G_NO)
                .build();
    }

}
