package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ThirdPartyOwnerDTO extends GenericDTO {
	
	private Long	id;
	private Long	ropdId;
	private String ownerName;
	private String ownerRelation;
	private String otherRelation;
		
	private ThirdPartyTypeDTO thirdPartyType;
}
