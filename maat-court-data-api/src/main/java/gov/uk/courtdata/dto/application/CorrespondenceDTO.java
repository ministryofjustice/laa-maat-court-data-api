package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CorrespondenceDTO extends GenericDTO {
    private Long id;
    private Long repId;
    private Long financialAssessmentId;
    private Long passportAssessmentId;
    private Date generatedDate;
    private Date lastPrintDate;
    private String templateName;
    private Date originalEmailDate;

    @Builder.Default
    private Collection<PrintDateDTO> printDateDTOs = new ArrayList<>();
    @Builder.Default
    private CorrespondenceTypeDTO correspondenceType = new CorrespondenceTypeDTO();
}
