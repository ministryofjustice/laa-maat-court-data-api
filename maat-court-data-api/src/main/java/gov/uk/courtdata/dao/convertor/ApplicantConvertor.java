package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.AddressType;
import gov.uk.courtdata.dao.oracle.ApplicantDetailsType;
import gov.uk.courtdata.dao.oracle.DisabilitiesTabtype;
import gov.uk.courtdata.dao.oracle.DisabilityType;
import gov.uk.courtdata.dto.application.AddressDTO;
import gov.uk.courtdata.dto.application.ApplicantDTO;
import gov.uk.courtdata.dto.application.DisabilityDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class ApplicantConvertor extends Convertor
{

	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public ApplicantDTO getDTO()
	{
		return (ApplicantDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public ApplicantDetailsType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new ApplicantDetailsType() );
		}
		
		if ( getDbType() instanceof ApplicantDetailsType )
		{
			return (ApplicantDetailsType)getDbType();
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
		if ( dto instanceof ApplicantDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + AddressDTO.class.getName());
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
		this.setDto( new ApplicantDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setTimestamp(					convertorHelper.toLocalDateTime(getOracleType().getTimeStamp()));
			getDTO().setId(							convertorHelper.toSysGenLong( 	getOracleType().getApplId() ));
			getDTO().setApplicantHistoryId(			convertorHelper.toLong( 	getOracleType().getAphiId() ));
			getDTO().setDob(						convertorHelper.toDate(		getOracleType().getDob() ));
			getDTO().setEmail(						convertorHelper.toString(	getOracleType().getEmail() ));
			getDTO().setFirstName(					convertorHelper.toString(	getOracleType().getFirstName()  ));
			getDTO().setGender(						convertorHelper.toString(	getOracleType().getGender()  ));
			getDTO().setHomeTelephone(				convertorHelper.toString(	getOracleType().getPhoneHome()  ));
			getDTO().setWorkTelephone(				convertorHelper.toString(	getOracleType().getPhoneWork()  ));
			getDTO().setMobileTelephone(			convertorHelper.toString(	getOracleType().getPhoneMobile()  ));
			getDTO().setNiNumber(					convertorHelper.toString(	getOracleType().getNiNumber()  ));
			getDTO().setForeignId(					convertorHelper.toString(	getOracleType().getForeignId()  ));
			getDTO().setNoFixedAbode(				convertorHelper.toBoolean(	getOracleType().getNoFixedAbode()  ));
			getDTO().setDisabled(    				convertorHelper.toString(	getOracleType().getDisabled()  ));
			getDTO().setOtherNames(					convertorHelper.toString(	getOracleType().getOtherNames()  ));
			getDTO().setUseSupplierAddrForPost(		convertorHelper.toBoolean(	getOracleType().getUseSuppAddrForPost()));
			getDTO().setSurname(					convertorHelper.toString(	getOracleType().getLastName()  ));
			getDTO().setSpecialInvestigation(			convertorHelper.toDate(		getOracleType().getSpecialInvestigation()));

			DisabilityConvertor disabilityConvertor	= new DisabilityConvertor();
			DisabilitiesTabtype	disabilityTabType 	= getOracleType().getAppDisabilitiesTab();
	    	/*
	    	 * set an empty collection
	    	 */
			getDTO().setDisabilities( new ArrayList<DisabilityDTO>() );

			if ( disabilityTabType != null )
			{
				DisabilityType[] disabilityTypes = disabilityTabType.getArray();
				for ( int i = 0; i < disabilityTypes.length; i++ )
				{
					disabilityConvertor.setDTOFromType( disabilityTypes[i] );
					getDTO().getDisabilities().add( disabilityConvertor.getDTO() );
				}
			}
			
			if ( getOracleType().getEmpStatusObject() != null )
			{
				EmploymentStatusConvertor empStatusConvertor	= new EmploymentStatusConvertor();
				empStatusConvertor.setDTOFromType( getOracleType().getEmpStatusObject() );
				getDTO().setEmploymentStatusDTO( empStatusConvertor.getDTO() );
			}
			
			if ( getOracleType().getEmpStatusObject() != null )
			{
				EthnicityConvertor convertor	= new EthnicityConvertor();
				convertor.setDTOFromType( getOracleType().getEthnicityObject() );
				getDTO().setEthnicity( convertor.getDTO() );
			}
			
			AddressType 		addressType		= getOracleType().getHomeAddressObject();
			AddressConvertor adressConvertor	= new AddressConvertor();
			
			if ( addressType != null )
			{
				adressConvertor.setDTOFromType(addressType);
				getDTO().setHomeAddressDTO( adressConvertor.getDTO() );
			}
			
			addressType = getOracleType().getPostAddressObject();
			if ( addressType != null )
			{
				adressConvertor.setDTOFromType(addressType);
				getDTO().setPostalAddressDTO( adressConvertor.getDTO() );
			}
			
			ApplicantPaymentDetailsConvertor applPayDetailsConv = new ApplicantPaymentDetailsConvertor();
			
			if ( applPayDetailsConv != null )
			{
				applPayDetailsConv.setDTOFromType(getOracleType().getApplPaymentDetailsObject());
			}
			getDTO().setPaymentDetailsDTO( applPayDetailsConv.getDTO() );
			
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "ApplicantConvertor - the embedded dto is null");
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
			
	        getOracleType().setTimeStamp(          	convertorHelper.toTimestamp(getDTO().getTimestamp()));
			getOracleType().setApplId( 				convertorHelper.toSysGenLong( 	getDTO().getId() ) );
			getOracleType().setAphiId( 				convertorHelper.toLong( 	getDTO().getApplicantHistoryId() ) );
			getOracleType().setDob(					convertorHelper.toDate(		getDTO().getDob() ));
			getOracleType().setEmail(				convertorHelper.toString(	getDTO().getEmail() ));
			getOracleType().setFirstName(			convertorHelper.toString(	getDTO().getFirstName()  ));
			getOracleType().setGender(				convertorHelper.toString(	getDTO().getGender()  ));
			getOracleType().setPhoneHome(			convertorHelper.toString(	getDTO().getHomeTelephone()  ));
			getOracleType().setPhoneWork(			convertorHelper.toString(	getDTO().getWorkTelephone()  ));
			getOracleType().setPhoneMobile(			convertorHelper.toString(	getDTO().getMobileTelephone()  ));
			getOracleType().setNiNumber(			convertorHelper.toString(	getDTO().getNiNumber()  ));
			getOracleType().setForeignId(			convertorHelper.toString(	getDTO().getForeignId()  ));
			getOracleType().setNoFixedAbode(		convertorHelper.toBoolean(	getDTO().getNoFixedAbode()  ));
			getOracleType().setDisabled(			convertorHelper.toString(	getDTO().getDisabled()  ));
			
			getOracleType().setOtherNames(			convertorHelper.toString(	getDTO().getOtherNames()  ));
			getOracleType().setLastName(			convertorHelper.toString(	getDTO().getSurname()  ));
			
			getOracleType().setUseSuppAddrForPost(	convertorHelper.toBoolean(	getDTO().getUseSupplierAddrForPost()));
			getOracleType().setSpecialInvestigation(convertorHelper.toDate(		getDTO().getSpecialInvestigation() ));

			if ( ( getDTO().getDisabilities() != null ) && ( getDTO().getDisabilities().size() > 0 ) )
			{

				DisabilityType[] 		disabilityTypes = new DisabilityType[ getDTO().getDisabilities().size() ];
				Iterator<DisabilityDTO> dit				= getDTO().getDisabilities().iterator();
				int idx = 0;
				while ( dit.hasNext() )
				{
					DisabilityDTO disabilityDTO	= (DisabilityDTO)dit.next();
                    DisabilityConvertor 	disabilityConvertor	= new DisabilityConvertor();
					disabilityConvertor.setTypeFromDTO( disabilityDTO );
					disabilityTypes[idx++]		= disabilityConvertor.getOracleType();
				}
				getOracleType().setAppDisabilitiesTab(new DisabilitiesTabtype( disabilityTypes ));
			}
			else
			{
				getOracleType().setAppDisabilitiesTab(new DisabilitiesTabtype(  ));
			}
			
			EmploymentStatusConvertor empStatusConvertor	= new EmploymentStatusConvertor();
			if ( getDTO().getEmploymentStatusDTO() != null )
			{
				empStatusConvertor.setTypeFromDTO( getDTO().getEmploymentStatusDTO() );
			}
			getOracleType().setEmpStatusObject( empStatusConvertor.getOracleType() );

			EthnicityConvertor convertor	= new EthnicityConvertor();
			if ( getDTO().getEthnicity() != null )
			{
				convertor.setTypeFromDTO( getDTO().getEthnicity() );
			}
			getOracleType().setEthnicityObject( convertor.getOracleType() );

			AddressConvertor 	adressConvertor	= new AddressConvertor();

			if ( getDTO().getHomeAddressDTO() != null )
			{
				adressConvertor.setTypeFromDTO( getDTO().getHomeAddressDTO() );
			}
			getOracleType().setHomeAddressObject( adressConvertor.getOracleType() );

			AddressConvertor 	postAdressConvertor	= new AddressConvertor();
			if ( getDTO().getPostalAddressDTO() != null )
			{
				postAdressConvertor.setTypeFromDTO( getDTO().getPostalAddressDTO() );
			}
			getOracleType().setPostAddressObject( postAdressConvertor.getOracleType() );

			ApplicantPaymentDetailsConvertor 	applicantPaymentDetailsConvertor	= new ApplicantPaymentDetailsConvertor();
			if ( getDTO().getPaymentDetailsDTO() != null )
			{
				applicantPaymentDetailsConvertor.setTypeFromDTO( getDTO().getPaymentDetailsDTO() );
			}
			getOracleType().setApplPaymentDetailsObject( applicantPaymentDetailsConvertor.getOracleType() );


		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "ApplicantConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
