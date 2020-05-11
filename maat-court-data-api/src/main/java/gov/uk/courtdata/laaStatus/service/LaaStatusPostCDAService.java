package gov.uk.courtdata.laaStatus.service;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.laaStatus.controller.LaaStatusCDAController;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.laastatus.*;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaaStatusPostCDAService {

    private final RepOrderCPDataRepository repOrderCPDataRepository;

    private final LaaStatusCDAController laaStatusCDAController;

    private final CourtDataApiClient courtDataApiClient;

    private final SolicitorMAATDataRepository solicitorMAATDataRepository;


    public void process(final CaseDetails caseDetails) {
        RootData repOrderData = buildMessage(caseDetails);
        log.info(repOrderData.toString());
        laaStatusCDAController.updateLaaStatus(repOrderData);

      //  courtDataApiClient.invoke(repOrderData);


    }


    /**
     * @param caseDetails
     */
    private RootData buildMessage(CaseDetails caseDetails) {

        log.info(caseDetails.toString());


        return RootData.builder().data(RepOrderData.builder()
                .type("representation_orders")
                .attributes(buildAttributes(caseDetails))
                .relationships(mapRelationships()).build()).build();

    }


    public RepOrderData buildRepOrderData(CaseDetails caseDetails) {

        return RepOrderData.builder().attributes(buildAttributes(caseDetails))
                .relationships(mapRelationships()).build();

    }


    public Attributes buildAttributes(CaseDetails caseDetails) {

        List<Offence> offences =
                caseDetails.getDefendant().getOffences().stream().map(this::mapOffence).collect(Collectors.toList());
        return Attributes.builder()
                .maatReference(caseDetails.getMaatId())
                .defenceOrganisation(mapDefenceOrganisation(caseDetails))
                .offences(offences)
                .build();
    }


    public DefenceOrganisation mapDefenceOrganisation(CaseDetails caseDetails) {

        SolicitorMAATDataEntity solicitorDetails = solicitorMAATDataRepository.findBymaatId(caseDetails.getMaatId()).get();

        return DefenceOrganisation.builder().laaContractNumber(solicitorDetails.getAccountCode())
                .organisation(mapOrganisation(solicitorDetails)).build();
    }


    public Organisation mapOrganisation(SolicitorMAATDataEntity solicitorMAATDataEntity) {

        return Organisation.builder().address(mapAddress(solicitorMAATDataEntity))
                .contact(mapContact(solicitorMAATDataEntity))
                .name(solicitorMAATDataEntity.getAccountName()).build();
    }


    public Address mapAddress(SolicitorMAATDataEntity solicitorDetails) {

        return Address.builder().address1(solicitorDetails.getLine1())
                .address2(solicitorDetails.getLine2())
                .address3(solicitorDetails.getLine3())
                .address4(solicitorDetails.getCity())
                .address5(solicitorDetails.getCounty())
                .postcode(solicitorDetails.getPostcode()).build();
    }

    public Contact mapContact(SolicitorMAATDataEntity solicitorDetails) {
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


    private Relationships mapRelationships() {
        return Relationships.builder().defendant(mapDefendant()).build();
    }

    private Defendant mapDefendant() {
        return Defendant.builder().data(DefendantData.builder().id("61e16fc1-9036-4a28-93b8-ec096b06b0b7")
                .type("defendants").build()).build();
    }

}
