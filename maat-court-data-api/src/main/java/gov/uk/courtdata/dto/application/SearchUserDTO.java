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
public class SearchUserDTO extends GenericDTO 
{
	private String 				firstName;
	private String				surname;
	private String				userName;
	private Long				areaID;
	
}
