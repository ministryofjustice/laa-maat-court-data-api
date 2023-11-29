package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CorrespondenceDTO extends GenericDTO {
    private Long id;
    private Long repId;
    private Long financialAssessmentId;
    private Long passportAssessmentId;
    private Date generatedDate;
    private Date lastPrintDate;
    private CorrespondenceTypeDTO correspondenceType;
    private String templateName;
    private Date originalEmailDate; // MW - 30/03/2017 - FIP Changes

    private Collection<PrintDateDTO> printDateDTOs;

}
