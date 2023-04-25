package gov.uk.courtdata.eform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseDetails {
    private String urn;

    @JsonProperty("case_type")
    private String caseType;

    @JsonProperty("appeal_maat_id")
    private String appealMaatId;

    private List<Offence> offences;

    private List<Defendant> codefendants;

    @JsonProperty("hearing_court_name")
    private String heartingCourtName;

    @JsonProperty("hearing_date")
    private LocalDate hearingDate;
}