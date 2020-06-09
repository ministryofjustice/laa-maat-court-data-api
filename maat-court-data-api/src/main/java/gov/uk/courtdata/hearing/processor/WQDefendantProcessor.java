package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQDefendant;
import gov.uk.courtdata.hearing.dto.DefendantDTO;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQDefendantRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WQDefendantProcessor {

    private final WQDefendantRepository defendantRepository;

    public void process(final HearingDTO magsCourtDTO) {

        DefendantDTO defendantDTO = magsCourtDTO.getDefendant();

        WQDefendant defendantEntity = WQDefendant.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .forename(defendantDTO.getForename())
                .surname(defendantDTO.getSurname())
                .dateOfBirth(DateUtil.parse(defendantDTO.getDateOfBirth()))
                .addressLine1(defendantDTO.getAddress_line1())
                .addressLine2(defendantDTO.getAddress_line2())
                .addressLine3(defendantDTO.getAddress_line3())
                .addressLine4(defendantDTO.getAddress_line4())
                .addressLine5(defendantDTO.getAddress_line5())
                .postCode(defendantDTO.getPostcode())
                .nino(defendantDTO.getNino())
                .telephoneHome(defendantDTO.getTelephoneHome())
                .telephoneWork(defendantDTO.getTelephoneWork())
                .telephoneMobile(defendantDTO.getTelephoneMobile())
                .email1(defendantDTO.getEmail1())
                .email2(defendantDTO.getEmail2())
                .build();

        defendantRepository.save(defendantEntity);

    }

}
