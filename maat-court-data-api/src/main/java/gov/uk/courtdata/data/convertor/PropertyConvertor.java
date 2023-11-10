/**
 *
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.PropertyType;
import gov.uk.courtdata.data.oracle.ThirdPartyTabtype;
import gov.uk.courtdata.data.oracle.ThirdPartyType;
import gov.uk.courtdata.dto.application.PropertyDTO;
import gov.uk.courtdata.dto.application.ThirdPartyOwnerDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class PropertyConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public PropertyDTO getDTO()
	{
		return (PropertyDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public PropertyType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new PropertyType() );
		}

		if ( getDbType() instanceof PropertyType )
		{
			return (PropertyType)getDbType();
		}
		else
		{
			/*
			 * fatal error ???? write a handler in the GenericDTO
			 */
			//throw new DAOApplicationException( Constants.INVALID_DTO_TYPE_CLASS );
			return null;  // temp fix, could cause null pointer exception
		}
	}


	/**
	 * sets the local instance of the dto
	 * @see Convertor#setDTO(Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException
	{
		if ( dto instanceof PropertyDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + PropertyDTO.class.getName());
	}


	/**
	 * Updates the local instance of the DTO by converting the data in the
	 * oracle type object passed as a parameter
	 * @see Convertor#setDTOFromType(Object)
	 *
	 * CREATE OR REPLACE TYPE TOGDATA.property_type    as object
(
   id                           number(10),
   address_object               address_type,
   residential_status_object    residential_status_type,
   property_type_object         property_type_type,
   PERCENTAGE_OWNED_APPLICANT   number(5,2),
   PERCENTAGE_OWNED_PARTNER     number(5,2),
   BEDROOMS                     varchar2(10),
   current_value                number(10,2),
   equity_amount                number(10,2),
   DECLARED_MARKET_VALUE             number(10,2),
   DECLARED_MORTGAGE_CHARGES            number(10,2),
   VERIFIED_MARKET_VALUE            number(10,2),
   VERIFIED_MORTGAGE_CHARGES             number(10,2),
   verified_residential_status  residential_status_type,
   tennant_in_place             varchar2(1),
   time_stamp                   timestamp
)
	 *
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException
	{
		// save it
		this.setType( oracleType );
		this.setDto( new PropertyDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setTimestamp(					getOracleType().getTimeStamp() );
			getDTO().setId(							convertorHelper.toLong( 	getOracleType().getId() ));
			getDTO().setBedrooms(					convertorHelper.toString(	getOracleType().getBedrooms() ));

			getDTO().setPercentageOwnedApplicant(	convertorHelper.toDouble(	getOracleType().getPercentageOwnedApplicant() ));
			getDTO().setPercentageOwnedPartner(		convertorHelper.toDouble(	getOracleType().getPercentageOwnedPartner() ));
			getDTO().setDeclaredMortgageCharges(	convertorHelper.toCurrency(	getOracleType().getDeclaredMortgageCharges() ));
			getDTO().setDeclaredMarketValue(		convertorHelper.toCurrency(	getOracleType().getDeclaredMarketValue() ));
			getDTO().setVerifiedMortgageCharges(	convertorHelper.toCurrency(	getOracleType().getVerifiedMortgageCharges() ));
			getDTO().setVerifiedMarketValue(		convertorHelper.toCurrency(	getOracleType().getVerifiedMarketValue() ));
			getDTO().setVerifiedEquityAmount(		convertorHelper.toCurrency(	getOracleType().getVerifiedEquityAmount()));
			getDTO().setDeclaredEquityAmount(		convertorHelper.toCurrency(	getOracleType().getDeclaredEquityAmount()));
			getDTO().setTenantInPlace(	convertorHelper.toBoolean(	getOracleType().getTennantInPlace() ));

			ResidentialStatusConvertor vrsConvertor = new ResidentialStatusConvertor();
			if(getOracleType().getVerifiedResidentialStatus() != null){
				vrsConvertor.setDTOFromType(getOracleType().getVerifiedResidentialStatus());
			}
			getDTO().setVerifyResidentialStatus(vrsConvertor.getDTO());

			AddressConvertor 	adConvertor	= new AddressConvertor();
			if ( getOracleType().getAddressObject() != null )
			{
				adConvertor.setDTOFromType(getOracleType().getAddressObject());
			}
			getDTO().setAddressDto(  adConvertor.getDTO() );


			ResidentialStatusConvertor rsConvertor = new ResidentialStatusConvertor();
			if ( getOracleType().getResidentialStatusObject() != null )
			{
				rsConvertor.setDTOFromType( getOracleType().getResidentialStatusObject() );
			}
			getDTO().setResidentialStatus( rsConvertor.getDTO() );

			PropertyTypeConvertor convertor = new PropertyTypeConvertor();
			if ( getOracleType().getPropertyTypeObject() != null )
			{
				convertor.setDTOFromType( getOracleType().getPropertyTypeObject() );
			}
			getDTO().setType( convertor.getDTO() );
			
			
						
			/*
	    	 * set an empty collection
	    	 */
			
			getDTO().setThirdPartyOwners(new ArrayList<ThirdPartyOwnerDTO>() );
			
			
			if ( getOracleType().getThirdPartyOwners() != null ){
		
				ThirdPartyTabtype thirdPartyTabtype = getOracleType().getThirdPartyOwners();
		
				ThirdPartyType[] thirdPartyTypeArray = thirdPartyTabtype.getArray();
				
				ThirdPartyOwnerConvertor thirdPartyOwnerConvertor = new ThirdPartyOwnerConvertor();
				
				for ( int i = 0; i < thirdPartyTypeArray.length; i++ ){
					
					thirdPartyOwnerConvertor.setDTOFromType( thirdPartyTypeArray[i] );
					getDTO().getThirdPartyOwners().add(thirdPartyOwnerConvertor.getDTO());
				}				
			}
			
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */

			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}


	/**
	 * Updates the local instance of the Oracle type by converting the data in the
	 * dto object passed as a parameter
	 * @see Convertor#setTypeFromDTO(Object)
	 */
	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException
	{
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try
		{
			setType( null );	// force new type to be instantiated
            setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getOracleType().setTimeStamp(					getDTO().getTimestamp() );
			getOracleType().setId(							convertorHelper.toLong(		getDTO().getId() ));

			getOracleType().setBedrooms(					convertorHelper.toString(getDTO().getBedrooms()	 ));

			getOracleType().setPercentageOwnedApplicant(	convertorHelper.toDouble(	getDTO().getPercentageOwnedApplicant() ));
			getOracleType().setPercentageOwnedPartner(		convertorHelper.toDouble(	getDTO().getPercentageOwnedPartner() ));
			getOracleType().setDeclaredMortgageCharges(		convertorHelper.toCurrency(	getDTO().getDeclaredMortgageCharges() ));
			getOracleType().setDeclaredMarketValue(			convertorHelper.toCurrency(	getDTO().getDeclaredMarketValue() ));
			getOracleType().setVerifiedMortgageCharges(		convertorHelper.toCurrency(	getDTO().getVerifiedMortgageCharges() ));
			getOracleType().setVerifiedMarketValue(			convertorHelper.toCurrency(	getDTO().getVerifiedMarketValue() ));
			getOracleType().setTennantInPlace(				convertorHelper.toBoolean(	getDTO().getTenantInPlace() ));
			getOracleType().setVerifiedEquityAmount(		convertorHelper.toCurrency(	getDTO().getVerifiedEquityAmount()));
			getOracleType().setDeclaredEquityAmount(		convertorHelper.toCurrency(	getDTO().getDeclaredEquityAmount()));


			ResidentialStatusConvertor vrsConvertor = new ResidentialStatusConvertor();
			if(getDTO().getVerifyResidentialStatus() != null){
				vrsConvertor.setTypeFromDTO(getDTO().getVerifyResidentialStatus());
			}
			getOracleType().setVerifiedResidentialStatus(vrsConvertor.getOracleType());

			AddressConvertor 	adConvertor	= new AddressConvertor();
			if ( getDTO().getAddressDto() != null )
			{
				adConvertor.setTypeFromDTO( getDTO().getAddressDto() );
			}
			getOracleType().setAddressObject( adConvertor.getOracleType() );

			ResidentialStatusConvertor rsConvertor = new ResidentialStatusConvertor();
			if ( getDTO().getResidentialStatus() != null )
			{
				rsConvertor.setTypeFromDTO( getDTO().getResidentialStatus() );
			}
			getOracleType().setResidentialStatusObject( rsConvertor.getOracleType() );

			PropertyTypeConvertor convertor = new PropertyTypeConvertor();
			if ( getDTO().getType() != null )
			{
				convertor.setTypeFromDTO( getDTO().getType() );
			}
			getOracleType().setPropertyTypeObject(   convertor.getOracleType() );
			
			
			if ( ( getDTO().getThirdPartyOwners() != null ) && ( getDTO().getThirdPartyOwners()).size() > 0 ) {

				ThirdPartyType[] thirdPartyTypes = new ThirdPartyType[ getDTO().getThirdPartyOwners().size()];  
				Iterator<ThirdPartyOwnerDTO> thirdParties = getDTO().getThirdPartyOwners().iterator();  

				int idx = 0;
				
				while ( thirdParties.hasNext() ){
			
					ThirdPartyOwnerDTO thirdPartyOwnerDTO = (ThirdPartyOwnerDTO)thirdParties.next();
                    ThirdPartyOwnerConvertor thirdPartyOwnerConvertor = new ThirdPartyOwnerConvertor();
                    thirdPartyOwnerConvertor.setTypeFromDTO(thirdPartyOwnerDTO);
            		
                    thirdPartyTypes[idx++]  = thirdPartyOwnerConvertor.getOracleType();
            
				}
				
				getOracleType().setThirdPartyOwners(new ThirdPartyTabtype(thirdPartyTypes));
				
			}else{
				
				getOracleType().setThirdPartyOwners(new ThirdPartyTabtype());
			}

		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */

			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
