package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IOJAppealDTO extends GenericDTO {
    private Long iojId;
    private Long cmuId;
    private Date receivedDate;
    private Date decisionDate;
    private String appealSetUpResult;
    private String appealDecisionResult;
    private String notes;

    @Builder.Default
    private IOJDecisionReasonDTO appealReason = new IOJDecisionReasonDTO();
    @Builder.Default
    private AssessmentStatusDTO assessmentStatusDTO = new AssessmentStatusDTO();
    @Builder.Default
    private NewWorkReasonDTO newWorkReasonDTO = new NewWorkReasonDTO();

}