package gov.uk.courtdata.unlink.impl;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.UnlinkEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_SUCCESS_STATUS;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_UNLINK_EVENT;


@Component
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
        wqLinkRegisterEntity.setRemovedDate(LocalDate.now());

        wqLinkRegisterRepository.save(wqLinkRegisterEntity);
    }

    private void processUnlinkReason(UnlinkModel unlinkModel) {
        Unlink unlink = unlinkModel.getUnlink();
        UnlinkEntity unlinkEntity = UnlinkEntity.builder()
                .caseId(unlinkModel.getWqLinkRegisterEntity().getCaseId())
                .reasonId(unlink.getReasonId())
                .otherReason(unlink.getReasonText())
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
                .createdTime(LocalDate.now())
                .caseId(wqLinkRegisterEntity.getCaseId())
                .build();
        wqCoreRepository.save(wqCoreEntity);
    }

    private void processCPData(UnlinkModel unlinkModel) {

        RepOrderCPDataEntity repOrderCPData = unlinkModel.getRepOrderCPDataEntity();
        repOrderCPData.setDefendantId(null);
        repOrderCPDataRepository.save(repOrderCPData);
    }
}
