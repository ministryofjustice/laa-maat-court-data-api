package gov.uk.courtdata.reporder.gqlfilter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
