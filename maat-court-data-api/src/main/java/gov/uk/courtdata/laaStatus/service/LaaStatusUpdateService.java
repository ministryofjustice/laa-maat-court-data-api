package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.laaStatus.impl.LaaStatusUpdateImpl;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaaStatusUpdateService {



    private final LaaStatusUpdateImpl laaStatusUpdateImpl;
    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    public void execute(CourtDataDTO courtDataDTO) {
        courtDataDTO = buildCourtData(courtDataDTO.getCaseDetails());
        laaStatusUpdateImpl.execute(courtDataDTO);
    }

    private CourtDataDTO buildCourtData(CaseDetails caseDetails) {
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = wqLinkRegisterRepository.findBymaatId(caseDetails.getMaatId());
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterEntityList.get(0);
        return CourtDataDTO.builder()
                .caseDetails(caseDetails)
                .caseId(wqLinkRegisterEntity.getCaseId())
                .libraId(Integer.parseInt(wqLinkRegisterEntity.getLibraId()))
                .proceedingId(wqLinkRegisterEntity.getProceedingId())
                .build();

    }
}
