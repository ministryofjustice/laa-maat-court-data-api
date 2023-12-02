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
public class TransferTypesDTO extends GenericDTO implements Comparable<TransferTypesDTO> 
{

	
	private	String				type;
	private	String				description;
	private int					seq;
	

	public int compareTo(TransferTypesDTO dto) {

		return type.compareTo(dto.getType() );
	}


}
