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
public class AreaTransferDetailsDTO extends GenericDTO
{

	private Long						id ;
	
	private AreaDTO                     areaFrom ;
	private AreaDTO                     areaTo ;
	private CaseManagementUnitDTO       cmuFrom ;
	private CaseManagementUnitDTO       cmuTo ;
	private TransferTypeDTO             transferType ;
	private TransferStatusDTO			transferStatus;
	
	private Date                        dateHmcsSent ;
	private Date                        dateHmcsReceived;
	private UserDTO                     hmcsSentBy     ;
	private UserDTO                     hmcsReceivedBy ;

}
