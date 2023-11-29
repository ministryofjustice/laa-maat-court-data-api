package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.UserRoleTabType;
import gov.uk.courtdata.dao.oracle.UserRoleType;
import gov.uk.courtdata.dao.oracle.UserSessionType;
import gov.uk.courtdata.dto.application.UserDTO;
import gov.uk.courtdata.dto.application.UserRoleDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class UserSessionConvertor extends Convertor
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
	public UserSessionType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		UserSessionType ret = null;
		
		if ( getDbType() == null )
		{
			setType( new UserSessionType() );
		}
		
		if ( getDbType() instanceof UserSessionType )
		{
			return (UserSessionType)getDbType();
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
			getOracleType().setSurname(  		convertorHelper.toString( 	getDTO().getSurname() ) );
			getOracleType().setPassword(		convertorHelper.toString( 	getDTO().getPassword() ) );
			getOracleType().setUserSession(		convertorHelper.toString( 	getDTO().getUserSession() ) );
			getOracleType().setAppName(			convertorHelper.toString( 	getDTO().getAppName() ) );
			getOracleType().setAppServer(		convertorHelper.toString(	getDTO().getAppServer() ) );
	    	getOracleType().setPasswordExpiry( 	convertorHelper.toDate(	   	getDTO().getPasswordExpiry() ));
	    	getOracleType().setEnabled(  		convertorHelper.toBoolean( 	getDTO().isEnabled() ) );
			getOracleType().setLoggedIn(  		convertorHelper.toBoolean( 	getDTO().isLoggedIn() ) );
			getOracleType().setLocked(          convertorHelper.toBoolean( 	getDTO().isLocked() ) );
			getOracleType().setLoggingInAttempts(	convertorHelper.toInt( 	getDTO().getLoggingInAttempts() ));
			
			if ( getDTO().getAreaDTO() != null )
			{
				AreaConvertor areaConvertor = new AreaConvertor();
				areaConvertor.setTypeFromDTO( getDTO().getAreaDTO() );
				getOracleType().setAreaObject( areaConvertor.getOracleType() );
			}
			
			if ( ( getDTO().getUserRoles() != null ) && ( getDTO().getUserRoles().size() > 0 ) )
			{
				UserRoleType[]				uRoles 		= new UserRoleType[ getDTO().getUserRoles().size() ];
				Iterator<UserRoleDTO> 		roleIt		= getDTO().getUserRoles().iterator();
				
				UserRoleConvertor userRoleConvertor 	= new UserRoleConvertor();
				
				int idx = 0;
				while ( roleIt.hasNext() )
				{
					UserRoleDTO	userRoleDto = (UserRoleDTO)roleIt.next();
					userRoleConvertor.setTypeFromDTO( userRoleDto );
					uRoles[idx++] 		 	= (UserRoleType)userRoleConvertor.getDbType() ;	// get a type from the individual DTO
					
				}
				UserRoleTabType userRoleTabType = new UserRoleTabType( uRoles );
				
				getOracleType().setUserRoleTab( userRoleTabType );
			}
			
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
			getDTO().setSurname( convertorHelper.toString( 			getOracleType().getSurname() ));
			getDTO().setPassword( convertorHelper.toString( 		getOracleType().getPassword() ));
			getDTO().setUserSession( convertorHelper.toString( 		getOracleType().getUserSession() ));
			getDTO().setEnabled( convertorHelper.toBoolean( 		getOracleType().getEnabled() ));
			getDTO().setLocked( convertorHelper.toBoolean( 		    getOracleType().getLocked() ));
			getDTO().setLoggedIn( convertorHelper.toBoolean( 		getOracleType().getLoggedIn() ));	    	
			getDTO().setLoggingInAttempts( convertorHelper.toInt(  	getOracleType().getLoggingInAttempts() ));
			getDTO().setPasswordExpiry( convertorHelper.toDate(		getOracleType().getPasswordExpiry() ));	

			AreaConvertor areaConvertor	= new AreaConvertor();			
			areaConvertor.setDTOFromType(							getOracleType().getAreaObject() );
			getDTO().setAreaDTO(  areaConvertor.getDTO() );
			
	    	/*
	    	 * convert roles
	    	 */	    	
	    	UserRoleTabType userRoleTabType = getOracleType().getUserRoleTab();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setUserRoles( new HashSet<UserRoleDTO>() );	    		
	    	
	    	if ( userRoleTabType != null )
	    	{
	    		UserRoleType[] userRoleTypes 	=  userRoleTabType.getArray();
	    		
	    		UserRoleConvertor userRoleConvertor	= new UserRoleConvertor();
	    		
	    		for ( int i = 0; i < userRoleTypes.length ; i++ )
	    		{
	    			userRoleConvertor.setDTOFromType( userRoleTypes[i]  );
	    			getDTO().getUserRoles().add( userRoleConvertor.getDTO() );
	    		}
	    	}
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
