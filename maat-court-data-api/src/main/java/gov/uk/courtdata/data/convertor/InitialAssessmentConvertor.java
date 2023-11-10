/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.*;
import gov.uk.courtdata.dto.application.AssessmentSectionSummaryDTO;
import gov.uk.courtdata.dto.application.ChildWeightingDTO;
import gov.uk.courtdata.dto.application.InitialAssessmentDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class InitialAssessmentConvertor extends Convertor 
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public InitialAssessmentDTO getDTO() {
		return (InitialAssessmentDTO)getDto();
	}


	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public InitialAssessmentType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new InitialAssessmentType() );
		}
		
		if ( getDbType() instanceof InitialAssessmentType )
		{
			return (InitialAssessmentType)getDbType();
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
		if ( dto instanceof InitialAssessmentDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + InitialAssessmentDTO.class.getName());
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
		this.setDto( new InitialAssessmentDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setCriteriaId(						convertorHelper.toLong(		getOracleType().getCriteriaId() ));
			getDTO().setTimestamp(						getOracleType().getTimeStamp() );
			getDTO().setAssessmentDate                (	convertorHelper.toDate( 	getOracleType().getAssessmentDate() ));
			getDTO().setOtherBenefitNote              (	convertorHelper.toString( 	getOracleType().getOtherBenefitNote() ));
			getDTO().setOtherIncomeNote               (	convertorHelper.toString( 	getOracleType().getOtherIncomeNote() ));
			getDTO().setTotalAggregatedIncome         (	convertorHelper.toDouble( 	getOracleType().getTotAggregatedIncome() ));
			getDTO().setAdjustedIncomeValue           (	convertorHelper.toDouble( 	getOracleType().getAdjustedIncomeValue() ));
			getDTO().setNotes                         (	convertorHelper.toString( 	getOracleType().getNotes() ));
			getDTO().setLowerThreshold                (	convertorHelper.toDouble( 	getOracleType().getLowerThreshold() ));
			getDTO().setUpperThreshold                (	convertorHelper.toDouble( 	getOracleType().getUpperThreshold() ));
			getDTO().setResult                        (	convertorHelper.toString( 	getOracleType().getResult() ));
			getDTO().setResultReason                  (	convertorHelper.toString( 	getOracleType().getResultReason() ));


			NewWorkReasonConvertor mwrConvertor	= new NewWorkReasonConvertor();
			if ( getOracleType().getNewWorkReasonObject() != null )
			{
				mwrConvertor.setDTOFromType( getOracleType().getNewWorkReasonObject() );
			}
			getDTO().setNewWorkReason( mwrConvertor.getDTO() ); // if type is null this will initialise a valid empty object
			
			ReviewTypeConvertor reviewTypeConvertor	= new ReviewTypeConvertor();
			if ( getOracleType().getReviewTypeObject() != null )
			{
				reviewTypeConvertor.setDTOFromType( getOracleType().getReviewTypeObject() );
			}
			getDTO().setReviewType(reviewTypeConvertor.getDTO() );
			
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getOracleType().getStatusObject() != null )
			{
				assConvertor.setDTOFromType( getOracleType().getStatusObject() );
			}
			getDTO().setAssessmnentStatusDTO( assConvertor.getDTO() ); // if type is null this will initialise a valid empty object
			
			/*
			 * set up empty collection for he income evidence table
			 */
			getDTO().setSectionSummaries( new ArrayList<AssessmentSectionSummaryDTO>() );
			
			if(getOracleType().getSectionSummariesTab() != null)
			{
				AssSectionSummaryType[] sectionSummaryTypes = getOracleType().getSectionSummariesTab().getArray();
				AssessmentSectionSummaryConvertor sectionSummaryConverter = new AssessmentSectionSummaryConvertor();
				
				for(int i = 0; i < sectionSummaryTypes.length ; i++ )
				{
					sectionSummaryConverter.setDTOFromType(sectionSummaryTypes[i]);
					getDTO().getSectionSummaries().add( sectionSummaryConverter.getDTO());
				}
			}

			/*
			 * set up empty collection for he income evidence table
			 */
			getDTO().setChildWeightings(	new ArrayList<ChildWeightingDTO>() );
			
			if(getOracleType().getChildWeightingTab() != null)
			{
				AssessmentChildWeightingType[] childWeightings 	= getOracleType().getChildWeightingTab().getArray();
				ChildWeightingConvertor weightConverter 		= new ChildWeightingConvertor();
				
				for(int i = 0; i < childWeightings.length ; i++ )
				{
					weightConverter.setDTOFromType(childWeightings[i]);
					
					getDTO().getChildWeightings().add( weightConverter.getDTO());
				}
			}

		}
		catch (NegativeArraySizeException nasx )
		{
			throw new MAATSystemException( nasx );
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
			setDTO( dto );	
			ConvertorHelper convertorHelper 		= new ConvertorHelper();

			getOracleType().setCriteriaId			 ( convertorHelper.toLong(      getDTO().getCriteriaId			   () ));
			getOracleType().setAssessmentDate        ( convertorHelper.toDate( 	  	getDTO().getAssessmentDate         () ) );
			getOracleType().setOtherBenefitNote      ( convertorHelper.toString( 	getDTO().getOtherBenefitNote       () ) );
			getOracleType().setOtherIncomeNote       ( convertorHelper.toString( 	getDTO().getOtherIncomeNote        () ) );
			getOracleType().setTotAggregatedIncome   ( convertorHelper.toDouble( 	getDTO().getTotalAggregatedIncome  () ) );
			getOracleType().setAdjustedIncomeValue   ( convertorHelper.toDouble( 	getDTO().getAdjustedIncomeValue    () ) );
			getOracleType().setNotes                 ( convertorHelper.toString( 	getDTO().getNotes                  () ) );
			getOracleType().setLowerThreshold        ( convertorHelper.toDouble( 	getDTO().getLowerThreshold         () ) );
			getOracleType().setUpperThreshold        ( convertorHelper.toDouble( 	getDTO().getUpperThreshold         () ) );
			getOracleType().setResult                ( convertorHelper.toString( 	getDTO().getResult                 () ) );
			getOracleType().setResultReason          ( convertorHelper.toString( 	getDTO().getResultReason           () ) );


			NewWorkReasonConvertor mwrConvertor	= new NewWorkReasonConvertor();
			if ( getDTO().getNewWorkReason() != null )
			{
				mwrConvertor.setTypeFromDTO( getDTO().getNewWorkReason() );
			}
			getOracleType().setNewWorkReasonObject( mwrConvertor.getOracleType() ); // if type is null this will initialise a valid empty object
			
			ReviewTypeConvertor reviewTypeConvertor	= new ReviewTypeConvertor();
			if ( getDTO().getReviewType() != null )
			{
				reviewTypeConvertor.setTypeFromDTO( getDTO().getReviewType() );
			}
			getOracleType().setReviewTypeObject( reviewTypeConvertor.getOracleType() ); 
		
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getDTO().getAssessmnentStatusDTO() != null )
			{
				assConvertor.setTypeFromDTO( getDTO().getAssessmnentStatusDTO() );
			}
			getOracleType().setStatusObject( assConvertor.getOracleType() ); // if type is null this will initialise a valid empty object
			

			//Section summaries
			
			if( getDTO().getSectionSummaries() != null)
			{
				AssSectionSummaryType[] 				sectionSummaryTypes 	= new AssSectionSummaryType[getDTO().getSectionSummaries().size()];				
				Iterator<AssessmentSectionSummaryDTO> 	It 						= getDTO().getSectionSummaries().iterator();
				int i = 0;
				
				AssessmentSectionSummaryConvertor 		sectionSummaryConverter = new AssessmentSectionSummaryConvertor();
				while(It.hasNext())
				{
					sectionSummaryConverter.setTypeFromDTO(It.next());
					sectionSummaryTypes[i++] = sectionSummaryConverter.getOracleType();
				}
				getOracleType().setSectionSummariesTab(new AssSectionSummaryTabType(sectionSummaryTypes));
			}

			

			if(getDTO().getChildWeightings() != null && getDTO().getChildWeightings().size() > 0)
			{
				AssessmentChildWeightingType[] assChildWeightingTypes 	= new AssessmentChildWeightingType[getDTO().getChildWeightings().size()];								
				Iterator<ChildWeightingDTO> 	it 						= getDTO().getChildWeightings().iterator();
				int i = 0;

				ChildWeightingConvertor weightConverter  = new ChildWeightingConvertor();
				while( it.hasNext() )
				{
					weightConverter.setTypeFromDTO(it.next());
					assChildWeightingTypes[i++] = weightConverter.getOracleType();
				}
				getOracleType().setChildWeightingTab(new AssChildWeightingTabType(assChildWeightingTypes));
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

}
