/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.ApplPaymentDetailsType;
import gov.uk.courtdata.dto.application.ApplicantPaymentDetailsDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 * 
 *
 */
public class ApplicantPaymentDetailsConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public ApplicantPaymentDetailsDTO getDTO() 
	{
		return (ApplicantPaymentDetailsDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public ApplPaymentDetailsType getOracleType() throws MAATApplicationException,
			MAATSystemException {

		ApplPaymentDetailsType ret = null;
		
		if ( getDbType() == null )
		{
			setType( new ApplPaymentDetailsType() );
		}
		
		if ( getDbType() instanceof ApplPaymentDetailsType )
		{
			return (ApplPaymentDetailsType)getDbType();
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
		if ( dto instanceof ApplicantPaymentDetailsDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ApplicantPaymentDetailsDTO.class.getName());
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
		this.setDto( new ApplicantPaymentDetailsDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
	
			getDTO().setPaymentDay(		convertorHelper.toInteger(	getOracleType().getPaymentDay() 			));	
			getDTO().setAccountNumber(	convertorHelper.toString(	getOracleType().getBankAccountNo() 			));	
			getDTO().setSortCode(		convertorHelper.toString(	getOracleType().getSortCode()		 		));	
			getDTO().setAccountName(	convertorHelper.toString(	getOracleType().getBankAccountName() 		));					

			PaymentMethodConvertor paymentConvertor = new PaymentMethodConvertor();
			
			if(getOracleType().getPaymentMethodObject() != null){
				paymentConvertor.setDTOFromType(getOracleType().getPaymentMethodObject());
			}
			getDTO().setPaymentMethod(paymentConvertor.getDTO());
			
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ApplicantPaymentDetailsConvertor - the embedded dto is null", nex);
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
			getOracleType().setBankAccountName(		convertorHelper.toString(	getDTO().getAccountName() 	));	
			getOracleType().setBankAccountNo(		convertorHelper.toString(	getDTO().getAccountNumber()	));				
			getOracleType().setPaymentDay(			convertorHelper.toInteger(	getDTO().getPaymentDay()	));	
			getOracleType().setSortCode(			convertorHelper.toString(	getDTO().getSortCode()	));	

			if(getDTO().getPaymentMethod() != null){
				PaymentMethodConvertor paymentMethodConvertor = new PaymentMethodConvertor();
				paymentMethodConvertor.setTypeFromDTO(getDTO().getPaymentMethod());
				getOracleType().setPaymentMethodObject( paymentMethodConvertor.getOracleType());
			}
		}		
		catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ApplicantPaymentDetailsConvertor - the embedded dto is null", nex );
		}
		catch (SQLException ex ){
			throw new MAATSystemException( ex );
		}
	}

}
