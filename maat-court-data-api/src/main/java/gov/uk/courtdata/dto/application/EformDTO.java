package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EformDTO extends GenericDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7973253236109193131L;
	
	private Long maatRef;
	private Long usn;
	private String xmlDoc;


}
