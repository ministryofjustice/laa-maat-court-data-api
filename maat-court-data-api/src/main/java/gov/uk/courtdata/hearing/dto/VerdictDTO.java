package gov.uk.courtdata.hearing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerdictDTO {

    private String offenceId;
    private String verdictDate;
    private String category;
    private String categoryType;
    private String cjsVerdictCode;
    private String verdictCode;
}
