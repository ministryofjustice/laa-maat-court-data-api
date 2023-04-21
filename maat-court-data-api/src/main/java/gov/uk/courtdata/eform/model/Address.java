package gov.uk.courtdata.eform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @JsonProperty("lookup_id")
    private String lookupId;

    @JsonProperty("address_line_one")
    private String addressLineOne;

    @JsonProperty("address_line_two")
    private String addressLineTwo;

    private String city;

    private String country;

    private String postcode;
}
