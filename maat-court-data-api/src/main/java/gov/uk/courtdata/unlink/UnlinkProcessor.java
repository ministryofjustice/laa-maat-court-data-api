package gov.uk.courtdata.unlink;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.model.UnlinkModel;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.unlink.validate.UnlinkValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnlinkProcessor {

    private final Gson gson;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final UnlinkValidator unlinkValidator;
    private final DefendantMAATDataRepository defendantMAATDataRepository;
    private final UnlinkImpl unlinkImpl;

    public void process(String unlinkJson) {

        UnlinkModel unlinkModel = new UnlinkModel();
        Unlink unlink = gson.fromJson(unlinkJson, Unlink.class);
        unlinkValidator.validateRequest(unlink);
        unlinkModel.setUnlink(unlink);
        mapWqLinkRegister(unlinkModel);
        unlinkImpl.execute(unlinkModel);
    }

    private void mapWqLinkRegister(UnlinkModel unlinkModel) {
        Integer maatId = unlinkModel.getUnlink().getMaatId();
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = wqLinkRegisterRepository
                .findBymaatId(maatId);
        unlinkValidator.validateWQLinkRegister(wqLinkRegisterEntityList, maatId);
        unlinkModel.setWqLinkRegisterEntity(wqLinkRegisterEntityList.get(0));
        unlinkModel.setTxId(defendantMAATDataRepository.getTxnID());
    }
}
