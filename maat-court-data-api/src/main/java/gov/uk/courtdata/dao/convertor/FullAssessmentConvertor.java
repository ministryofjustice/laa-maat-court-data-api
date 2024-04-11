package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.AssSectionSummaryTabType;
import gov.uk.courtdata.dao.oracle.AssSectionSummaryType;
import gov.uk.courtdata.dao.oracle.FullAssessmentType;
import gov.uk.courtdata.dto.application.AssessmentSectionSummaryDTO;
import gov.uk.courtdata.dto.application.FullAssessmentDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class FullAssessmentConvertor extends Convertor {

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public FullAssessmentDTO getDTO() {
		return (FullAssessmentDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public FullAssessmentType getOracleType() throws MAATApplicationException,
			MAATSystemException {
		
		if ( getDbType() == null )
		{
			setType( new FullAssessmentType() );
		}
		
		if ( getDbType() instanceof FullAssessmentType )
		{
			return (FullAssessmentType)getDbType();
		}
		else
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTO(java.lang.Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		if ( dto instanceof FullAssessmentDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + FullAssessmentDTO.class.getName());

	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTOFromType(java.lang.Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		
		this.setType( oracleType );
		this.setDto( new FullAssessmentDTO() );
		
		ConvertorHelper convertorHelper = new ConvertorHelper();
		try {
			
			getDTO().setCriteriaId(					convertorHelper.toLong	(getOracleType().getCriteriaId()));
			getDTO().setAssessmentDate(				convertorHelper.toDate	(getOracleType().getAssessmentDate()));
			getDTO().setAssessmentNotes(			convertorHelper.toString(getOracleType().getAssessmentNotes()));
			getDTO().setAdjustedLivingAllowance(	convertorHelper.toDouble(getOracleType().getAdjustedLivingAllowance()));
			getDTO().setOtherHousingNote(			convertorHelper.toString(getOracleType().getOtherHousingNote()));
			getDTO().setTotalAggregatedExpense(		convertorHelper.toDouble(getOracleType().getTotAggregatedExp()));
			getDTO().setTotalAnnualDisposableIncome(convertorHelper.toDouble(getOracleType().getTotAnnualDisposableInc()));
			getDTO().setThreshold(					convertorHelper.toDouble(getOracleType().getThreshold()));
			getDTO().setResult(						convertorHelper.toString(getOracleType().getResult()));
			getDTO().setResultReason(				convertorHelper.toString(getOracleType().getResultReason()));
			getDTO().setTimestamp(					convertorHelper.toZonedDateTime(getOracleType().getTimeStamp()));

			//section summaries
			getDTO().setSectionSummaries(			new ArrayList<AssessmentSectionSummaryDTO>());
			
			if(getOracleType().getSectionSummariesTab() != null)
			{
				AssSectionSummaryType[] 			sectionSummaryTypes 	= getOracleType().getSectionSummariesTab().getArray();
				AssessmentSectionSummaryConvertor sectionSummaryConverter = new AssessmentSectionSummaryConvertor();
				
				for(int i = 0; i < sectionSummaryTypes.length ; i++ )
				{
					sectionSummaryConverter.setDTOFromType(sectionSummaryTypes[i]);
					getDTO().getSectionSummaries().add( sectionSummaryConverter.getDTO());
				}
			}
			//Assessment Status
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getOracleType().getStatusObject() != null )
			{
				assConvertor.setDTOFromType( getOracleType().getStatusObject() );
			}
			getDTO().setAssessmnentStatusDTO( assConvertor.getDTO() ); // if type is null this will initialise a valid empty object
		
		} catch (SQLException e) {
			throw new MAATSystemException( e );
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setTypeFromDTO(java.lang.Object)
	 */
	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {
		
		try {
			setType( null );	// force new type to be instantiated 
			setDTO(dto);
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setCriteriaId(				convertorHelper.toLong	(getDTO().getCriteriaId()	));
			getOracleType().setAssessmentDate(			convertorHelper.toDate	(getDTO().getAssessmentDate()));
			getOracleType().setAssessmentNotes(			convertorHelper.toString(getDTO().getAssessmentNotes())); 
			getOracleType().setAdjustedLivingAllowance(	convertorHelper.toDouble(getDTO().getAdjustedLivingAllowance())); 
			getOracleType().setOtherHousingNote(		convertorHelper.toString(getDTO().getOtherHousingNote())); 
			getOracleType().setTotAggregatedExp(		convertorHelper.toDouble(getDTO().getTotalAggregatedExpense())); 
			getOracleType().setTotAnnualDisposableInc(	convertorHelper.toDouble(getDTO().getTotalAnnualDisposableIncome()));
			getOracleType().setThreshold(				convertorHelper.toDouble(getDTO().getThreshold())); 
			getOracleType().setResult(					convertorHelper.toString(getDTO().getResult())); 
			getOracleType().setResultReason(			convertorHelper.toString(getDTO().getResultReason())); 
		
			//Section summaries
			if(getDTO().getSectionSummaries() != null )
			{
				AssSectionSummaryType[] 				sectionSummaryTypes 	= new AssSectionSummaryType[getDTO().getSectionSummaries().size()];				
				AssessmentSectionSummaryConvertor 		sectionSummaryConverter = new AssessmentSectionSummaryConvertor();
				Iterator<AssessmentSectionSummaryDTO> 	it 						= getDTO().getSectionSummaries().iterator();
				int i = 0;
				
				while(it.hasNext())
				{
					sectionSummaryConverter.setTypeFromDTO(it.next());
					sectionSummaryTypes[i++] = sectionSummaryConverter.getOracleType();
				}
				getOracleType().setSectionSummariesTab(new AssSectionSummaryTabType(sectionSummaryTypes));
			}
			//Assessment Status
			AssessmentStatusConvertor statusConverter = new AssessmentStatusConvertor();
			if(getDTO().getAssessmnentStatusDTO() != null)
			{			
				statusConverter.setTypeFromDTO(getDTO().getAssessmnentStatusDTO());				
			}
			getOracleType().setStatusObject(statusConverter.getOracleType());
			
		} catch (SQLException e) {
			throw new MAATApplicationException(e);
		}

	}

}
