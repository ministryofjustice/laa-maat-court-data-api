package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Verdict {

    private String offenceId;
    private String verdictDate;
    private String category;
    private String categoryType;
    private String cjsVerdictCode;
    private String verdictCode;
}
