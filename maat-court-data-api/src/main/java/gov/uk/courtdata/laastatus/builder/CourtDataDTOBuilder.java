package gov.uk.courtdata.laastatus.builder;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class CourtDataDTOBuilder {


    private final WqLinkRegisterRepository wqLinkRegisterRepository;
    private final SolicitorMAATDataRepository solicitorMAATDataRepository;
    private final DefendantMAATDataRepository defendantMAATDataRepository;
    private final OffenceRepository offenceRepository;

    /**
     * @param caseDetails
     * @return
     */
    public CourtDataDTO build(CaseDetails caseDetails) {

        final Integer maatId = caseDetails.getMaatId();
        List<WqLinkRegisterEntity> wqLinkRegisterEntityList = wqLinkRegisterRepository.findBymaatId(maatId);
        final Optional<SolicitorMAATDataEntity> optSolicitorMAATDataEntity = solicitorMAATDataRepository.findBymaatId(maatId);
        SolicitorMAATDataEntity solicitorMAATDataEntity = optSolicitorMAATDataEntity.orElse(null);

        final Optional<DefendantMAATDataEntity> optDefendantMAATDataEntity = defendantMAATDataRepository.findBymaatId(maatId);
        DefendantMAATDataEntity defendantMAATDataEntity = optDefendantMAATDataEntity.orElse(null);
        WqLinkRegisterEntity wqLinkRegisterEntity = wqLinkRegisterEntityList.iterator().next();

        log.info("Build status message to post.");

        caseDetails.getDefendant().getOffences().forEach(offence -> {

            Optional<OffenceEntity> offenceEntity
                    = offenceRepository.findByMaxTxId(wqLinkRegisterEntity.getCaseId(), offence.getAsnSeq());

            offence.setOffenceId(offenceEntity.map(OffenceEntity::getOffenceId).orElse(null));


        });

        return CourtDataDTO.builder()
                .caseDetails(caseDetails)
                .caseId(wqLinkRegisterEntity.getCaseId())
                .libraId(wqLinkRegisterEntity.getLibraId().substring(2))
                .proceedingId(wqLinkRegisterEntity.getProceedingId())
                .defendantMAATDataEntity(defendantMAATDataEntity)
                .solicitorMAATDataEntity(solicitorMAATDataEntity)
                .build();
    }


}
