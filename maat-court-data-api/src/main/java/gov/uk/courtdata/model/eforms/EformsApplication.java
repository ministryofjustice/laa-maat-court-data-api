package gov.uk.courtdata.model.eforms;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.uk.courtdata.enums.FunctionType;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EformsApplication {
    private String id;

    @JsonProperty("schema_version")
    private BigDecimal schemaVersion;

    private Integer reference;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("submitted_at")
    private LocalDateTime submittedAt;

    @JsonProperty("date_stamp")
    private LocalDateTime dateStamp;

    // Transfer to ENUM
    private String status;

    @JsonProperty("ioj_passport")
    private List<String> iojPassport;

    @JsonProperty("provider_details")
    private ProviderDetails providerDetails;

    @JsonProperty("client_details")
    private ClientDetails clientDetails;

    @JsonProperty("case_details")
    private CaseDetails caseDetails;

    @JsonProperty("interests_of_justice")
    private List<InterestOfJustice> interestsOfJustice;
}
