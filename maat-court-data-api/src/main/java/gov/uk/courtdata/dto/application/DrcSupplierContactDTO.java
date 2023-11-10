/**
 * 
 */
package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DrcSupplierContactDTO extends GenericDTO 
{
	long				id;
	long				supplierId;
	String				firstName;
	String				lastName;
	String				title;
	String				telephone;
	String				status;
	String				description;
}
