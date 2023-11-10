package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.HrDetailType;
import gov.uk.courtdata.dto.application.HRDetailDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class HRDetailConvertor extends Convertor {

	@Override
	public HRDetailDTO getDTO()
	{
		return (HRDetailDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public HrDetailType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new HrDetailType() );
		}
		
		if ( getDbType() instanceof HrDetailType )
		{
			return (HrDetailType)getDbType();
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
		if ( dto instanceof HRDetailDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " 
												+ dto.getClass().getName() 
												+ " instead of " 
												+ HRDetailDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new HRDetailDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setId( 				convertorHelper.toLong(		getOracleType().getId()));
			getDTO().setDateReceived(		convertorHelper.toDate(		getOracleType().getDateReceived()));
			getDTO().setOtherDescription(	convertorHelper.toString(	getOracleType().getOtherDescription()));
			getDTO().setAmountNumber(		convertorHelper.toCurrency(	getOracleType().getAmountNumber()));
			getDTO().setDateDue(			convertorHelper.toDate(		getOracleType().getDateDue()));
			getDTO().setAccepted(			convertorHelper.toBoolean(	getOracleType().getAccepted()));
			getDTO().setTimeStamp(			getOracleType().getTimeStamp());
			getDTO().setHrReasonNote(		convertorHelper.toString(getOracleType().getHrReasonNote()));
			
			FrequencyConvertor frequencyConverter = new FrequencyConvertor();
			if(getOracleType().getFrequencyObject() != null)
			{			
				frequencyConverter.setDTOFromType(getOracleType().getFrequencyObject());
			}
			getDTO().setFrequency(frequencyConverter.getDTO());
			
			HRReasonConvertor reasonConverter = new HRReasonConvertor();
			if(getOracleType().getHrReasonObject() != null)
			{
				reasonConverter.setDTOFromType(getOracleType().getHrReasonObject());
			}
			getDTO().setReason(reasonConverter.getDTO());
			
			
			HRDetailDescriptionConvertor hrDCconvertor = new HRDetailDescriptionConvertor();
			if(getOracleType().getHrDetailDescriptionObject() != null)
			{
				hrDCconvertor.setDTOFromType(getOracleType().getHrDetailDescriptionObject());
			}			
			getDTO().setDetailDescription(hrDCconvertor.getDTO() );
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
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
			getOracleType().setId(				convertorHelper.toLong(		getDTO().getId()));
            getOracleType().setDateReceived(	convertorHelper.toDate(		getDTO().getDateReceived()));
            getOracleType().setOtherDescription(convertorHelper.toString(	getDTO().getOtherDescription()));
            getOracleType().setAmountNumber(	convertorHelper.toCurrency(	getDTO().getAmountNumber()));
            getOracleType().setDateDue(			convertorHelper.toDate(		getDTO().getDateDue()));
            getOracleType().setAccepted(		convertorHelper.toBoolean(	getDTO().isAccepted()));
            getOracleType().setTimeStamp(									getDTO().getTimeStamp()); 
			getOracleType().setHrReasonNote(	convertorHelper.toString(	getDTO().getHrReasonNote()));
			FrequencyConvertor frequencyConverter = new FrequencyConvertor();
			if(getDTO().getFrequency() != null)
			{
				frequencyConverter.setTypeFromDTO(getDTO().getFrequency());
			}
	        getOracleType().setFrequencyObject(frequencyConverter.getOracleType());
	        
	        HRReasonConvertor reasonConverter = new HRReasonConvertor();
	        if(getDTO().getReason() != null)
	        {
	        	reasonConverter.setTypeFromDTO(getDTO().getReason());
	        }
	        getOracleType().setHrReasonObject(reasonConverter.getOracleType());
	        
			HRDetailDescriptionConvertor hrDCconvertor = new HRDetailDescriptionConvertor();
			if ( getDTO().getDetailDescription() != null )
			{
				hrDCconvertor.setTypeFromDTO( getDTO().getDetailDescription() );
			}
			getOracleType().setHrDetailDescriptionObject( hrDCconvertor.getOracleType() );
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( this.getClass().getName() + " - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
