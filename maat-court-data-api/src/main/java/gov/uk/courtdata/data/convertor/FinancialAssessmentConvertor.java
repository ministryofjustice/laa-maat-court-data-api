/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.FinAssessmentType;
import gov.uk.courtdata.dto.application.FinancialAssessmentDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class FinancialAssessmentConvertor extends Convertor
{
	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public FinancialAssessmentDTO getDTO()
	{
		return (FinancialAssessmentDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public FinAssessmentType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new FinAssessmentType() );
		}
		
		if ( getDbType() instanceof FinAssessmentType )
		{
			return (FinAssessmentType)getDbType();
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
		if ( dto instanceof FinancialAssessmentDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + FinancialAssessmentDTO.class.getName());
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
		this.setDto( new FinancialAssessmentDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setTimestamp(	getOracleType().getTimeStamp() );
			
			getDTO().setId(				convertorHelper.toLong(		getOracleType().getFinAssessmentId()));
			getDTO().setFullAvailable(	convertorHelper.toBoolean(	getOracleType().getFullAvailable() 	));
			
			IncomeEvidenceSummaryConvertor ieConvertor = new IncomeEvidenceSummaryConvertor();
			if ( getOracleType().getIncomeEvidenceSummaryObject() != null )
			{
				ieConvertor.setDTOFromType(  getOracleType().getIncomeEvidenceSummaryObject() );
			}
			getDTO().setIncomeEvidence( ieConvertor.getDTO() );
						
			InitialAssessmentConvertor iaConvertor = new InitialAssessmentConvertor();
			if ( getOracleType().getInitialAssessmentObject() != null )
			{
				iaConvertor.setDTOFromType(getOracleType().getInitialAssessmentObject());
			}
			getDTO().setInitial(iaConvertor.getDTO());
			
			FullAssessmentConvertor faConvertor = new FullAssessmentConvertor();
			if ( getOracleType().getFullAssessmentObject() != null )
			{
				faConvertor.setDTOFromType(getOracleType().getFullAssessmentObject());
			}
			getDTO().setFull(faConvertor.getDTO());
			
			HardshipOverviewConvertor convertor = new HardshipOverviewConvertor();
			if ( getOracleType().getHardshipOverviewObject() != null )
			{
				convertor.setDTOFromType(getOracleType().getHardshipOverviewObject() );
			}
			getDTO().setHardship(convertor.getDTO());
			
			//Added by Murali
			getDTO().setUsn(	convertorHelper.toLong(	getOracleType().getUsn()));
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			throw new MAATApplicationException( "FinancialAssessmentConvertor - the embedded dto is null");
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
			getOracleType().setTimeStamp(		getDTO().getTimestamp() );
			
			getOracleType().setFinAssessmentId(	convertorHelper.toLong(		getDTO().getId()			));
			getOracleType().setFullAvailable(	convertorHelper.toBoolean(	getDTO().getFullAvailable()	));
			
			IncomeEvidenceSummaryConvertor ieConvertor = new IncomeEvidenceSummaryConvertor();
			if ( getDTO().getIncomeEvidence() != null )
			{
				ieConvertor.setTypeFromDTO( getDTO().getIncomeEvidence() );
			}
			getOracleType().setIncomeEvidenceSummaryObject( ieConvertor.getOracleType());
			

			InitialAssessmentConvertor iaConvertor = new InitialAssessmentConvertor();
			if ( getDTO().getInitial() != null )
			{
				iaConvertor.setTypeFromDTO( getDTO().getInitial() );
			}
			getOracleType().setInitialAssessmentObject( iaConvertor.getOracleType() );
			
			FullAssessmentConvertor faConvertor = new FullAssessmentConvertor();
			if ( getDTO().getFull() != null )
			{
				faConvertor.setTypeFromDTO( getDTO().getFull() );
			}
			getOracleType().setFullAssessmentObject( faConvertor.getOracleType() );
			
			HardshipOverviewConvertor hrConvertor = new HardshipOverviewConvertor();
			if ( getDTO().getHardship() != null )
			{
				hrConvertor.setTypeFromDTO( getDTO().getHardship() );
			}
			getOracleType().setHardshipOverviewObject( hrConvertor.getOracleType() );
			
			//Added by Murali
			getOracleType().setUsn(	convertorHelper.toLong( getDTO().getUsn() ));
						
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "FinancialAssessmentConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}


}
