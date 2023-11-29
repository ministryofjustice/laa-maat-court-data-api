/**
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
public class ApplicantLinkDTO extends GenericDTO
{

	private	Long					id;
	private Date					linked;
	private Date					unlinked;
	ApplicantDTO					partnerDTO;
	
}
