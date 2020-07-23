package gov.uk.courtdata.model.hearing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CCOutComeData {

    private String ccooOutcome;
    private String appealType;
    private String caseEndDate;
}
