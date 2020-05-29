package gov.uk.courtdata.laaStatus.builder;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.laastatus.*;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@Slf4j
@AllArgsConstructor
public class RepOrderUpdateMessageBuilder {


    private final RepOrderCPDataRepository repOrderCPDataRepository;

    private final SolicitorMAATDataRepository solicitorMAATDataRepository;


    /**
     * Build representation order(aka laa status update) message to post to CDA.
     *
     * @param caseDetails
     */
    public LaaStatusUpdate build(final CaseDetails caseDetails) {

        return LaaStatusUpdate.builder().data(
                RepOrderData.builder()
                        .type("representation_order")
                        .attributes(buildAttributes(caseDetails))
                        .relationships(mapRelationships(caseDetails)).build())
                .build();

    }

    private Attributes buildAttributes(CaseDetails caseDetails) {

        List<Offence> offences =
                caseDetails.getDefendant().getOffences().stream()
                        .map(this::mapOffence).collect(Collectors.toList());
        return Attributes.builder()
                .maatReference(caseDetails.getMaatId())
                .defenceOrganisation(mapDefenceOrganisation(caseDetails))
                .offences(offences)
                .build();
    }


    /**
     *
     * @param caseDetails
     * @return
     */
    private DefenceOrganisation mapDefenceOrganisation(CaseDetails caseDetails) {

        SolicitorMAATDataEntity solicitorDetails = solicitorMAATDataRepository.findBymaatId(caseDetails.getMaatId()).get();

        return DefenceOrganisation.builder()
                .laaContractNumber(solicitorDetails.getAccountCode())
                .organisation(findSolicitorDetails(solicitorDetails))
                .build();
    }


    /**
     *
     * @param solicitorMAATDataEntity
     * @return
     */
    private Organisation findSolicitorDetails(SolicitorMAATDataEntity solicitorMAATDataEntity) {

        return Organisation.builder()
                //TODO: to be enabled when CDA handled invalid data.
                //.address(mapAddress(solicitorMAATDataEntity))
                // .contact(mapContact(solicitorMAATDataEntity))
                .name(solicitorMAATDataEntity.getAccountName())
                .build();
    }

    /**
     *
     * @param solicitorDetails
     * @return
     */
    private Address mapAddress(SolicitorMAATDataEntity solicitorDetails) {

        return Address.builder().address1(solicitorDetails.getLine1())
                .address2(solicitorDetails.getLine2())
                .address3(solicitorDetails.getLine3())
                .address4(solicitorDetails.getCity())
                .address5(solicitorDetails.getCounty())
                .postcode(solicitorDetails.getPostcode()).build();
    }

    /**
     *
     * @param solicitorDetails
     * @return
     */
    private Contact mapContact(SolicitorMAATDataEntity solicitorDetails) {
        return Contact.builder().work(solicitorDetails.getPhone())
                .primaryEmail(solicitorDetails.getAdminEmail())
                .secondaryEmail(solicitorDetails.getEmail()).build();

    }


    /**
     * @param offence
     * @return
     */
    private Offence mapOffence(final gov.uk.courtdata.model.Offence offence) {

        return Offence.builder()
                .offenceId(offence.getOffenceId())

                .statusCode(offence.getLegalAidStatus())
                .statusDate(offence.getLegalAidStatusDate())
                .effectiveStartDate(offence.getLegalAidStatusDate())
                .effectiveEndDate(offence.getLegalAidStatusDate()).build();
    }


    /**
     *
     * @param caseDetails
     * @return
     */
    private Relationships mapRelationships(final CaseDetails caseDetails) {

        return Relationships.builder()
                .defendant(mapDefendant(caseDetails.getMaatId()))
                .build();
    }

    /**
     *
     * @param maatId
     * @return
     */
    private Defendant mapDefendant(final Integer maatId) {

        return Defendant.builder().data(DefendantData.builder()
                .id(findDefendantId(maatId))
                .type("defendants").build())
                .build();
    }


    /**
     *
     * @param maatId
     * @return
     */
    private String findDefendantId(final Integer maatId) {
        Optional<RepOrderCPDataEntity> repOrderCPData
                = repOrderCPDataRepository.findByrepOrderId(maatId);

        return repOrderCPData.isPresent() ? repOrderCPData.get().getDefendantId() : null;
    }

}
