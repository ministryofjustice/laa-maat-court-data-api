package gov.uk.courtdata.contribution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionAppealDTO {

    @NotBlank
    private String caseType;

    @NotBlank
    private String appealType;

    @NotBlank
    private String outcome;

    @NotBlank
    private String assessmentResult;
}