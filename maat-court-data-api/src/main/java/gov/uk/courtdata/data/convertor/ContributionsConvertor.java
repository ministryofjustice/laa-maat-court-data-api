/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.ContributionsType;
import gov.uk.courtdata.dto.application.ContributionsDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class ContributionsConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public ContributionsDTO getDTO()
	{
		return (ContributionsDTO)getDto();
	}

	/**
	 * 
	 * @see Convertor#getOracleType()
	 */
	@Override
	public ContributionsType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new ContributionsType() );
		}
		
		if ( getDbType() instanceof ContributionsType )
		{
			return (ContributionsType)getDbType();
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
		if ( dto instanceof ContributionsDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ContributionsDTO.class.getName());
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
		this.setDto( new ContributionsDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
	
			getDTO().setId(					convertorHelper.toLong( 	getOracleType().getId()    				));
			getDTO().setMonthlyContribs(	convertorHelper.toSysGenCurrency( 	getOracleType().getMonthlyContribs()    ));
			getDTO().setUpfrontContribs(	convertorHelper.toSysGenCurrency( 	getOracleType().getUpfrontContribs()	));
			getDTO().setEffectiveDate( 		convertorHelper.toSysGenDate( 	getOracleType().getEffectiveDate()  	));
			getDTO().setCalcDate(			convertorHelper.toSysGenDate(		getOracleType().getCalcDate()			));
			getDTO().setCapped(				convertorHelper.toSysGenCurrency(	getOracleType().getContributionCap()	));
			getDTO().setUpliftApplied(		convertorHelper.toBoolean(	getOracleType().getUpliftApplied()		));
			getDTO().setBasedOn(			convertorHelper.toSysGenString(	getOracleType().getBasedOn()			));
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ContributionsConvertor - the embedded dto is null");
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

			getOracleType().setId(					convertorHelper.toLong( 	getDTO().getId()    			));
			getOracleType().setMonthlyContribs(	    convertorHelper.toSysGenCurrency( 	getDTO().getMonthlyContribs()   ));
			getOracleType().setUpfrontContribs(	    convertorHelper.toSysGenCurrency( 	getDTO().getUpfrontContribs()	));
			getOracleType().setEffectiveDate( 		convertorHelper.toSysGenDate( 	getDTO().getEffectiveDate()  	));
			getOracleType().setCalcDate(			convertorHelper.toSysGenDate(		getDTO().getCalcDate()			));
			getOracleType().setContributionCap(		convertorHelper.toSysGenCurrency(	getDTO().getCapped()			));
			getOracleType().setUpliftApplied(		convertorHelper.toBoolean(	getDTO().isUpliftApplied()		));
			getOracleType().setBasedOn(				convertorHelper.toSysGenString(	getDTO().getBasedOn()			));
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ContributionsConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
