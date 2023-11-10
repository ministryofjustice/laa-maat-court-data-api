/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.LSCTransferType;
import gov.uk.courtdata.dto.application.LscTransferDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class LSCTransferConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public LscTransferDTO getDTO()
	{
		return (LscTransferDTO)getDto();
	}
	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public LSCTransferType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new LSCTransferType() );
		}
		
		if ( getDbType() instanceof LSCTransferType )
		{
			return (LSCTransferType)getDbType();
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
		if ( dto instanceof LscTransferDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + LscTransferDTO.class.getName());
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
		this.setDto( new LscTransferDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setId(						convertorHelper.toLong(	getOracleType().getId() ));		
			getDTO().setDateHmcsSent(			convertorHelper.toDate(	getOracleType().getDateHmcsSent() ));
			getDTO().setDateLscReceived(		convertorHelper.toDate(	getOracleType().getDateLscReceived() ));
			getDTO().setDateLscReturned(		convertorHelper.toDate(	getOracleType().getDateLscReturned() ));
			getDTO().setDateHmcsReceived(		convertorHelper.toDate(	getOracleType().getDateHmcsReceived() ));
						
			//HMCS Sent By
			UserConvertor hsConvertor = new UserConvertor();
			if(getOracleType().getHmcsSentByUserObject() != null){
				hsConvertor.setDTOFromType(getOracleType().getHmcsSentByUserObject());
			}
			getDTO().setHmcsSentBy(hsConvertor.getDTO());
			
			//LSC Received By
			UserConvertor lrConvertor = new UserConvertor();
			if(getOracleType().getLscReceivedByUserObject() != null){
				lrConvertor.setDTOFromType(getOracleType().getLscReceivedByUserObject());
			}
			getDTO().setLscReceivedBy(lrConvertor.getDTO());
			
			//LSC Returned By
			UserConvertor lrtdConvertor = new UserConvertor();
			if(getOracleType().getLscReturnedByUserObject() != null){
				lrtdConvertor.setDTOFromType(getOracleType().getLscReturnedByUserObject());
			}
			getDTO().setLscReturnedBy(lrtdConvertor.getDTO());
			
			//HMCS Received By
			UserConvertor hrConvertor = new UserConvertor();
			if(getOracleType().getHmcsReceivedByUserObject() != null){
				hrConvertor.setDTOFromType(getOracleType().getHmcsReceivedByUserObject());
			}
			getDTO().setHmcsReceivedBy(hrConvertor.getDTO());
	
			//Area From Object
			AreaConvertor afConvertor	= new AreaConvertor();
			if(getOracleType().getAreaFromObject() != null){
				afConvertor.setDTOFromType(getOracleType().getAreaFromObject() );
			}
			getDTO().setAreaFrom( afConvertor.getDTO() );
			
			//Area To Object
			AreaConvertor atConvertor	= new AreaConvertor();			
			if(getOracleType().getAreaToObject() != null){
				atConvertor.setDTOFromType(getOracleType().getAreaToObject() );
			}
			getDTO().setAreaTo( atConvertor.getDTO() );
			
			//CMU From Object
			CaseManagementUnitConvertor cmuConvertor	= new CaseManagementUnitConvertor();			
			if(getOracleType().getCmuFromObject() != null){
				cmuConvertor.setDTOFromType(getOracleType().getCmuFromObject() );
			}
			getDTO().setCmuFrom( cmuConvertor.getDTO() );
			
			//CMU To Object
			if(getOracleType().getCmuToObject() != null){
				cmuConvertor.setDTOFromType(getOracleType().getCmuToObject() );
			}
			getDTO().setCmuTo( cmuConvertor.getDTO() );
			
			//Transfer Type Object
			TransferTypeConvertor typeConvertor = new TransferTypeConvertor();
			if(getOracleType().getTransferTypeObject() != null){
				typeConvertor.setDTOFromType(getOracleType().getTransferTypeObject());
			}
			getDTO().setTransferType(typeConvertor.getDTO());
			
			//Transfer Status Object
			TransferStatusConvertor statusConvertor = new TransferStatusConvertor();
			if(getOracleType().getStatusObject() != null){
				statusConvertor.setDTOFromType(getOracleType().getStatusObject());
			}
			getDTO().setTransferStatus(statusConvertor.getDTO());
		
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "LSCTransferConvertor - the embedded dto is null");
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

			getOracleType().setId(						convertorHelper.toLong(	    getDTO().getId() ));		
			getOracleType().setDateHmcsSent(			convertorHelper.toDate(	    getDTO().getDateHmcsSent() ));
			getOracleType().setDateLscReceived(			convertorHelper.toDate(	    getDTO().getDateLscReceived() ));
			getOracleType().setDateLscReturned(			convertorHelper.toDate(	    getDTO().getDateLscReturned() ));
			getOracleType().setDateHmcsReceived(		convertorHelper.toDate(	    getDTO().getDateHmcsReceived() ));						
						
			//HMCS Sent By
			UserConvertor hsConvertor = new UserConvertor();
			if(getDTO().getHmcsSentBy() != null){
				hsConvertor.setTypeFromDTO(getDTO().getHmcsSentBy());
			}
			getOracleType().setHmcsSentByUserObject(hsConvertor.getOracleType());
			
			//HMCS Received By
			UserConvertor hrConvertor = new UserConvertor();
			if(getDTO().getHmcsReceivedBy() != null){
				hrConvertor.setTypeFromDTO(getDTO().getHmcsReceivedBy());
			}
			getOracleType().setHmcsReceivedByUserObject(hrConvertor.getOracleType());
			
			//LSC Received By
			UserConvertor lrConvertor = new UserConvertor();
			if(getDTO().getLscReceivedBy() != null){
				lrConvertor.setTypeFromDTO(getDTO().getLscReceivedBy());
			}
			getOracleType().setLscReceivedByUserObject(lrConvertor.getOracleType());
			
			//LSC Returned By
			UserConvertor lrtConvertor = new UserConvertor();
			if(getDTO().getLscReturnedBy() != null){
				lrtConvertor.setTypeFromDTO(getDTO().getLscReturnedBy());
			}
			getOracleType().setLscReturnedByUserObject(lrtConvertor.getOracleType());
			
			//Area From Object
			AreaConvertor afConvertor	= new AreaConvertor();	
			if(getDTO().getAreaFrom() != null){
				afConvertor.setTypeFromDTO(getDTO().getAreaFrom());
			}
			getOracleType().setAreaFromObject(afConvertor.getOracleType());
			
			//Area To Object
			AreaConvertor atConvertor	= new AreaConvertor();	
			if(getDTO().getAreaTo() != null){
				atConvertor.setTypeFromDTO(getDTO().getAreaTo());
			}
			getOracleType().setAreaToObject(atConvertor.getOracleType());
					
			//Cmu To Object
			CaseManagementUnitConvertor cmuToConvertor	= new CaseManagementUnitConvertor();	
			if(getDTO().getCmuTo() != null){
				cmuToConvertor.setTypeFromDTO(getDTO().getCmuTo());
			}
			getOracleType().setCmuToObject(cmuToConvertor.getOracleType());
			
			//Cmu From Object
			CaseManagementUnitConvertor cmuFrmConvertor	= new CaseManagementUnitConvertor();
			if(getDTO().getCmuFrom() != null){
				cmuFrmConvertor.setTypeFromDTO(getDTO().getCmuFrom());
			}
			getOracleType().setCmuFromObject(cmuFrmConvertor.getOracleType());
			
			//Transfer Type Object
			TransferTypeConvertor typeConvertor = new TransferTypeConvertor();
			if(getDTO().getTransferType() != null){
				typeConvertor.setTypeFromDTO(getDTO().getTransferType());
			}
			getOracleType().setTransferTypeObject(typeConvertor.getOracleType());
			
			//Transfer Status Object
			TransferStatusConvertor statusConvertor = new TransferStatusConvertor();
			if(getDTO().getTransferStatus() != null){
				statusConvertor.setTypeFromDTO(getDTO().getTransferStatus());
			}
			getOracleType().setStatusObject(statusConvertor.getOracleType());
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "LSCTransferConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
