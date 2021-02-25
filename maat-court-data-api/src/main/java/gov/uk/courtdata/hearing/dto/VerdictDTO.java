package gov.uk.courtdata.hearing.dto;

import gov.uk.courtdata.enums.VerdictCategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerdictDTO {

    private String offenceId;
    private String verdictDate;
    private String category;
    private VerdictCategoryType categoryType;
    private String cjsVerdictCode;
    private String verdictCode;
}