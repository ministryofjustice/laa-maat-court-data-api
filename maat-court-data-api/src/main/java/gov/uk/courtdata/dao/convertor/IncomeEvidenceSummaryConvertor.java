package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.*;
import gov.uk.courtdata.dto.application.EvidenceDTO;
import gov.uk.courtdata.dto.application.ExtraEvidenceDTO;
import gov.uk.courtdata.dto.application.IncomeEvidenceSummaryDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class IncomeEvidenceSummaryConvertor extends Convertor
{

	/**
	 * 
	 * @see Convertor#getDTO()
	 */
	@Override
	public IncomeEvidenceSummaryDTO getDTO()
	{
		return (IncomeEvidenceSummaryDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public IncomeEvidenceSummaryType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new IncomeEvidenceSummaryType() );
		}
		
		if ( getDbType() instanceof IncomeEvidenceSummaryType )
		{
			return (IncomeEvidenceSummaryType)getDbType();
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
		if ( dto instanceof IncomeEvidenceSummaryDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + IncomeEvidenceSummaryDTO.class.getName());
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
		this.setDto( new IncomeEvidenceSummaryDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
					
			getDTO().setIncomeEvidenceNotes(	convertorHelper.toString( 	getOracleType().getIncomeEvidenceNotes()    ));
			getDTO().setEvidenceDueDate(		convertorHelper.toDate(		getOracleType().getEvidenceDueDate()		));	
			getDTO().setEvidenceReceivedDate(	convertorHelper.toDate(		getOracleType().getEvidenceReceivedDate()	));	
			getDTO().setUpliftAppliedDate(		convertorHelper.toDate(		getOracleType().getUpliftAppliedDate()		));	
			getDTO().setUpliftRemovedDate(		convertorHelper.toDate(		getOracleType().getUpliftRemovedDate()		));	
			getDTO().setFirstReminderDate(		convertorHelper.toDate(		getOracleType().getFirstReminderDate()		));	
			getDTO().setSecondReminderDate(		convertorHelper.toDate(		getOracleType().getSecondReminderDate()		));
			getDTO().setEnabled(				convertorHelper.toBoolean(	getOracleType().getEnabled()				));
			getDTO().setUpliftsAvailable(		convertorHelper.toBoolean(	getOracleType().getUpliftsAvailable()		));
			
			/*
			 * set up empty collection for the applicant income evidence table
			 */
			getDTO().setApplicantIncomeEvidenceList( new ArrayList<EvidenceDTO>() );
			
			if ( getOracleType().getApplicantIncomeEvidenceTab() != null )
			{
				IncomeEvidenceType[] 	ievTab 		= getOracleType().getApplicantIncomeEvidenceTab().getArray();
				IncomeEvidenceConvertor convertor 	= new IncomeEvidenceConvertor();
				
				for ( int i = 0; i < ievTab.length; i++ )
				{
					convertor.setDTOFromType(  ievTab[i] );
					getDTO().getApplicantIncomeEvidenceList().add( convertor.getDTO() );
				}
			}
			
			/*
			 * set up empty collection for the applicant income evidence table
			 */
			getDTO().setPartnerIncomeEvidenceList( new ArrayList<EvidenceDTO>() );
			
			if ( getOracleType().getPartnerIncomeEvidenceTab() != null )
			{
				IncomeEvidenceType[] 	ievTab 		= getOracleType().getPartnerIncomeEvidenceTab().getArray();
				IncomeEvidenceConvertor convertor 	= new IncomeEvidenceConvertor();
				
				for ( int i = 0; i < ievTab.length; i++ )
				{
					convertor.setDTOFromType(  ievTab[i] );
					getDTO().getPartnerIncomeEvidenceList().add( convertor.getDTO() );
				}
			}
			
			/*
			 * convert the extra evidence collection
			 */
			getDTO().setExtraEvidenceList( new ArrayList<ExtraEvidenceDTO>() );
			if ( getOracleType().getExtraEvidenceTab() != null )
			{
				ExtraEvidenceTabtype 	eett 		= getOracleType().getExtraEvidenceTab();
				ExtraEvidenceType[]		eettArray	= eett.getArray();
				ExtraEvidenceConvertor eec			= new ExtraEvidenceConvertor();
				for ( int i = 0; i < eettArray.length; i++ )
				{
					eec.setDTOFromType( eettArray[i] );
					getDTO().getExtraEvidenceList().add( eec.getDTO() );
				}				
			}
			
			
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "IncomeEvidenceSummaryConvertor - the embedded dto is null");
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
			getOracleType().setIncomeEvidenceNotes(	convertorHelper.toString( 	getDTO().getIncomeEvidenceNotes()   ));
			getOracleType().setEvidenceDueDate(		    convertorHelper.toDate(		getDTO().getEvidenceDueDate()		));	
			getOracleType().setEvidenceReceivedDate(	convertorHelper.toDate(		getDTO().getEvidenceReceivedDate()  ));	
			getOracleType().setUpliftAppliedDate(		convertorHelper.toDate(		getDTO().getUpliftAppliedDate()	    ));	
			getOracleType().setUpliftRemovedDate(		convertorHelper.toDate(		getDTO().getUpliftRemovedDate()	    ));	
			getOracleType().setFirstReminderDate(		convertorHelper.toDate(		getDTO().getFirstReminderDate()		));	
			getOracleType().setSecondReminderDate(		convertorHelper.toDate(		getDTO().getSecondReminderDate()	));
			getOracleType().setEnabled(					convertorHelper.toBoolean(	getDTO().getEnabled()	));
			getOracleType().setUpliftsAvailable(		convertorHelper.toBoolean(	getDTO().getUpliftsAvailable()	));
			
			if ( getDTO().getApplicantIncomeEvidenceList() != null )
			{
				IncomeEvidenceType[] 	ieTypes = new IncomeEvidenceType[ getDTO().getApplicantIncomeEvidenceList().size() ];
				Iterator<EvidenceDTO>	it		= getDTO().getApplicantIncomeEvidenceList().iterator();
				int idx = 0;
				
				IncomeEvidenceConvertor convertor = new IncomeEvidenceConvertor();
				while ( it.hasNext() )
				{
					convertor.setTypeFromDTO( it.next() );
					ieTypes[idx++] = convertor.getOracleType();
				}
				IncomeEvidenceTabType ieTabType = new IncomeEvidenceTabType( ieTypes );
				getOracleType().setApplicantIncomeEvidenceTab( ieTabType );
			}
			
			
			if ( getDTO().getPartnerIncomeEvidenceList() != null )
			{
				IncomeEvidenceType[] 	ieTypes = new IncomeEvidenceType[ getDTO().getPartnerIncomeEvidenceList().size() ];
				Iterator<EvidenceDTO>	it		= getDTO().getPartnerIncomeEvidenceList().iterator();
				int idx = 0;
				
				IncomeEvidenceConvertor convertor = new IncomeEvidenceConvertor();
				while ( it.hasNext() )
				{
					convertor.setTypeFromDTO( it.next() );
					ieTypes[idx++] = convertor.getOracleType();
				}
				IncomeEvidenceTabType ieTabType = new IncomeEvidenceTabType( ieTypes );
				getOracleType().setPartnerIncomeEvidenceTab( ieTabType );
			}
			
			
			if ( getDTO().getExtraEvidenceList() != null )
			{
				ExtraEvidenceType[] 	eeTypes = new ExtraEvidenceType[ getDTO().getExtraEvidenceList().size() ];
				Iterator<ExtraEvidenceDTO>	it		= getDTO().getExtraEvidenceList().iterator();
				int idx = 0;
				
				ExtraEvidenceConvertor 	eec		= new ExtraEvidenceConvertor();
				while ( it.hasNext() )
				{
					eec.setTypeFromDTO( it.next() );
					eeTypes[idx++] = eec.getOracleType();
				}
				ExtraEvidenceTabtype eeTabType = new ExtraEvidenceTabtype( eeTypes );
				getOracleType().setExtraEvidenceTab( eeTabType );
			}
			else
			{
				getOracleType().setExtraEvidenceTab( new ExtraEvidenceTabtype(  ) );
			}
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "IncomeEvidenceSummaryConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}
}
