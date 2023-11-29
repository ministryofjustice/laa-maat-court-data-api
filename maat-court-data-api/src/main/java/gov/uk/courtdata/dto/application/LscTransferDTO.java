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
public class LscTransferDTO extends AreaTransferDetailsDTO
{
	private Date                        dateLscReceived ;
	private Date                        dateLscReturned ;
	private UserDTO                      lscReceivedBy  ;
	private UserDTO                      lscReturnedBy  ;

}
