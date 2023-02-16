package gov.uk.courtdata.eformsApplication.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.courtdata.eformsApplication.dto.EformsStagingDTO;
import gov.uk.courtdata.model.eformsApplication.*;
import io.sentry.protocol.App;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EformsStagingBuilderTest {
    private static EformsStagingBuilder testBuilder;

    @BeforeAll
    public static void setUp() {
        testBuilder = new EformsStagingBuilder();
    }
    @Test
    void build() throws SQLException, JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime testDate = LocalDateTime.of(1, 1, 1, 1, 1);
        EformsApplication testApplication = EformsApplication.builder()
                .id("696dd4fd-b619-4637-ab42-a5f4565bcf4a")
                .schemaVersion(BigDecimal.valueOf(1.0))
                .reference(6000001)
                .createdAt(testDate)
                .submittedAt(testDate.plusHours(1))
                .dateStamp(testDate.plusHours(2))
                .status("submitted")
                .providerDetails(ProviderDetails.builder()
                        .officeCode("1A123B")
                        .providerEmail("provider@example.com")
                        .legalRepFirstName("John")
                        .legalRepLastName("Doe")
                        .legalRepTelephone("123456789")
                        .build())
                .clientDetails(ClientDetails.builder()
                        .applicant(Applicant.builder()
                                .firstName("Kit")
                                .lastName("Pound")
                                .nino("AJ123456C")
                                .dateOfBirth(LocalDate.parse("2011-06-09", formatter))
                                .telephoneNumber("07771231231")
                                .correspondenceAddressType("home_address")
                                .homeAddress(Address.builder()
                                        .addressLineOne("1 Road")
                                        .addressLineTwo("Village")
                                        .city("Some nice City")
                                        .country("United Kingdom")
                                        .postcode("SW1A 2AA")
                                        .build())
                                .build())
                        .build())
                .caseDetails(CaseDetails.builder()
                        .urn("123URN")
                        .caseType("appeal_to_crown_court")
                        .offences(List.of(
                                Offence.builder()
                                        .name("Attempt robbery")
                                        .offenceClass("Class C")
                                        .dates(List.of(
                                                FromDateToDate.builder()
                                                        .dateFrom(LocalDate.parse("2020-05-11", formatter))
                                                        .dateTo(LocalDate.parse("2020-05-12", formatter))
                                                        .build(),
                                                FromDateToDate.builder()
                                                        .dateFrom(LocalDate.parse("2020-08-11", formatter))
                                                        .build()
                                        )).build(),
                                Offence.builder()
                                        .name("Non-listed offence, manually entered")
                                        .dates(List.of(
                                                FromDateToDate.builder()
                                                        .dateFrom(LocalDate.parse("2020-09-15", formatter))
                                                        .build()
                                        )).build()
                        ))
                        .codefendants(List.of(
                                Defendant.builder()
                                        .firstName("Zoe")
                                        .lastName("Blogs")
                                        .conflictOfInterest("yes")
                                        .build()
                        ))
                        .heartingCourtName("Cardiff Magistrates' Court")
                        .hearingDate(LocalDate.parse("2024-11-11", formatter))
                        .build())
                .build();

        EformsStagingDTO result = testBuilder.build(testApplication);
        assertNotNull(result);
    }
}