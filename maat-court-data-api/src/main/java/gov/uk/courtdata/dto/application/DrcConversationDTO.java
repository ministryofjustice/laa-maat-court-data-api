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
public class DrcConversationDTO extends GenericDTO 
{
	private	long							rep_id;
	private DrcDTO							drcDTO;
	private	Collection<DrcProcessDTO> 		drcProcessStages;

}
