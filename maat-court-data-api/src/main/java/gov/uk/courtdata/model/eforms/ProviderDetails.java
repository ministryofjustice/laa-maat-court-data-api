package gov.uk.courtdata.model.eforms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDetails {
    @JsonProperty("office_code")
    private String officeCode;

    @JsonProperty("provider_email")
    private String providerEmail;

    @JsonProperty("legal_rep_first_name")
    private String legalRepFirstName;

    @JsonProperty("legal_rep_last_name")
    private String legalRepLastName;

    @JsonProperty("legal_rep_telephone")
    private String legalRepTelephone;
}
