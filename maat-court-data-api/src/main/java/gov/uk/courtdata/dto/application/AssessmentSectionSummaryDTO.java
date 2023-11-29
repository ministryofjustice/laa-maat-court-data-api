package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AssessmentSectionSummaryDTO extends GenericDTO {

    private String section;
    private Double applicantAnnualTotal;
    private Double partnerAnnualTotal;
    private Double annualTotal;
    private Collection<AssessmentDetailDTO> assessmentDetail;

}
