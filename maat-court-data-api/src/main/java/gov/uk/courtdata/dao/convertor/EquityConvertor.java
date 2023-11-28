/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.EquityType;
import gov.uk.courtdata.dao.oracle.PropertyType;
import gov.uk.courtdata.dto.application.EquityDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class EquityConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public EquityDTO getDTO()
	{
		return (EquityDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public EquityType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new EquityType() );
		}
		
		if ( getDbType() instanceof EquityType )
		{
			return (EquityType)getDbType();
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
		if ( dto instanceof EquityDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + EquityDTO.class.getName());
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
		this.setDto( new EquityDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setId(						convertorHelper.toLong(		getOracleType().getId()));
			getDTO().setVerifiedBy(				convertorHelper.toString( 	getOracleType().getVerifiedBy()    	));
			getDTO().setDateEntered(			convertorHelper.toDate( 	getOracleType().getDateEntered() 	));
			getDTO().setVerifiedDate(			convertorHelper.toDate( 	getOracleType().getVerifiedDate() 	));
			getDTO().setUndeclared(				convertorHelper.toBoolean(getOracleType().getUndeclared()));
			
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getOracleType().getAssetStatusObject() != null )
			{
				assConvertor.setDTOFromType( getOracleType().getAssetStatusObject() );
			}
			getDTO().setAssessmentStatus( assConvertor.getDTO() ); // if type is null this will initialise a valid empty object
			
			
			PropertyType	propertyType		= getOracleType().getPropertyObject();
			
			if ( propertyType != null )
			{
				PropertyConvertor 	convertor	= new PropertyConvertor();
				convertor.setDTOFromType(propertyType);
				getDTO().setPropertyDTO( convertor.getDTO() );
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
			getOracleType().setId(						convertorHelper.toLong(		getDTO().getId()));
			getOracleType().setVerifiedBy(				convertorHelper.toString( 	getDTO().getVerifiedBy()    	    ));
			getOracleType().setDateEntered(				convertorHelper.toDate( 	getDTO().getDateEntered() 	        ));
			getOracleType().setVerifiedDate(			convertorHelper.toDate( 	getDTO().getVerifiedDate() 	        ));
			getOracleType().setUndeclared(				convertorHelper.toBoolean(getDTO().getUndeclared()));
			
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getDTO().getAssessmentStatus() != null )
			{
				assConvertor.setTypeFromDTO( getDTO().getAssessmentStatus() );
			}
			getOracleType().setAssetStatusObject( assConvertor.getOracleType() ); // if type is null this will initialise a valid empty object
			
			if ( getDTO().getPropertyDTO() != null )
			{
				PropertyConvertor 	convertor	= new PropertyConvertor();
				convertor.setTypeFromDTO( getDTO().getPropertyDTO() );
				getOracleType().setPropertyObject( convertor.getOracleType() );
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
