package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.ContributionSummaryType;
import gov.uk.courtdata.dto.application.ContributionSummaryDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class ContributionSummaryConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public ContributionSummaryDTO getDTO()
	{
		return (ContributionSummaryDTO)getDto();
	}

	/**
	 * 
	 * @see Convertor#getOracleType()
	 */
	@Override
	public ContributionSummaryType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new ContributionSummaryType() );
		}
		
		if ( getDbType() instanceof ContributionSummaryType )
		{
			return (ContributionSummaryType)getDbType();
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
		if ( dto instanceof ContributionSummaryDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ContributionSummaryDTO.class.getName());
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
		this.setDto( new ContributionSummaryDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
	
			getDTO().setId(					convertorHelper.toLong( 	getOracleType().getId()    				));
			getDTO().setMonthlyContribs(	convertorHelper.toDouble( 	getOracleType().getMonthlyContribs()    ));
			getDTO().setUpfrontContribs(	convertorHelper.toDouble( 	getOracleType().getUpfrontContribs()	));
			getDTO().setEffectiveDate( 		convertorHelper.toDate( 	getOracleType().getEffectiveDate()  	));
			getDTO().setCalcDate(			convertorHelper.toDate(		getOracleType().getCalcDate()			));	
			getDTO().setUpliftApplied(		convertorHelper.toBoolean(	getOracleType().getUpliftApplied()		));
			getDTO().setBasedOn(			convertorHelper.toString(	getOracleType().getBasedOn()			));
			getDTO().setFileName(			convertorHelper.toString(	getOracleType().getFileName()			));
			getDTO().setDateFileSent( 		convertorHelper.toDate( 	getOracleType().getDateFileSent()	  	));
			getDTO().setDateFileReceived(	convertorHelper.toDate( 	getOracleType().getDateFileReceived()  	));
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "ContributionsConvertor - the embedded dto is null");
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
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getOracleType().setId(					convertorHelper.toLong( 	getDTO().getId()    			));
			getOracleType().setMonthlyContribs(	    convertorHelper.toDouble( 	getDTO().getMonthlyContribs()   ));
			getOracleType().setUpfrontContribs(	    convertorHelper.toDouble( 	getDTO().getUpfrontContribs()	));
			getOracleType().setEffectiveDate( 		convertorHelper.toDate( 	getDTO().getEffectiveDate()  	));
			getOracleType().setCalcDate(			convertorHelper.toDate(		getDTO().getCalcDate()			));	
			getOracleType().setBasedOn(				convertorHelper.toString(	getDTO().getBasedOn()			));
			getOracleType().setFileName(			convertorHelper.toString(	getDTO().getFileName()			));
			getOracleType().setDateFileSent(		convertorHelper.toDate(		getDTO().getDateFileSent()		));	
			getOracleType().setDateFileReceived(	convertorHelper.toDate(		getDTO().getDateFileReceived()	));	
			getOracleType().setUpliftApplied(		convertorHelper.toBoolean(	getDTO().getUpliftApplied() 	));
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "ContributionsConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
