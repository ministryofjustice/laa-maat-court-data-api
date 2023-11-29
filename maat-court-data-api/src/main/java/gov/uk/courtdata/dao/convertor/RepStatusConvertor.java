/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.RepStatusType;
import gov.uk.courtdata.dto.application.RepStatusDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class RepStatusConvertor extends Convertor {

	/**
	 * return the DTO cast appropriately
	 * @see Convertor#getDTO()
	 */
	@Override
	public RepStatusDTO getDTO() {
		return (RepStatusDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public RepStatusType getOracleType() throws MAATApplicationException,
			MAATSystemException 
	{
		if ( getDbType() == null )
		{
			setType( new RepStatusType() );
		}
		
		if ( getDbType() instanceof RepStatusType )
		{
			return (RepStatusType)getDbType();
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
		if ( dto instanceof RepStatusDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + RepStatusDTO.class.getName());
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
		this.setDto( new RepStatusDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setStatus(				convertorHelper.toString(	getOracleType().getStatus() ));
			getDTO().setDescription(		convertorHelper.toString(	getOracleType().getDescription() ));
			getDTO().setUpdateAllowed(      convertorHelper.toBoolean(  getOracleType().getUpdateAllowed() 		));
			getDTO().setRemoveContribs(     convertorHelper.toBoolean(  getOracleType().getRemoveContribs() 		));
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "RepStatusConvertor - the embedded dto is null");
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
			getOracleType().setStatus(			convertorHelper.toString( 	getDTO().getStatus() ) );
			getOracleType().setDescription(		convertorHelper.toString(	getDTO().getDescription() ));	
			getOracleType().setUpdateAllowed(   convertorHelper.toBoolean(  getDTO().getUpdateAllowed()   )); 
			getOracleType().setRemoveContribs(  convertorHelper.toBoolean(  getDTO().getRemoveContribs()   )); 
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "RepStatusConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}


}
