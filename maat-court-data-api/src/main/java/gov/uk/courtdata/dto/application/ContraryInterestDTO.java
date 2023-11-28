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
public class ContraryInterestDTO extends GenericDTO implements Comparable<ContraryInterestDTO> 
{
	// Specific ref. dao code Constants...
	public static final String NO_CONTRARY_INTEREST = "NOCON";
	
	
	private	String				code;
	private	String				description;
	
	


	/**
	 * @param arg0
	 * @return
	 * @see String#compareTo(String)
	 */
	public int compareTo(ContraryInterestDTO dto) {
		
		return code.compareTo(dto.getCode() );
	}

}
