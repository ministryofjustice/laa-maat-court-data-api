/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.CapitalEvidenceSummaryType;
import gov.uk.courtdata.dto.application.CapitalEvidenceSummaryDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class CapitalEvidenceSummaryConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public CapitalEvidenceSummaryDTO getDTO()
	{
		return (CapitalEvidenceSummaryDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public CapitalEvidenceSummaryType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new CapitalEvidenceSummaryType() );
		}
		
		if ( getDbType() instanceof CapitalEvidenceSummaryType )
		{
			return (CapitalEvidenceSummaryType)getDbType();
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
		if ( dto instanceof CapitalEvidenceSummaryDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + CapitalEvidenceSummaryDTO.class.getName());
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
		this.setDto( new CapitalEvidenceSummaryDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
	
			getDTO().setCapitalNote(					convertorHelper.toString( 	getOracleType().getCapitalNote()    			));
			getDTO().setEvidenceDueDate(				convertorHelper.toDate(		getOracleType().getEvidenceDueDate()			));	
			getDTO().setEvidenceReceivedDate(			convertorHelper.toDate(		getOracleType().getEvidenceReceivedDate()		));	
			getDTO().setCapitalAllowWithheldDate(		convertorHelper.toDate(		getOracleType().getCapitalAllowWithheldDate()	));	
			getDTO().setCapitalAllowReinstatedDate(		convertorHelper.toDate(		getOracleType().getCapitalAllowReinstatedDate()	));	
			getDTO().setFirstReminderDate(				convertorHelper.toDate(		getOracleType().getFirstReminderDate()			));	
			getDTO().setSecondReminderDate(				convertorHelper.toDate(		getOracleType().getSecondReminderDate()			));
			getDTO().setCapitalAllowance(				convertorHelper.toDouble(	getOracleType().getCapitalAllowance()));
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
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
			getOracleType().setCapitalNote(					convertorHelper.toString( 	getDTO().getCapitalNote()    			));
			getOracleType().setEvidenceDueDate(				convertorHelper.toDate(		getDTO().getEvidenceDueDate()			));	
			getOracleType().setEvidenceReceivedDate(		convertorHelper.toDate(		getDTO().getEvidenceReceivedDate()		));	
			getOracleType().setCapitalAllowWithheldDate(	convertorHelper.toDate(		getDTO().getCapitalAllowWithheldDate()	));	
			getOracleType().setCapitalAllowReinstatedDate(	convertorHelper.toDate(		getDTO().getCapitalAllowReinstatedDate()));	
			getOracleType().setFirstReminderDate(			convertorHelper.toDate(		getDTO().getFirstReminderDate()			));	
			getOracleType().setSecondReminderDate(			convertorHelper.toDate(		getDTO().getSecondReminderDate()		));
			getOracleType().setCapitalAllowance(			convertorHelper.toDouble(	getDTO().getCapitalAllowance()));
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
