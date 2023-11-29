package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.UserType;
import gov.uk.courtdata.dto.application.UserDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class UserConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public UserDTO getDTO()
	{
		// TODO Auto-generated method stub
		return (UserDTO)this.getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public UserType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		UserType ret = null;
		
		if ( getDbType() == null )
		{
			setType( new UserType() );
		}
		
		if ( getDbType() instanceof UserType )
		{
			return (UserType)getDbType();
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTO(java.lang.Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException
	{
		if ( dto instanceof UserDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + UserDTO.class.getName());
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setTypeFromDTO(java.lang.Object)
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
			getOracleType().setUserName( 		convertorHelper.toString( 	getDTO().getUserName() ) );
			getOracleType().setFirstName( 		convertorHelper.toString( 	getDTO().getFirstName() ) );
			getOracleType().setInitials( 		convertorHelper.toString( 	getDTO().getInitials() ) );
			getOracleType().setLastName(  		convertorHelper.toString( 	getDTO().getSurname() ) );

			
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "CaseManagementUnitConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTOFromType(java.lang.Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException
	{
		// save it
		this.setType( oracleType );
		this.setDto( new UserDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setUserName( convertorHelper.toString( 		getOracleType().getUserName() ));
			getDTO().setFirstName( convertorHelper.toString( 		getOracleType().getFirstName() ));
			getDTO().setInitials( convertorHelper.toString( 		getOracleType().getInitials() ));
			getDTO().setSurname( convertorHelper.toString( 			getOracleType().getLastName() ));

		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "AreaConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}


}
