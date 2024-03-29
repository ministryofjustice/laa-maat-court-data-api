package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static gov.uk.courtdata.constants.CourtDataConstants.LEADING_ZERO_2;

@Component
@RequiredArgsConstructor
public class WqLinkRegisterProcessor implements Process {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Integer maatCat = geCategory(courtDataDTO);
        final WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder()
                .createdTxId(courtDataDTO.getTxId())
                .createdDate(LocalDateTime.now())
                .createdUserId(caseDetails.getCreatedUser())
                .caseId(courtDataDTO.getCaseId())
                .libraId(courtDataDTO.getLibraId())
                .maatId(caseDetails.getMaatId())
                .cjsAreaCode(String.format(LEADING_ZERO_2, Integer.parseInt(caseDetails.getCjsAreaCode())))
                .cjsLocation(caseDetails.getCjsLocation())
                .proceedingId(courtDataDTO.getProceedingId())
                .maatCat(maatCat)
                .mlrCat(maatCat)
                .caseUrn(caseDetails.getCaseUrn())
                .build();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);


    }

    protected Integer geCategory(CourtDataDTO courtDataDTO) {
        return courtDataDTO.getSolicitorMAATDataEntity().getCmuId();
    }
}
