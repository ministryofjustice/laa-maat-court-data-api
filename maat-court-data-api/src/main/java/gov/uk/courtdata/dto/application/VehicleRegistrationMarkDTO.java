/**
 * 
 */
package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class VehicleRegistrationMarkDTO extends GenericDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2856620006134321801L;
	
	private Long movId;
	private String VehicleRegistrationMark;
}
