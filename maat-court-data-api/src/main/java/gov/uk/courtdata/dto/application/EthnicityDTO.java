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
public class EthnicityDTO extends GenericDTO
{
	private Long		id;
	private	String		level1;
	private String		level2;
	private String 		description;
	private Date		dateFrom;
	private Date		dateTo;

}
