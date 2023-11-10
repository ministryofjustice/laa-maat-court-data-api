/*
 * Mark Whitaker  - 30/03/2017
 * 
 * Introduced as part of the Future Initiatives Project (FIP) to support interface
 * between application and database for digitised Means Assessments. Implements a 
 * DTO design pattern to transport relevant data.
 *
 */

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
public class DigitisedMeansAssessmentDTO extends GenericDTO{

	private Long					id;
	private Long					maatId;
	private Date					dateCreated;
	private Date					originalEmailDate;

	
}
