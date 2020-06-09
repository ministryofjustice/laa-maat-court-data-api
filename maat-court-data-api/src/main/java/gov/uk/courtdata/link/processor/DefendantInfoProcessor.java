package gov.uk.courtdata.link.processor;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.repository.DefendantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.CREATE_LINK;
import static gov.uk.courtdata.constants.CourtDataConstants.SEARCH_TYPE_0;
import static gov.uk.courtdata.util.DateUtil.parse;


@Component
@RequiredArgsConstructor
public class DefendantInfoProcessor implements Process {

    private final DefendantRepository defendantRepository;

    @Override
    public void process(CourtDataDTO courtDataDTO) {

        DefendantMAATDataEntity defendantMAATDataEntity = courtDataDTO.getDefendantMAATDataEntity();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Defendant defendant = caseDetails.getDefendant();

        DefendantEntity defendantEntity = DefendantEntity.builder()
                .caseId(courtDataDTO.getCaseId())
                .txId(courtDataDTO.getTxId())
                .forename(defendant.getForename())
                .surname(defendant.getSurname())
                .organisation(defendant.getOrganization())
                .dateOfBirth(parse(defendant.getDateOfBirth()))
                .addressLine1(defendant.getAddress_line1())
                .addressLine2(defendant.getAddress_line2())
                .addressLine3(defendant.getAddress_line3())
                .addressLine4(defendant.getAddress_line4())
                .addressLine5(defendant.getAddress_line5())
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
                .datasource(getDataSource())
                .build();
        defendantRepository.save(defendantEntity);


    }

    protected String getDataSource() {
        return CREATE_LINK;
    }
}
