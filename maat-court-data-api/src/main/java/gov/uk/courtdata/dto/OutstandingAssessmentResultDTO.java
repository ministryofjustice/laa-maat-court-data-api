package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutstandingAssessmentResultDTO {
    @Builder.Default
    boolean outstandingAssessments = false;
    String message;
}
