package gov.uk.courtdata.eform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


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

    // TODO Transfer to ENUM
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
