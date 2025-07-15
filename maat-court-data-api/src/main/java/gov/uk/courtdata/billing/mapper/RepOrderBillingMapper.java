package gov.uk.courtdata.billing.mapper;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.entity.RepOrderBillingEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RepOrderBillingMapper {
    private RepOrderBillingMapper() { }

    public static RepOrderBillingDTO mapEntityToDTO(RepOrderBillingEntity repOrderEntity) {
        Integer magsCourtId = StringUtils.isEmpty(repOrderEntity.getMagsCourtId())
            ? null : Integer.parseInt(repOrderEntity.getMagsCourtId());

        return RepOrderBillingDTO.builder()
            .id(repOrderEntity.getId())
            .applicantId(repOrderEntity.getApplicantId())
            .arrestSummonsNo(repOrderEntity.getArrestSummonsNo())
            .evidenceFeeLevel(repOrderEntity.getEvidenceFeeLevel())
            .supplierAccountCode(repOrderEntity.getSupplierAccountCode())
            .magsCourtId(magsCourtId)
            .magsCourtOutcome(repOrderEntity.getMagsCourtOutcome())
            .dateReceived(repOrderEntity.getDateReceived())
            .crownCourtRepOrderDate(repOrderEntity.getCrownCourtRepOrderDate())
            .offenceType(repOrderEntity.getOffenceType())
            .crownCourtWithdrawalDate(repOrderEntity.getCrownCourtWithdrawalDate())
            .applicantHistoryId(repOrderEntity.getApplicantHistoryId())
            .caseId(repOrderEntity.getCaseId())
            .committalDate(repOrderEntity.getCommittalDate())
            .repOrderStatus(repOrderEntity.getRepOrderStatus())
            .appealTypeCode(repOrderEntity.getAppealTypeCode())
            .crownCourtOutcome(repOrderEntity.getCrownCourtOutcome())
            .dateCreated(repOrderEntity.getDateCreated())
            .userCreated(repOrderEntity.getUserCreated())
            .dateModified(repOrderEntity.getDateModified())
            .userModified(repOrderEntity.getUserModified())
            .caseType(repOrderEntity.getCaseType())
            .build();
    }
}
