package gov.uk.courtdata.model.eformsApplication.xmlModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "row")
public class Charge {
    @JsonProperty("Charge")
    protected String charge;
    @JsonProperty("Offence_when")
    protected String offenceWhen;
    @JsonProperty("Offence_date_1")
    protected LocalDateTime offenceDate1;
    @JsonProperty("Offence_date_2")
    protected LocalDateTime offenceDate2;
}
