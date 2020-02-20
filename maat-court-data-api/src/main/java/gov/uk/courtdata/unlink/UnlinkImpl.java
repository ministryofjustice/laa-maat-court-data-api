package gov.uk.courtdata.unlink;

import gov.uk.courtdata.entity.UnlinkEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.UnlinkReasonRepository;
import gov.uk.courtdata.repository.WqCoreRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static gov.uk.courtdata.constants.CourtDataConstants.*;


@Component
@RequiredArgsConstructor
public class UnlinkImpl {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final WqCoreRepository wqCoreRepository;
    private final UnlinkReasonRepository unlinkReasonRepository;

    public void execute(UnlinkModel unlinkModel) {

        processWqCore(unlinkModel);
        processUnlinkReason(unlinkModel);
        processUnLinkWQRegister(unlinkModel);
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
}
