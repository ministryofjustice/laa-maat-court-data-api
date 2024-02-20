/**
 * 
 */
package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllowedWorkReasonDTO extends GenericDTO  {
	@Builder.Default
	private Collection<NewWorkReasonDTO> iojAppealWorkReason = new ArrayList<>();
	@Builder.Default
	private Collection<NewWorkReasonDTO> magsCourtHardshipWorkReason = new ArrayList<>();
	@Builder.Default
	private Collection<NewWorkReasonDTO> passportWorkReason = new ArrayList<>();
	@Builder.Default
	private Collection<NewWorkReasonDTO> meansAssessmentWorkReason = new ArrayList<>();
	@Builder.Default
	private Collection<NewWorkReasonDTO> crownCourtHardshipWorkReason = new ArrayList<>();
	@Builder.Default
	private Collection<NewWorkReasonDTO> eligibilityWorkReason = new ArrayList<>();
}
