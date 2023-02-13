package gov.uk.courtdata.model.eformsApplication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offence {
    private String name;

    @JsonProperty("offence_class")
    private String offenceClass;

    private List<FromDateToDate> dates;
}
