package gov.uk.courtdata.laaStatus.processor;

import gov.uk.courtdata.dto.LaaModelManager;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.link.processor.WqLinkRegisterProcessor;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateWqLinkRegisterProcessor extends WqLinkRegisterProcessor {


    private WqLinkRegisterRepository wqLinkRegisterRepository;

    public UpdateWqLinkRegisterProcessor(WqLinkRegisterRepository wqLinkRegisterRepository) {
        super(wqLinkRegisterRepository);
    }

    @Override
    public void process(LaaModelManager laaModelManager) {
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterRepository
                .findBymaatId(laaModelManager.getCaseDetails()
                        .getMaatId()).get(0);
        if (wqLinkRegisterEntity.getMlrCat() != geCategory(laaModelManager)) {
            super.process(laaModelManager);
        }
    }


    @Override
    protected int geCategory(LaaModelManager laaModelManager) {
        return laaModelManager.getCaseDetails().getCategory();
    }

    @Autowired
    public void setWqLinkRegisterRepository(WqLinkRegisterRepository wqLinkRegisterRepository) {
        this.wqLinkRegisterRepository = wqLinkRegisterRepository;
    }
}
