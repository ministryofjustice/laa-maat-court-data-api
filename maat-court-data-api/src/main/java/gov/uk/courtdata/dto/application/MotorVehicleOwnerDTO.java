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
@EqualsAndHashCode(callSuper=false)
public class MotorVehicleOwnerDTO extends GenericDTO {
	private Long 		id;
	private Long 		repId;
	private Boolean 	noVehicleDeclared;
	private Boolean		available;

	@Builder.Default
	private Collection<VehicleRegistrationMarkDTO>  vehicleRegistrationMarkDTOs = new ArrayList<>();
}
