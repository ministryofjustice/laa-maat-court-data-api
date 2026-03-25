package gov.uk.courtdata.model.reporder;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaatSearchResponse {
    private Integer maatId;
    @JsonProperty("isLinked")
    private boolean isLinked;
    private LinkingDetail linkingDetail;
}

