package gov.uk.courtdata.laaStatus.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.link.processor.Process;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateWqLinkRegisterProcessor implements Process {


    private final WqLinkRegisterRepository wqLinkRegisterRepository;


    public void process(CourtDataDTO courtDataDTO) {
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterRepository
                .findBymaatId(courtDataDTO.getCaseDetails()
                        .getMaatId()).get(0);
        final int category = geCategory(courtDataDTO);
        if (wqLinkRegisterEntity.getMlrCat() != category) {
            wqLinkRegisterEntity.setMlrCat(category);
            wqLinkRegisterRepository.save(wqLinkRegisterEntity);
        }
    }


    protected int geCategory(CourtDataDTO courtDataDTO) {
        return courtDataDTO.getCaseDetails().getCategory();
    }


}
