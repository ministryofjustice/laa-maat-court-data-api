package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.IOJType;
import gov.uk.courtdata.dto.application.IOJAppealDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class IojAppealConvertor extends Convertor {

	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public IOJAppealDTO getDTO(){
		return (IOJAppealDTO)this.getDto();
	}
	
	/**
	 * sets the local instance of the dto
	 * @see Convertor#setDTO(Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException	{
		if (dto instanceof IOJAppealDTO)
			this.setDto(dto);
		else
			throw new MAATApplicationException(" Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + IOJAppealDTO.class.getName());
	}
	
	@Override
	public IOJType getOracleType() throws MAATApplicationException, MAATSystemException{

		if ( getDbType() == null ){
			setType( new IOJType() );
		}
		
		if ( getDbType() instanceof IOJType ){
			return (IOJType)getDbType();
		}else{
			return null;
		}
	}	

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTOFromType(java.lang.Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException 
	{

		this.setType(oracleType);
		this.setDto( new IOJAppealDTO() );	// create the new DTO
	    
	    try
	    {
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setTimestamp(						convertorHelper.toZonedDateTime(getOracleType().getTimeStamp()));

			getDTO().setIojId(					convertorHelper.toLong( 	getOracleType().getId() ));
			getDTO().setCmuId(					convertorHelper.toLong( 	getOracleType().getCmuId() ));
			getDTO().setReceivedDate(			convertorHelper.toDate( 	getOracleType().getReceivedDate() ));
			getDTO().setDecisionDate(			convertorHelper.toDate( 	getOracleType().getDecisionDate() ));
			getDTO().setAppealSetUpResult(		convertorHelper.toString( 	getOracleType().getAppealSetupResult()));
			getDTO().setAppealDecisionResult(	convertorHelper.toString( 	getOracleType().getAppealDecisionResult() ));
			getDTO().setNotes(					convertorHelper.toString( 	getOracleType().getNotes() ));
			
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getOracleType().getAppealStatus() != null )
			{
				assConvertor.setDTOFromType( getOracleType().getAppealStatus() );
			}					
			getDTO().setAssessmentStatusDTO( assConvertor.getDTO() );
			
			IojDecisionReasonConvertor iojDrConvertor	= new IojDecisionReasonConvertor();
			if ( getOracleType().getDecisionReasonObject() != null )
			{
				iojDrConvertor.setDTOFromType( getOracleType().getDecisionReasonObject() );
			}
			getDTO().setAppealReason( iojDrConvertor.getDTO() );
			
			NewWorkReasonConvertor convertor	= new NewWorkReasonConvertor();
			if ( getOracleType().getNewWorkReasonObject() != null )
			{
				convertor.setDTOFromType( getOracleType().getNewWorkReasonObject() );
			}
			getDTO().setNewWorkReasonDTO( convertor.getDTO() );
			
		}catch (NullPointerException nex){
			throw new MAATApplicationException( "IOJAppealConvertor - the embedded dto is null");
		
		}catch (SQLException ex){
			throw new MAATSystemException( ex );
		}	
		
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setTypeFromDTO(java.lang.Object)
	 */
	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try{	
			setType( null );	// force new type to be instantiated 
			setDTO(dto);	
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getOracleType().setTimeStamp(				convertorHelper.toTimestamp(getDTO().getTimestamp()));

			getOracleType().setId(						convertorHelper.toLong( 	getDTO().getIojId() ));
			getOracleType().setCmuId(					convertorHelper.toLong( 	getDTO().getCmuId() ));
			getOracleType().setReceivedDate(			convertorHelper.toDate( 	getDTO().getReceivedDate() ));
			getOracleType().setDecisionDate(			convertorHelper.toDate( 	getDTO().getDecisionDate() ));
			getOracleType().setAppealSetupResult(		convertorHelper.toString( 	getDTO().getAppealSetUpResult()));
			getOracleType().setAppealDecisionResult(	convertorHelper.toString( 	getDTO().getAppealDecisionResult() ));
			getOracleType().setNotes(					convertorHelper.toString( 	getDTO().getNotes() ));
			
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getDTO().getAssessmentStatusDTO() != null )
			{
				assConvertor.setTypeFromDTO( getDTO().getAssessmentStatusDTO() );
			}					
			getOracleType().setAppealStatus( assConvertor.getOracleType() );
			
			IojDecisionReasonConvertor iojDrConvertor	= new IojDecisionReasonConvertor();
			if ( getDTO().getAppealReason() != null )
			{
				iojDrConvertor.setTypeFromDTO( getDTO().getAppealReason() );
			}
			getOracleType().setDecisionReasonObject( iojDrConvertor.getOracleType() );
			
			NewWorkReasonConvertor convertor	= new NewWorkReasonConvertor();
			if ( getDTO().getNewWorkReasonDTO() != null )
			{
				convertor.setTypeFromDTO( getDTO().getNewWorkReasonDTO() );
			}
			getOracleType().setNewWorkReasonObject( convertor.getOracleType() );
			
			
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "IOJAppealConvertor - the embedded dto is null");
		}
		catch (SQLException ex)
		{
			throw new MAATSystemException( ex );
		}
	}

}
