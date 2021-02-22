package gov.uk.courtdata.model;


import gov.uk.courtdata.enums.VerdictCategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Verdict {

    private String offenceId;
    private String verdictDate;
    private String category;
    private VerdictCategoryType categoryType;
    private String cjsVerdictCode;
    private String verdictCode;
}