package gov.uk.courtdata.model.eformsApplication.xmlModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Row {
    @JsonProperty("Charge")
    protected String charge;
    @JsonProperty("Offence_when")
    protected String offenceWhen;
    @JsonProperty("Offence_date_1")
    protected LocalDateTime offenceDate1;
    @JsonProperty("Offence_date_2")
    protected LocalDateTime offenceDate2;
}
