package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.HrProgressType;
import gov.uk.courtdata.dto.application.HRProgressDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class HRProgressConvertor extends Convertor {

	@Override
	public HRProgressDTO getDTO()
	{
		return (HRProgressDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public HrProgressType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new HrProgressType() );
		}
		
		if ( getDbType() instanceof HrProgressType )
		{
			return (HrProgressType)getDbType();
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
		if ( dto instanceof HRProgressDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " 
												+ dto.getClass().getName() 
												+ " instead of " 
												+ HRProgressDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new HRProgressDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setId( 			convertorHelper.toLong(	getOracleType().getId()));
			getDTO().setDateCompleted(	convertorHelper.toDate(getOracleType().getDateCompleted()));
			getDTO().setDateRequired(	convertorHelper.toDate(getOracleType().getDateRequired()));			
			getDTO().setDateRequested(	convertorHelper.toDate(getOracleType().getDateRequested()));			
			getDTO().setTimestamp(		convertorHelper.toZonedDateTime(getOracleType().getTimeStamp()));
			
			HRProgressActionConvertor progressActionConverter = new HRProgressActionConvertor();
			if(getOracleType().getProgressActionObject() != null)
			{			
				progressActionConverter.setDTOFromType(getOracleType().getProgressActionObject());				
			}
			getDTO().setProgressAction(progressActionConverter.getDTO());
			
			HRProgressResponseConvertor progressResponseConverter = new HRProgressResponseConvertor();
			if(getOracleType().getResponseRequiredObject() != null)
			{			
				progressResponseConverter.setDTOFromType(getOracleType().getResponseRequiredObject());				
			}
			getDTO().setProgressResponse(progressResponseConverter.getDTO());
		
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( this.getClass().getName() + " - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try
		{
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId(		 		convertorHelper.toLong(getDTO().getId()));		
            getOracleType().setDateCompleted(	convertorHelper.toDate(getDTO().getDateCompleted()));	
            getOracleType().setDateRequired(	convertorHelper.toDate(getDTO().getDateRequired()));	
            getOracleType().setDateRequested(	convertorHelper.toDate(getDTO().getDateRequested()));	
            getOracleType().setTimeStamp(	 	convertorHelper.toTimestamp(getDTO().getTimestamp()));
            
            HRProgressActionConvertor  progressActionConverter = new HRProgressActionConvertor();
			if(getDTO().getProgressAction() != null){				
				progressActionConverter.setTypeFromDTO(getDTO().getProgressAction());				
			}
			getOracleType().setProgressActionObject(progressActionConverter.getOracleType());
			
			HRProgressResponseConvertor progressResponseConverter = new HRProgressResponseConvertor();
			if(getDTO().getProgressResponse() != null){				
				progressResponseConverter.setTypeFromDTO(getDTO().getProgressResponse());				
			}
			getOracleType().setResponseRequiredObject(progressResponseConverter.getOracleType());

		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( this.getClass().getName() + " - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
