package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.OffenceTypeType;
import gov.uk.courtdata.dto.application.OffenceDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class OffenceConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public OffenceDTO getDTO()
	{
		return (OffenceDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public OffenceTypeType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new OffenceTypeType() );
		}
		
		if ( getDbType() instanceof OffenceTypeType )
		{
			return (OffenceTypeType)getDbType();
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
		if ( dto instanceof OffenceDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + OffenceDTO.class.getName());
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
		this.setDto( new OffenceDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setOffenceType(			convertorHelper.toString(	getOracleType().getOffenceType() ));
			getDTO().setDescription(			convertorHelper.toString(	getOracleType().getDescription() ));
			getDTO().setContributionCap(        convertorHelper.toDouble(	getOracleType().getContribsCap() ));
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "OffenceConvertor - the embedded dto is null");
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
			getOracleType().setOffenceType(		convertorHelper.toString( 	getDTO().getOffenceType() ) );
			getOracleType().setDescription(		convertorHelper.toString(	getDTO().getDescription() ));
			getOracleType().setContribsCap(		convertorHelper.toDouble(	getDTO().getContributionCap()));
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "OffenceConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
