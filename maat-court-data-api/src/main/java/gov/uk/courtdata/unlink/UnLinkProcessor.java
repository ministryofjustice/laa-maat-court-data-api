package gov.uk.courtdata.unlink;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.unlink.validate.UnlinkValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnLinkProcessor {


    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final UnlinkValidator unlinkValidator;
    private final UnLinkImpl unlinkImpl;

    public UnlinkModel process(Unlink unlinkJson) {

        UnlinkModel unlinkModel = new UnlinkModel();
        unlinkValidator.validateRequest(unlinkJson);
        unlinkModel.setUnlink(unlinkJson);
        mapWqLinkRegister(unlinkModel);
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
}
