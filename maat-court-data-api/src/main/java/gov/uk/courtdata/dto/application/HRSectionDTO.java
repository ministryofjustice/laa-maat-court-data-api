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
public class HRSectionDTO extends GenericDTO {

    private HRDetailTypeDTO detailType;
    private Collection<HRDetailDTO> detail;
    private Currency applicantAnnualTotal;
    private Currency partnerAnnualTotal;
    private Currency annualTotal;

}
