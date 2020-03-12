package gov.uk.courtdata.laaStatus.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
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
    public void process(CourtDataDTO courtDataDTO) {
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterRepository
                .findBymaatId(courtDataDTO.getCaseDetails()
                        .getMaatId()).get(0);
        if (wqLinkRegisterEntity.getMlrCat() != geCategory(courtDataDTO)) {
            super.process(courtDataDTO);
        }
    }


    @Override
    protected int geCategory(CourtDataDTO courtDataDTO) {
        return courtDataDTO.getCaseDetails().getCategory() != null ? courtDataDTO.getCaseDetails().getCategory() : super.geCategory(courtDataDTO);
    }

    @Autowired
    public void setWqLinkRegisterRepository(WqLinkRegisterRepository wqLinkRegisterRepository) {
        this.wqLinkRegisterRepository = wqLinkRegisterRepository;
    }
}
