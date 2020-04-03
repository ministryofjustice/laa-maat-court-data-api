package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.laaStatus.controller.LaaStatusCDAController;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.laastatus.*;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaaStatusPostCDAService {

    private final RepOrderCPDataRepository repOrderCPDataRepository;

    private final LaaStatusCDAController laaStatusCDAController;

    private final CourtDataApiClient courtDataApiClient;


    public void process(final CaseDetails caseDetails) {
        LaaStatusUpdate laaStatusUpdate = buildMessage(caseDetails);
        log.info(laaStatusUpdate.toString());
        // laaStatusCDAController.updateLaaStatus(laaStatusUpdate);

        courtDataApiClient.invoke(laaStatusUpdate);


    }


    /**
     * @param caseDetails
     */
    private LaaStatusUpdate buildMessage(CaseDetails caseDetails) {

        log.info(caseDetails.toString());

        List<Offence> offences =
                caseDetails.getDefendant().getOffences().stream().map(this::mapOffence).collect(Collectors.toList());

        return LaaStatusUpdate.builder().maatId(caseDetails.getMaatId())
                .defendant(mapDefendant(caseDetails))
                .offences(offences)
                .solicitor(mapSolicitor(caseDetails))
                .build();

    }


    /**
     * @param caseDetails
     * @return
     */
    private Defendant mapDefendant(final CaseDetails caseDetails) {

        Optional<RepOrderCPDataEntity>
                repOrderCPDataEntity = repOrderCPDataRepository.findByrepOrderId(caseDetails.getMaatId());

        return Defendant.builder()
                .uuid(repOrderCPDataEntity.get().getDefendantId())
                .foreName(caseDetails.getDefendant().getForename())
                .surName(caseDetails.getDefendant().getSurname())
                .build();
    }


    /**
     * @param offence
     * @return
     */
    private Offence mapOffence(final gov.uk.courtdata.model.Offence offence) {

        final String uuid = "";

        return Offence.builder()
                .uuid(uuid)
                .offenceCode(offence.getOffenceCode())
                .offenceShortTitle(offence.getOffenceShortTitle())
                .offenceClassification(offence.getOffenceClassification())
                .modeOfTrial(offence.getModeOfTrial())
                .offenceDate(offence.getOffenceDate())
                .legalAid(LegalAid.builder()
                        .status(offence.getLegalAidStatus())
                        .statusDate(offence.getLegalAidStatusDate())
                        .startDate(offence.getLegalAidStatusDate())
                        .endDate("").build())
                .build();
    }


    /**
     * @param caseDetails
     * @return
     */
    private Solicitor mapSolicitor(final CaseDetails caseDetails) {

        return Solicitor.builder()
                .firmName(caseDetails.getSolicitor().getFirstName())
                .laaAccountNumber(caseDetails.getSolicitor().getLaaOfficeAccount())
                .contactName(caseDetails.getSolicitor().getContactName())
                .addressLine1(caseDetails.getSolicitor().getAddress_line1())
                .addressLine2(caseDetails.getSolicitor().getAddress_line2())
                .postcode(caseDetails.getSolicitor().getPostcode())
                .phone(caseDetails.getSolicitor().getTelephone())
                .email(caseDetails.getSolicitor().getEmail())
                .build();

    }
}
