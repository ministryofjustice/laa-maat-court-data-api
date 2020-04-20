package gov.uk.courtdata.unlink.processor;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.unlink.impl.UnLinkImpl;
import gov.uk.courtdata.unlink.validator.UnlinkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnLinkProcessor {


    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final RepOrderCPDataRepository repOrderCPDataRepository;
    private final UnlinkValidator unlinkValidator;
    private final UnLinkImpl unlinkImpl;

    public UnlinkModel process(Unlink unlinkJson) {

        log.info("unlinkjson {}",unlinkJson);

        unlinkValidator.validateRequest(unlinkJson);
        UnlinkModel unlinkModel =  UnlinkModel.builder().unlink(unlinkJson).build();
        mapWqLinkRegister(unlinkModel);
        mapRepOrderCpData(unlinkModel);
        unlinkImpl.execute(unlinkModel);
        return unlinkModel;
    }

    private void mapWqLinkRegister(UnlinkModel unlinkModel) {
        Integer maatId = unlinkModel.getUnlink().getMaatId();
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = wqLinkRegisterRepository
                .findBymaatId(maatId);
        unlinkValidator.validateWQLinkRegister(wqLinkRegisterEntityList, maatId);
        unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntityList.get(0));
    }

    private void mapRepOrderCpData(UnlinkModel unlinkModel) {
        Unlink unlink = unlinkModel.getUnlink();
        Optional<RepOrderCPDataEntity> repOrderCPDataEntity =
                repOrderCPDataRepository.findByrepOrderId(unlink.getMaatId());
        unlinkModel.setRepOrderCPDataEntity(repOrderCPDataEntity.get());

    }
}
