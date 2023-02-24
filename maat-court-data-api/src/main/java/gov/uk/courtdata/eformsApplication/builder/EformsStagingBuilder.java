package gov.uk.courtdata.eformsApplication.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import gov.uk.courtdata.eformsApplication.dto.EformsStagingDTO;
import gov.uk.courtdata.model.eformsApplication.*;
import gov.uk.courtdata.model.eformsApplication.xmlModels.FieldData;
import gov.uk.courtdata.model.eformsApplication.xmlModels.FormData;
import gov.uk.courtdata.model.eformsApplication.xmlModels.LaaAdded;
import gov.uk.courtdata.model.eformsApplication.xmlModels.ParentFormData;
import gov.uk.courtdata.model.eformsApplication.xmlModels.Row;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EformsStagingBuilder {

    private static final String APPLICATION_TYPE = "CRM14";
    private static final String OFFENCE_CLASS_PREFIX = "Class ";
    private static final String HOME_ADDRESS_TYPE = "home_address";

    public EformsStagingDTO build(EformsApplication eformsApplication) throws SQLException, JsonProcessingException {
        return EformsStagingDTO
                .builder()
                .usn(eformsApplication.getReference())
                .type(APPLICATION_TYPE)
                .xmlDoc(generateXmlString(eformsApplication))
                .build();
    }

    public String generateXmlString(EformsApplication eformsApplication) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JSR310Module());
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return xmlMapper.writeValueAsString(buildParentFormData(eformsApplication)).replace("wstxns1", "fd");
    }

    public ParentFormData buildParentFormData(EformsApplication eformsApplication) {
        return ParentFormData.builder().formData(buildFormData(eformsApplication)).build();
    }
    public FormData buildFormData(EformsApplication eformsApplication) {
        return FormData.builder().fieldData(buildFieldData(eformsApplication)).build();
    }

    public FieldData buildFieldData(EformsApplication eformsApplication) {
        ProviderDetails provider = eformsApplication.getProviderDetails();
        Applicant applicant = eformsApplication.getClientDetails().getApplicant();
        Address homeAddress = applicant.getHomeAddress();
        Address contactAddress = applicant.getCorrespondenceAddressType().equals(HOME_ADDRESS_TYPE) ?
            homeAddress : applicant.getCorrespondenceAddress();
        CaseDetails caseDetails = eformsApplication.getCaseDetails();

        return FieldData.builder()
                .formType(APPLICATION_TYPE)
                .dateReceived(eformsApplication.getCreatedAt())
                .datestampDate(eformsApplication.getDateStamp())
                .applicationType("New application") // hardcoded for testing
                .legalRepLaaAccount(provider.getOfficeCode())
                .legalRepFullName(String.format("%s %s", provider.getLegalRepFirstName(), provider.getLegalRepLastName()))
                .solicitorPhoneLandline(provider.getLegalRepTelephone())
                .forenames(applicant.getFirstName())
                .surname(applicant.getLastName())
                .niNumber(applicant.getNino())
                .dateOfBirth(toLocalDateTime(applicant.getDateOfBirth()))
                .phoneLandline(applicant.getTelephoneNumber())
                .haveHomeAddress("Yes") // hardcoded for testing
                .homeAddress1(homeAddress.getAddressLineOne())
                .homeAddress2(homeAddress.getAddressLineTwo())
                .homeAddress3(homeAddress.getCity())
                .homePostcode(homeAddress.getPostcode())
                .contactAddress1(contactAddress.getAddressLineOne())
                .contactAddress2(contactAddress.getAddressLineTwo())
                .contactAddress3(contactAddress.getCity())
                .contactPostcode(contactAddress.getPostcode())
                .urn(caseDetails.getUrn())
                .usn(eformsApplication.getReference().longValue())
                .chargesBrought(buildChargeList(eformsApplication.getCaseDetails().getOffences()))
                .offenceType(getOffenceType(eformsApplication.getCaseDetails().getOffences()))
                .laaAdded(LaaAdded.builder().caseType(caseDetails.getCaseType()).build())
                .disabled("No")// hardcoded for testing
                .codefendantsDetails(
                        caseDetails.getCodefendants().stream().map(defendant ->
                                        String.format("%s %s", defendant.getFirstName(), defendant.getLastName()))
                                .collect(Collectors.toList()))
                .build();
    }

    private List<Row> buildChargeList(List<Offence> offences) {

        List<Row> charges = new ArrayList<>();

        for (Offence offence : offences) {
            charges.addAll(
                    offence.getDates().stream().map(dates ->
                            Row.builder()
                                    .charge(offence.getName())
                                    .offenceWhen(dates.getDateTo() != null ? "BETWEEN" : "ON")
                                    .offenceDate1(toLocalDateTime(dates.getDateFrom()))
                                    .offenceDate2(toLocalDateTime(dates.getDateTo()))
                                    .build()).collect(Collectors.toList())
            );
        }
        return charges;
    }

    private String getOffenceType(List<Offence> offences) {
        List<Offence> offencesWithClass = offences.stream()
                .filter(item -> item.getOffenceClass() != null &&
                        item.getOffenceClass().startsWith(OFFENCE_CLASS_PREFIX)).sorted(Comparator.comparing(
                        offence -> offence.getOffenceClass().charAt(offence.getOffenceClass().length() - 1))).collect(Collectors.toList());
        return offencesWithClass.get(0).getOffenceClass();
    }

    private LocalDateTime toLocalDateTime(LocalDate date) {
        return date == null ? null:date.atStartOfDay();
    }
}
