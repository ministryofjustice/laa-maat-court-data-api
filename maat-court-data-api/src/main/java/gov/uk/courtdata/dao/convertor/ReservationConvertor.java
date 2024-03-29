package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.ReservationType;
import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.dto.application.ReservationDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class ReservationConvertor extends Convertor {

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	public ReservationDTO getDTO() 
	{
		return (ReservationDTO)this.getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	
	public ReservationType getOracleType() throws MAATApplicationException, MAATSystemException 
	{
		if ( getDbType() == null )
		{
			setType( new ReservationType() );
		}
		
		if ( getDbType() instanceof ReservationType )
		{
			return (ReservationType)getDbType();
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTO(java.lang.Object)
	 */
	
	public void setDTO(Object dto) throws MAATApplicationException 
	{
		if ( dto instanceof ReservationDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ReservationDTO.class.getName());
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTOFromType(java.lang.Object)
	 */
	
	public void setDTOFromType(Object oracleType) throws MAATApplicationException, MAATSystemException 
	{
		// save it
		this.setType( oracleType );
		this.setDto( new ApplicationDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setRecordId(				convertorHelper.toLong( 	getOracleType().getRecordId() ));
			getDTO().setRecordName(				convertorHelper.toString( 	getOracleType().getRecordName() ));
		}
		catch (NegativeArraySizeException nasx )
		{
			throw new MAATSystemException( nasx );
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
			setDTO( dto );	
			ConvertorHelper convertorHelper 		= new ConvertorHelper();
			getOracleType().setRecordId( 			convertorHelper.toLong( 	getDTO().getRecordId() ) );
			getOracleType().setRecordName(			convertorHelper.toString( 	getDTO().getRecordName() ));

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
