package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CapitalEvidenceSummaryDTO extends GenericDTO {

	private Date	evidenceDueDate;
	private Date	evidenceReceivedDate;
	private Date	capitalAllowWithheldDate;
	private Date	capitalAllowReinstatedDate;
	private Date	firstReminderDate;
	private Date	secondReminderDate;
	private Double	capitalAllowance;
	private String	capitalNote;

}
