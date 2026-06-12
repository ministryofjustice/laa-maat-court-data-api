package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.WQDefendant;
import gov.uk.courtdata.hearing.dto.DefendantDTO;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQDefendantRepository;
import gov.uk.courtdata.util.DateUtil;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WQDefendantProcessor {

    private static final int TELEPHONE_HOME_MAX_LENGTH = 20;
    private static final int TELEPHONE_WORK_MAX_LENGTH = 25;
    private static final int TELEPHONE_MOBILE_MAX_LENGTH = 20;

    private static final int MIN_TELEPHONE_DIGITS = 7;

    private static final Pattern TELEPHONE_CANDIDATE_PATTERN = Pattern.compile("\\+?\\s*[0-9][0-9\\s().-]*");

    private static final Pattern NON_DIGIT_PATTERN = Pattern.compile("[^0-9]");

    private final WQDefendantRepository defendantRepository;

    public void process(final HearingDTO magsCourtDTO) {

        DefendantDTO defendantDTO = magsCourtDTO.getDefendant();

        WQDefendant defendantEntity = WQDefendant.builder()
                .caseId(magsCourtDTO.getCaseId())
                .txId(magsCourtDTO.getTxId())
                .forename(defendantDTO.getForename())
                .surname(defendantDTO.getSurname())
                .dateOfBirth(DateUtil.parse(defendantDTO.getDateOfBirth()))
                .addressLine1(defendantDTO.getAddressLine1())
                .addressLine2(defendantDTO.getAddressLine2())
                .addressLine3(defendantDTO.getAddressLine3())
                .addressLine4(defendantDTO.getAddressLine4())
                .addressLine5(defendantDTO.getAddressLine5())
                .postCode(defendantDTO.getPostcode())
                .nino(defendantDTO.getNino())
                .telephoneHome(telephoneNumberOnly(defendantDTO.getTelephoneHome(), TELEPHONE_HOME_MAX_LENGTH))
                .telephoneWork(telephoneNumberOnly(defendantDTO.getTelephoneWork(), TELEPHONE_WORK_MAX_LENGTH))
                .telephoneMobile(telephoneNumberOnly(defendantDTO.getTelephoneMobile(), TELEPHONE_MOBILE_MAX_LENGTH))
                .email1(defendantDTO.getEmail1())
                .email2(defendantDTO.getEmail2())
                .build();

        defendantRepository.save(defendantEntity);
    }

    private static String telephoneNumberOnly(final String rawTelephoneNumber, final int maxLength) {
        if (rawTelephoneNumber == null || rawTelephoneNumber.isBlank()) {
            return null;
        }

        Matcher matcher = TELEPHONE_CANDIDATE_PATTERN.matcher(rawTelephoneNumber.trim());

        while (matcher.find()) {
            String candidate = matcher.group();
            String digitsOnly = NON_DIGIT_PATTERN.matcher(candidate).replaceAll("");

            if (digitsOnly.length() < MIN_TELEPHONE_DIGITS) {
                continue;
            }

            String telephoneNumber = candidate.trim().startsWith("+") ? "+" + digitsOnly : digitsOnly;

            return telephoneNumber.length() <= maxLength ? telephoneNumber : null;
        }

        return null;
    }
}
