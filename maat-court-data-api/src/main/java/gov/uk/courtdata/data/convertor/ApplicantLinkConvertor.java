/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.ApplicantDetailsType;
import gov.uk.courtdata.data.oracle.ApplicantLinkType;
import gov.uk.courtdata.dto.application.ApplicantLinkDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class ApplicantLinkConvertor extends Convertor
{

	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public ApplicantLinkDTO getDTO()
	{
		return (ApplicantLinkDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public ApplicantLinkType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new ApplicantLinkType() );
		}
		
		if ( getDbType() instanceof ApplicantLinkType )
		{
			return (ApplicantLinkType)getDbType();
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
		if ( dto instanceof ApplicantLinkDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ApplicantLinkDTO.class.getName());
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
		this.setDto( new ApplicantLinkDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setId(							convertorHelper.toLong( 	getOracleType().getId() ));
			getDTO().setLinked(						convertorHelper.toDate(		getOracleType().getLinkDate() ));
			getDTO().setUnlinked(					convertorHelper.toDate(		getOracleType().getUnlinkDate() ));

			ApplicantConvertor applicantConvertor	= new ApplicantConvertor();
			
			ApplicantDetailsType applicantDetailsType = getOracleType().getApplicantDetailsObject();
			if ( applicantDetailsType != null )
			{
				applicantConvertor.setDTOFromType(applicantDetailsType);
				getDTO().setPartnerDTO( applicantConvertor.getDTO() );
			}
			
			
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ApplicantConvertor - the embedded dto is null");
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
			
			getOracleType().setId( 					convertorHelper.toLong( 	getDTO().getId() ) );
			getOracleType().setLinkDate(			convertorHelper.toDate(		getDTO().getLinked() ));
			getOracleType().setUnlinkDate(			convertorHelper.toDate(		getDTO().getUnlinked() ));
			

			ApplicantConvertor applicantConvertor	= new ApplicantConvertor();
			if ( getDTO().getPartnerDTO() != null )
			{
				applicantConvertor.setTypeFromDTO( getDTO().getPartnerDTO() );
			}
			getOracleType().setApplicantDetailsObject( applicantConvertor.getOracleType() );


		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ApplicantConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
