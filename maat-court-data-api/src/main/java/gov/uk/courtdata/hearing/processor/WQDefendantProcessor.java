package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQDefendant;
import gov.uk.courtdata.hearing.magistrate.dto.DefendantDTO;
import gov.uk.courtdata.hearing.magistrate.dto.MagistrateCourtDTO;
import gov.uk.courtdata.repository.WQDefendantRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WQDefendantProcessor {

    private final WQDefendantRepository defendantRepository;

    public void process(final MagistrateCourtDTO magsCourtDTO) {

        DefendantDTO defendantDTO = magsCourtDTO.getDefendant();

        WQDefendant defendantEntity = WQDefendant.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .forename(defendantDTO.getForename())
                .surname(defendantDTO.getSurname())
                .dateOfBirth(DateUtil.toDate(defendantDTO.getDateOfBirth()))
                .address_line1(defendantDTO.getAddress_line1())
                .address_line2(defendantDTO.getAddress_line2())
                .address_line3(defendantDTO.getAddress_line3())
                .address_line4(defendantDTO.getAddress_line4())
                .address_line5(defendantDTO.getAddress_line5())
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
