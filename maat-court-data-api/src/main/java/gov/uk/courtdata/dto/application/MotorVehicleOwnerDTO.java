/**
 * 
 */
package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
public class MotorVehicleOwnerDTO extends GenericDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6525468902833698008L;
	
	
	private Long 		id;
	private Long 		repId;
	private Boolean 	noVehicleDeclared;
	private Boolean		available;
	
	private Collection<VehicleRegistrationMarkDTO>  vehicleRegistrationMarkDTOs;
}
