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
public class CapitalPropertyDTO extends GenericDTO
{

	   private Long						id;
	   private PropertyDTO				propertyDTO;
	   private AssessmentStatusDTO  	asssessmentStatus; 
	   private Date						dateEntered;   
	   private Date						verifiedDate;
	   private String					verifiedBy;
	   private Boolean					undeclared;
}
