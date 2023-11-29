package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.UserQueryType;
import gov.uk.courtdata.dto.application.SearchUserDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class UserQueryConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public SearchUserDTO getDTO()
	{
		return (SearchUserDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public UserQueryType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new UserQueryType() );
		}
		
		if ( getDbType() instanceof UserQueryType )
		{
			return (UserQueryType)getDbType();
		}
		else
		{
			return null;
		}
	}


	/**
	 * sets the local instance of the dto
	 * @see Convertor#setDTO(Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException
	{
		if ( dto instanceof SearchUserDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + SearchUserDTO.class.getName());
	}


	/**
	 * Updates the local instance of the DTO by converting the dao in the
	 * oracle type object passed as a parameter
	 * @see Convertor#setDTOFromType(Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException
	{
		// save it
		this.setType( oracleType );
		this.setDto( new SearchUserDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setFirstName(		convertorHelper.toString(	getOracleType().getFirstName()));
			getDTO().setSurname(		convertorHelper.toString(	getOracleType().getSurname()  ));
			getDTO().setUserName(		convertorHelper.toString(	getOracleType().getUserName() ));
			getDTO().setAreaID(			convertorHelper.toLong(		getOracleType().getAreaId()   ));
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "MagsCourtConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}


	/**
	 * Updates the local instance of the Oracle type by converting the dao in the
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
			getOracleType().setFirstName(	convertorHelper.toString( 	getDTO().getFirstName() ) );
			getOracleType().setSurname(		convertorHelper.toString(	getDTO().getSurname() ));	
			getOracleType().setUserName(	convertorHelper.toString(	getDTO().getUserName() ));	
			getOracleType().setAreaId(		convertorHelper.toLong(	getDTO().getAreaID() ));	
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "MagsCourtConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
