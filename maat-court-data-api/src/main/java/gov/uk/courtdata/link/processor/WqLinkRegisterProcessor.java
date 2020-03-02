package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.COMMON_PLATFORM;

@RequiredArgsConstructor
@Component
public class WqLinkRegisterProcessor implements Process {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    @Override
    public void process(LaaModelManager laaModelManager) {

        CaseDetails caseDetails = laaModelManager.getCaseDetails();
        int maatCat = geCategory(laaModelManager);
        final WqLinkRegisterEntity wqLinkRegisterEntity = WqLinkRegisterEntity.builder()
                .createdTxId(laaModelManager.getTxId())
                .createdDate(LocalDate.now())
                .createdUserId(caseDetails.getCreatedUser())
                .caseId(laaModelManager.getCaseId())
                .libraId(COMMON_PLATFORM + laaModelManager.getLibraId())
                .maatId(caseDetails.getMaatId())
                .cjsAreaCode(caseDetails.getCjsAreaCode())
                .cjsLocation(caseDetails.getCjsLocation())
                .proceedingId(laaModelManager.getProceedingId())
                .maatCat(maatCat)
                .mlrCat(maatCat)
                .build();
        wqLinkRegisterRepository.save(wqLinkRegisterEntity);
    }

    protected int geCategory(LaaModelManager laaModelManager) {
        return laaModelManager.getSolicitorMAATDataEntity().getCmuId();
    }
}
