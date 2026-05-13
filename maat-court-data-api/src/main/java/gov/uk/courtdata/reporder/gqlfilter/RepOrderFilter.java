package gov.uk.courtdata.reporder.gqlfilter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepOrderFilter {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("sentenceOrderDate")
    private String sentenceOrderDate;
}
