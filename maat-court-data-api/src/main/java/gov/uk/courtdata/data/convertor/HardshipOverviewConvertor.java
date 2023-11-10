/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.oracle.HardshipOverviewType;
import gov.uk.courtdata.dto.application.HardshipOverviewDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class HardshipOverviewConvertor extends Convertor {

	@Override
	public HardshipOverviewDTO getDTO()
	{
		return (HardshipOverviewDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public HardshipOverviewType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new HardshipOverviewType() );
		}
		
		if ( getDbType() instanceof HardshipOverviewType )
		{
			return (HardshipOverviewType)getDbType();
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
		if ( dto instanceof HardshipOverviewDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " 
												+ dto.getClass().getName() 
												+ " instead of " 
												+ HardshipOverviewDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new HardshipOverviewDTO() );	// create the new DTO

		try
		{

			//Magistrates Court Hardship
			HardshipReviewConvertor mwrConvertor	= new HardshipReviewConvertor();
			if ( getOracleType().getMagsHardshipObject() != null )
			{
				mwrConvertor.setDTOFromType( getOracleType().getMagsHardshipObject() );
			}
			getDTO().setMagCourtHardship( mwrConvertor.getDTO() ); // if type is null this will initialise a valid empty object
			
			//Crown Court Hardship
			HardshipReviewConvertor crwnConvertor	= new HardshipReviewConvertor();
			if ( getOracleType().getCrownHardshipObject() != null )
			{
				crwnConvertor.setDTOFromType( getOracleType().getCrownHardshipObject() );
			}
			getDTO().setCrownCourtHardship( crwnConvertor.getDTO() ); // if type is null this will initialise a valid empty object
			

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
			
			//Magistrates Court Hardship
			HardshipReviewConvertor mwrConvertor	= new HardshipReviewConvertor();
			if ( getDTO().getMagCourtHardship() != null )
			{
				mwrConvertor.setTypeFromDTO( getDTO().getMagCourtHardship() );
			}
			getOracleType().setMagsHardshipObject( mwrConvertor.getOracleType() ); // if type is null this will initialise a valid empty object
			
			//Crown Court Hardship
			HardshipReviewConvertor crwnConverter	= new HardshipReviewConvertor();
			if ( getDTO().getCrownCourtHardship() != null )
			{
				crwnConverter.setTypeFromDTO( getDTO().getCrownCourtHardship() );
			}
			getOracleType().setCrownHardshipObject( crwnConverter.getOracleType() ); // if type is null this will initialise a valid empty object
			
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
