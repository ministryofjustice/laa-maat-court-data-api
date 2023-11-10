/**
 * 
 */
package gov.uk.courtdata.data.convertor;


import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.AddressType;
import gov.uk.courtdata.dto.application.AddressDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class AddressConvertor extends Convertor
{

	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public AddressDTO getDTO()
	{
		return (AddressDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public AddressType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new AddressType() );
		}
		
		if ( getDbType() instanceof AddressType )
		{
			return (AddressType)getDbType();
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
		if ( dto instanceof AddressDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + AddressDTO.class.getName());
	}

	/**
	 * Updates the local instance of the DTO by converting the data in the
	 * oracle type object passed as a parameter
	 * @see Convertor#setDTOFromType(Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException
	{
		// save it
		this.setType( oracleType );
		this.setDto( new AddressDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			boolean dirty = getDTO().getMDirty();	// must preserve the state of the dirty flag
			
			getDTO().setId(				convertorHelper.toLong( 	getOracleType().getId() ));
			getDTO().setLine1(			convertorHelper.toString(	getOracleType().getLine1() ));
			getDTO().setLine2(			convertorHelper.toString(	getOracleType().getLine2() ));
			getDTO().setLine3(			convertorHelper.toString(	getOracleType().getLine3() ));
			getDTO().setPostCode(		convertorHelper.toString(	getOracleType().getPostcode() ));
			getDTO().setCity(			convertorHelper.toString(	getOracleType().getCity() ));
			getDTO().setCounty(			convertorHelper.toString(	getOracleType().getCounty() ));
			getDTO().setCounty(			convertorHelper.toString(	getOracleType().getCounty() ));
			getDTO().setMDirty(dirty);	// Do not change the DTO dirty flag when it has just been populated from the database
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "AreaConvertor - the embedded dto is null");
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
			getOracleType().setId( 			convertorHelper.toLong( 	getDTO().getId() ) );
			getOracleType().setLine1(		convertorHelper.toString(	getDTO().getLine1() ));	
			getOracleType().setLine2(		convertorHelper.toString(	getDTO().getLine2() ));	
			getOracleType().setLine3(		convertorHelper.toString(	getDTO().getLine3() ));	
			getOracleType().setPostcode(	convertorHelper.toString(	getDTO().getPostCode() ));	
			getOracleType().setCity(		convertorHelper.toString(	getDTO().getCity() ));	
			getOracleType().setCounty(		convertorHelper.toString(	getDTO().getCounty() ));	
			getOracleType().setCountry(		convertorHelper.toString(	getDTO().getCountry() ));	
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "AddressConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
