/**
 * 
 */
package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DrcDTO extends GenericDTO 
{

	private	DrcSupplierDTO						drcSupplier;
	private	Collection<DrcSupplierContactDTO>	contacts;

}
