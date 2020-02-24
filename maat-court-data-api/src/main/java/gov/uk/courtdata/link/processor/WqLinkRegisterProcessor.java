package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.COMMON_PLATFORM;

@RequiredArgsConstructor
@Component
public class WqLinkRegisterProcessor implements Process {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    @Override
    public void process(CreateLinkDto saveAndLinkModel) {

        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        int maatCat = saveAndLinkModel.getSolicitorMAATDataEntity().getCmuId();
        final WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder()
                .createdTxId(saveAndLinkModel.getTxId())
                .createdDate(LocalDate.now())
                .createdUserId(caseDetails.getCreatedUser())
                .caseId(saveAndLinkModel.getCaseId())
                .libraId(COMMON_PLATFORM + saveAndLinkModel.getLibraId())
                .maatId(caseDetails.getMaatId())
                .cjsAreaCode(caseDetails.getCjsAreaCode())
                .cjsLocation(caseDetails.getCjsLocation())
                .proceedingId(saveAndLinkModel.getProceedingId())
                .maatCat(maatCat)
                .mlrCat(maatCat)
                .build();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);
    }
}
