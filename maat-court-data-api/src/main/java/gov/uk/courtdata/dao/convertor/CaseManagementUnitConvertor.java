/**
 * 08/05/2009 SWA-D  remove the circular dependency of the area dto, as the area dto has a collection of the 
 * cmu dto in the first place.
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.CmuType;
import gov.uk.courtdata.dto.application.CaseManagementUnitDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;



/**
 * Represents a case management unit. There will be multiple Case Management Units or 
 * LJAs  associated with an area.
 * 
 * @author OBOD-L
 *
 */
public class CaseManagementUnitConvertor extends Convertor
{	
	public CaseManagementUnitConvertor() {
	
	}

	  
	/**
	 * This will create a local instance of the DTO appropriate for this convertor
	 * @see Convertor#getDto()
	 */
	@Override
	public CaseManagementUnitDTO getDTO()
	{
		return (CaseManagementUnitDTO)this.getDto();
	}



	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDto(java.lang.Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException
	{
		if ( dto instanceof CaseManagementUnitDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + CaseManagementUnitDTO.class.getName());
	}



	/**
	 * This will create a local instance of the OracleType appropriate for this convertor
	 */
	@Override
	public CmuType getOracleType() {

		if ( getDbType() == null )
		{
			setType( new CmuType() );
		}
		
		if ( getDbType() instanceof CmuType )
		{
			return (CmuType)getDbType();
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
	 * This method will return the embedded oracle type object having first updated
	 * it from the corresponding dto attributes.
	 * @see uk.gov.lsc.maat.common.dto.GenericDTO#getTypeFromDTO()
	 */
	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException, MAATSystemException
	{
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try
		{
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId( 			convertorHelper.toLong(		getDTO().getCmuId() ) );
			getOracleType().setAreaId(		convertorHelper.toLong(		getDTO().getAreaId() ));
			getOracleType().setCode(		convertorHelper.toString(	getDTO().getCode() ));
			getOracleType().setDescription( convertorHelper.toString(	getDTO().getDescription() ));
			getOracleType().setEnabled(		convertorHelper.toBoolean(	getDTO().getEnabled() ));
			getOracleType().setHasLibra(	convertorHelper.toBoolean(	getDTO().getLibraAccess() ));
			getOracleType().setTimeStamp(	convertorHelper.toDate(		getDTO().getTimeStamp() ));	
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "CaseManagementUnitConvertor - dto object has not been set");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}


	/**
	 * This method stores an instance of the associated oracle type and runs a 
	 * type conversion to populate the dto attributes.
	 * @see uk.gov.lsc.maat.bus.data.dao.dto.GenericDTO#loadFromType(Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)  throws MAATApplicationException, MAATSystemException
	{
		// save it
		this.setType( oracleType );
		this.setDto( new CaseManagementUnitDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setCmuId(			convertorHelper.toLong( 	getOracleType().getId() ));
			getDTO().setAreaId(			convertorHelper.toLong( 	getOracleType().getAreaId() ));
			getDTO().setCode(			convertorHelper.toString(	getOracleType().getCode() ));
			getDTO().setDescription(	convertorHelper.toString(	getOracleType().getDescription() ));
			getDTO().setEnabled(		convertorHelper.toBoolean(	getOracleType().getEnabled() ));
			getDTO().setLibraAccess(	convertorHelper.toBoolean(	getOracleType().getHasLibra() ));
			getDTO().setTimeStamp(		convertorHelper.toDate(		getOracleType().getTimeStamp()	));
	    	
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "CaseManagementUnitConvertor - dto object has not been set");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}

}
