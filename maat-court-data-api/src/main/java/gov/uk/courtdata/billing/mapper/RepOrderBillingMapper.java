package gov.uk.courtdata.billing.mapper;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RepOrderBillingMapper {
    private RepOrderBillingMapper() { }

    public static RepOrderBillingDTO mapEntityToDTO(RepOrderEntity repOrderEntity) {
        Integer magsCourtId = StringUtils.isEmpty(repOrderEntity.getMacoCourt())
            ? null : Integer.parseInt(repOrderEntity.getMacoCourt());

        return RepOrderBillingDTO.builder()
            .id(repOrderEntity.getId())
            .applicantId(repOrderEntity.getApplicationId())
            .arrestSummonsNo(repOrderEntity.getArrestSummonsNo())
            .evidenceFeeLevel(repOrderEntity.getEvidenceFeeLevel())
            .supplierAccountCode(repOrderEntity.getSuppAccountCode())
            .magsCourtId(magsCourtId)
            .magsCourtOutcome(repOrderEntity.getMagsOutcome())
            .dateReceived(repOrderEntity.getDateReceived())
            .crownCourtRepOrderDate(repOrderEntity.getCrownRepOrderDate())
            .offenceType(repOrderEntity.getOftyOffenceType())
            .crownCourtWithdrawalDate(repOrderEntity.getCrownWithdrawalDate())
            .applicantHistoryId(repOrderEntity.getApplicantHistoryId())
            .caseId(repOrderEntity.getCaseId())
            .committalDate(repOrderEntity.getCommittalDate())
            .repOrderStatus(repOrderEntity.getRorsStatus())
            .appealTypeCode(repOrderEntity.getAppealTypeCode())
            .crownCourtOutcome(repOrderEntity.getCrownOutcome())
            .dateCreated(repOrderEntity.getDateCreated())
            .userCreated(repOrderEntity.getUserCreated())
            .dateModified(repOrderEntity.getDateModified())
            .userModified(repOrderEntity.getUserModified())
            .caseType(repOrderEntity.getCatyCaseType())
            .build();
    }
}
