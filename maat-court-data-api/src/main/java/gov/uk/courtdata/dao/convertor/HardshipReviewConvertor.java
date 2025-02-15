package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.HRSectionTabType;
import gov.uk.courtdata.dao.oracle.HardshipReviewType;
import gov.uk.courtdata.dao.oracle.HrSectionType;
import gov.uk.courtdata.dto.application.ApplicationHardshipReviewDTO;
import gov.uk.courtdata.dto.application.HRSectionDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class HardshipReviewConvertor extends Convertor {

	@Override
	public ApplicationHardshipReviewDTO getDTO()
	{
		return (ApplicationHardshipReviewDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public HardshipReviewType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new HardshipReviewType() );
		}
		
		if ( getDbType() instanceof HardshipReviewType )
		{
			return (HardshipReviewType)getDbType();
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
		if ( dto instanceof ApplicationHardshipReviewDTO)
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " 
												+ dto.getClass().getName() 
												+ " instead of " 
												+ ApplicationHardshipReviewDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new ApplicationHardshipReviewDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setId(convertorHelper.toLong(getOracleType().getId()));
			getDTO().setCmuId(convertorHelper.toLong(getOracleType().getCmuId()));
			getDTO().setReviewResult(convertorHelper.toString(getOracleType().getReviewResult()));
			getDTO().setReviewDate(convertorHelper.toDate(getOracleType().getReviewDate()));
			getDTO().setNotes(convertorHelper.toString(getOracleType().getNotes()));
			getDTO().setDecisionNotes(convertorHelper.toString(	getOracleType().getDecisionNotes()));
			getDTO().setDisposableIncome(getOracleType().getDisposIncome());
			getDTO().setDisposableIncomeAfterHardship(getOracleType().getDisposIncomeAfterHardship());
			
			//New Work Reason
			NewWorkReasonConvertor mwrConvertor	= new NewWorkReasonConvertor();
			if ( getOracleType().getNewWorkReasonObject() != null )
			{
				mwrConvertor.setDTOFromType( getOracleType().getNewWorkReasonObject() );
			}
			getDTO().setNewWorkReason( mwrConvertor.getDTO() ); // if type is null this will initialise a valid empty object
			
			//Hr Solicitors Costs
			HRSolictorsCostsConvertor solConvertor	= new HRSolictorsCostsConvertor();
			if ( getOracleType().getSolicitorCostsObject() != null )
			{
				solConvertor.setDTOFromType( getOracleType().getSolicitorCostsObject());
			}
			getDTO().setSolictorsCosts( solConvertor.getDTO() ); // if type is null this will initialise a valid empty object			
			
			//Assessment Status
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getOracleType().getStatusObject() != null )
			{
				assConvertor.setDTOFromType( getOracleType().getStatusObject() );
			}
			getDTO().setAsessmentStatus( assConvertor.getDTO() ); // if type is null this will initialise a valid empty object
			
			//HrSection
			getDTO().setSection( new ArrayList<HRSectionDTO>() );
			
			if(getOracleType().getSectionTab() != null)
			{
				HrSectionType[] 	sectionTypes 		= getOracleType().getSectionTab().getArray();
				HRSectionConvertor sectionConverter 	= new HRSectionConvertor();
				
				for(int i = 0; i < sectionTypes.length ; i++ )
				{
					sectionConverter.setDTOFromType(sectionTypes[i]);
					getDTO().getSection().add( sectionConverter.getDTO());
				}
			}

		}
		catch (NullPointerException nex)
		{
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
			ConvertorHelper convertorHelper = new ConvertorHelper();            
			getOracleType().setId(convertorHelper.toLong(getDTO().getId()));
			getOracleType().setCmuId(convertorHelper.toLong(getDTO().getCmuId()));
			getOracleType().setReviewResult(convertorHelper.toString(getDTO().getReviewResult()));
			getOracleType().setReviewDate(convertorHelper.toDate(getDTO().getReviewDate()));
			getOracleType().setNotes(convertorHelper.toString(getDTO().getNotes()));
			getOracleType().setDecisionNotes(convertorHelper.toString(getDTO().getDecisionNotes()));
			getOracleType().setDisposIncome(getDTO().getDisposableIncome());
			getOracleType().setDisposIncomeAfterHardship(getDTO().getDisposableIncomeAfterHardship());
			
			//New Work Reason
			NewWorkReasonConvertor mwrConvertor	= new NewWorkReasonConvertor();
			if ( getDTO().getNewWorkReason() != null )
			{
				mwrConvertor.setTypeFromDTO( getDTO().getNewWorkReason() );
			}
			getOracleType().setNewWorkReasonObject( mwrConvertor.getOracleType() ); // if type is null this will initialise a valid empty object
			
			//Assessment Status
			AssessmentStatusConvertor statusConverter = new AssessmentStatusConvertor();
			if(getDTO().getAsessmentStatus() != null)
			{	
				statusConverter.setTypeFromDTO(getDTO().getAsessmentStatus());				
			}
			getOracleType().setStatusObject(statusConverter.getOracleType());
			
			//Hr Solicitors Costs
			HRSolictorsCostsConvertor solConverter = new HRSolictorsCostsConvertor();
			if(getDTO().getSolictorsCosts() != null)
			{				
				solConverter.setTypeFromDTO(getDTO().getSolictorsCosts());				
			}
			getOracleType().setSolicitorCostsObject(solConverter.getOracleType());
			
			//HrSection
			
			Collection<HRSectionDTO> sections = getDTO().getSection();
			if ( getDTO().getSection() != null )
			{
				HrSectionType[] 		sectionTypes 		= new HrSectionType[sections.size()];
				HRSectionConvertor 		sectionConverter 	= new HRSectionConvertor();
				Iterator<HRSectionDTO> 	it 					= sections.iterator();
				int i = 0;

				while(it.hasNext())
				{
					sectionConverter.setTypeFromDTO(it.next());
					sectionTypes[i++] = sectionConverter.getOracleType();
				}
				getOracleType().setSectionTab(new HRSectionTabType(sectionTypes));
			}
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( this.getClass().getName() + " - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
