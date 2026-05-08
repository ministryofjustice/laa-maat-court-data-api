package gov.uk.courtdata.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class OutstandingAssessmentResultDTO {
    @Builder.Default
    boolean outstandingAssessments = false;

    String message;
}
