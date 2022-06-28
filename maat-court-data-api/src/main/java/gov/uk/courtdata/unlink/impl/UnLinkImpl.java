package gov.uk.courtdata.unlink.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.UnlinkEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.InCommonPlatformFlag;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static org.apache.commons.lang3.StringUtils.isBlank;


@Component
@XRayEnabled
@RequiredArgsConstructor
public class UnLinkImpl {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final WqCoreRepository wqCoreRepository;
    private final UnlinkReasonRepository unlinkReasonRepository;
    private final IdentifierRepository identifierRepository;
    private final RepOrderCPDataRepository repOrderCPDataRepository;

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public void execute(UnlinkModel unlinkModel) {

        mapTxnID(unlinkModel);
        processWqCore(unlinkModel);
        processUnlinkReason(unlinkModel);
        processUnLinkWQRegister(unlinkModel);
        processCPData(unlinkModel);
    }

    private void mapTxnID(UnlinkModel unlinkModel) {
        unlinkModel.setTxId(identifierRepository.getTxnID());
    }

    private void processUnLinkWQRegister(UnlinkModel unlinkModel) {

        WqLinkRegisterEntity wqLinkRegisterEntity = unlinkModel.getWqLinkRegisterEntity();
        wqLinkRegisterEntity.setRemovedTxId(unlinkModel.getTxId());
        wqLinkRegisterEntity.setRemovedUserId(unlinkModel.getUnlink().getUserId());
        wqLinkRegisterEntity.setRemovedDate(LocalDateTime.now());

        wqLinkRegisterRepository.save(wqLinkRegisterEntity);
    }

    private void processUnlinkReason(UnlinkModel unlinkModel) {
        Unlink unlink = unlinkModel.getUnlink();

        final String otherReasonText = isBlank(unlink.getOtherReasonText()) ? SYSTEM_UNLINKED :
                unlink.getOtherReasonText();

        UnlinkEntity unlinkEntity = UnlinkEntity.builder()
                .caseId(unlinkModel.getWqLinkRegisterEntity().getCaseId())
                .reasonId(unlink.getReasonId())
                .otherReason(otherReasonText)
                .txId(unlinkModel.getTxId())
                .build();

        unlinkReasonRepository.save(unlinkEntity);
    }

    private void processWqCore(UnlinkModel unlinkModel) {

        Unlink unlink = unlinkModel.getUnlink();
        WqLinkRegisterEntity wqLinkRegisterEntity = unlinkModel.getWqLinkRegisterEntity();
        WqCoreEntity wqCoreEntity = WqCoreEntity.builder()
                .txId(unlinkModel.getTxId())
                .wqType(WQ_UNLINK_EVENT)
                .wqStatus(WQ_SUCCESS_STATUS)
                .createdUserId(unlink.getUserId())
                .createdTime(LocalDateTime.now())
                .caseId(wqLinkRegisterEntity.getCaseId())
                .build();
        wqCoreRepository.save(wqCoreEntity);
    }

    private void processCPData(UnlinkModel unlinkModel) {

        RepOrderCPDataEntity repOrderCPData = unlinkModel.getRepOrderCPDataEntity();
        repOrderCPData.setInCommonPlatform(InCommonPlatformFlag.NO.getValue());
        repOrderCPDataRepository.save(repOrderCPData);
    }
}
