/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.AppealsType;
import gov.uk.courtdata.dto.application.AppealDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author obod-l
 *
 */
public class AppealConvertor extends Convertor {

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public AppealDTO getDTO()
	{
		return (AppealDTO )getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public AppealsType getOracleType() throws MAATApplicationException,	MAATSystemException {

		AppealsType ret = null;
		
		if ( getDbType() == null )
		{
			setType( new AppealsType() );
		}
		
		if ( getDbType() instanceof AppealsType )
		{
			return (AppealsType)getDbType();
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
		if ( dto instanceof AppealDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " +  AppealDTO.class.getName());
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
		this.setDto( new  AppealDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setAvailable(convertorHelper.toBoolean(getOracleType().getAvailable()));
			
			getDTO().setAppealReceivedDate(convertorHelper.toDate(getOracleType().getAppealReceivedDate()));
			getDTO().setAppealSentenceOrderDate(convertorHelper.toDate(getOracleType().getAppealSentenceOrderDate()));
			getDTO().setAppealSentOrderDateSet(convertorHelper.toDate(getOracleType().getAppealSentOrdDtDateSet()));
	
			AppealTypeConvertor appealTypeConvertor = new AppealTypeConvertor();
			if(getOracleType().getAppealTypeObject() != null){
				
				appealTypeConvertor.setDTOFromType(getOracleType().getAppealTypeObject());
			}
			
			getDTO().setAppealTypeDTO(appealTypeConvertor.getDTO());
			
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "AppealConvertor - the embedded dto is null", nex);
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
            setDTO( dto );
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getOracleType().setAvailable(convertorHelper.toBoolean(getDTO().getAvailable()));
			
			getOracleType().setAppealReceivedDate(convertorHelper.toDate(getDTO().getAppealReceivedDate()));
			getOracleType().setAppealSentenceOrderDate(convertorHelper.toDate(getDTO().getAppealSentenceOrderDate()));
			getOracleType().setAppealSentOrdDtDateSet(convertorHelper.toDate(getDTO().getAppealSentOrderDateSet()));
		
			
			if(getDTO().getAppealTypeDTO() != null){
				
				AppealTypeConvertor  appealTypeConvertor = new AppealTypeConvertor();
				appealTypeConvertor.setTypeFromDTO(getDTO().getAppealTypeDTO());
				getOracleType().setAppealTypeObject(appealTypeConvertor.getOracleType());
			}
			
		}		
		catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "AppealConvertor - the embedded dto is null", nex );
		}
		catch (SQLException ex ){
			throw new MAATSystemException( ex );
		}
	}

}
