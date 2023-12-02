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
public class DisabilityStatementDTO extends GenericDTO {
	private static final long serialVersionUID = -4873867127221639811L;

	public static final String DECLARED_DISABLED = "DECLARED_DISABLED";
	public static final String NOT_DISABLED = "NOT_DISABLED";
	
	private String				code;
	private String				description;

}
