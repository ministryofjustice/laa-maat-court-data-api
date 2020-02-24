package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.repository.DefendantRepository;
import gov.uk.courtdata.util.CourtDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.CREATE_LINK;
import static gov.uk.courtdata.constants.CourtDataConstants.SEARCH_TYPE_0;


@Component
@RequiredArgsConstructor
public class DefendantInfoProcessor implements Process {

    private final DefendantRepository defendantRepository;
    private final CourtDataUtil courtDataUtil;

    @Override
    public void process(CreateLinkDto saveAndLinkModel) {

        DefendantMAATDataEntity defendantMAATDataEntity = saveAndLinkModel.getDefendantMAATDataEntity();
        CaseDetails caseDetails = saveAndLinkModel.getCaseDetails();
        Defendant defendant = caseDetails.getDefendant();

        DefendantEntity defendantEntity = DefendantEntity.builder()
                .caseId(saveAndLinkModel.getCaseId())
                .txId(saveAndLinkModel.getTxId())
                .forename(defendant.getForename())
                .surname(defendant.getSurname())
                .organisation(defendant.getOrganization())
                .dateOfBirth(courtDataUtil.getDate(defendant.getDateOfBirth()))
                .address_line1(defendant.getAddress_line1())
                .address_line2(defendant.getAddress_line2())
                .address_line3(defendant.getAddress_line3())
                .address_line4(defendant.getAddress_line4())
                .address_line5(defendant.getAddress_line5())
                .postCode(defendant.getPostcode())
                .nino(defendant.getNino())
                .telephoneHome(defendant.getTelephoneHome())
                .telephoneWork(defendant.getTelephoneWork())
                .telephoneMobile(defendant.getTelephoneMobile())
                .email1(defendant.getEmail1())
                .email2(defendant.getEmail2())
                .pline1(defendantMAATDataEntity.getPline1())
                .pline2(defendantMAATDataEntity.getPline2())
                .pline3(defendantMAATDataEntity.getPline3())
                .pcity(defendantMAATDataEntity.getPcity())
                .ppostcode(defendantMAATDataEntity.getPpostcode())
                .pcountry(defendantMAATDataEntity.getPcountry())
                .pcountry(defendantMAATDataEntity.getPcountry())
                .useSol(defendantMAATDataEntity.getUseSol())
                .searchType(SEARCH_TYPE_0)
                .datasource(CREATE_LINK)
                .build();
        defendantRepository.save(defendantEntity);


    }
}
