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
public class PropertyDTO extends GenericDTO
{
	private Long					id;
	private AddressDTO				addressDto;
	private ResidentialStatusDTO	residentialStatus;
	private PropertyTypeDTO			type;
	private	Double					percentageOwnedApplicant;
	private Double					percentageOwnedPartner;
	private String					bedrooms;
	private Currency                declaredMarketValue;
	private Currency				declaredMortgageCharges;
	private Currency				verifiedMarketValue;
	private Currency				verifiedMortgageCharges;
	private ResidentialStatusDTO	verifyResidentialStatus;
	private Boolean 				tenantInPlace;
	private Currency				verifiedEquityAmount;
	private Currency				declaredEquityAmount;
	private Collection<ThirdPartyOwnerDTO> thirdPartyOwners;

}
