package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.AssSectionSummaryType;
import gov.uk.courtdata.data.oracle.AssessmentDetailTabType;
import gov.uk.courtdata.data.oracle.AssessmentDetailType;
import gov.uk.courtdata.dto.application.AssessmentDetailDTO;
import gov.uk.courtdata.dto.application.AssessmentSectionSummaryDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class AssessmentSectionSummaryConvertor extends Convertor {

	@Override
	public AssessmentSectionSummaryDTO getDTO() {

		return (AssessmentSectionSummaryDTO) this.getDto();
	}

	@Override
	public AssSectionSummaryType getOracleType() throws MAATApplicationException,
			MAATSystemException {
		
		if ( getDbType() == null )
		{
			setType( new AssSectionSummaryType() );
		}
		
		if ( getDbType() instanceof AssSectionSummaryType )
		{
			return (AssSectionSummaryType)getDbType();
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

	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		
		if ( dto instanceof AssessmentSectionSummaryDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + AssessmentSectionSummaryDTO.class.getName());

	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new AssessmentSectionSummaryDTO() );	// create the new DTO


			ConvertorHelper convertorHelper = new ConvertorHelper();
			try {
				getDTO().setSection(				convertorHelper.toString(getOracleType().getSection()));
				getDTO().setApplicantAnnualTotal(	convertorHelper.toDouble(getOracleType().getAppAnnualTotal()));
				getDTO().setPartnerAnnualTotal(		convertorHelper.toDouble(getOracleType().getPartnerAnnualTotal()));
				getDTO().setAnnualTotal(			convertorHelper.toDouble(getOracleType().getAnnualTotal()));
			
				getDTO().setAssessmentDetail(new ArrayList<AssessmentDetailDTO>());
				
				AssessmentDetailTabType assessmentDetailTab = getOracleType().getAssessmentDetailTab();
				AssessmentDetailType[] assessmentDetailTypes = assessmentDetailTab.getArray();
				
				AssessmentDetailConvertor assessmentDetailConverter = new AssessmentDetailConvertor();
				for(int i=0; i < assessmentDetailTypes.length;i++){
					assessmentDetailConverter.setDTOFromType(assessmentDetailTypes[i]);
					getDTO().getAssessmentDetail().add(assessmentDetailConverter.getDTO());
				}
			
			} catch (SQLException e) {
				throw new MAATApplicationException(e);
			}

	}

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {

        try 
        {
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setSection(            convertorHelper.toString( getDTO().getSection()));
			getOracleType().setAppAnnualTotal(     convertorHelper.toDouble( getDTO().getApplicantAnnualTotal()));
			getOracleType().setPartnerAnnualTotal( convertorHelper.toDouble( getDTO().getPartnerAnnualTotal()));
			getOracleType().setAnnualTotal(        convertorHelper.toDouble( getDTO().getAnnualTotal()));
			
			
			Collection<AssessmentDetailDTO> assessmentDetails = getDTO().getAssessmentDetail();
			
			if(assessmentDetails != null && assessmentDetails.size() > 0){
				AssessmentDetailType[] assessmentDetailTypes = new AssessmentDetailType[assessmentDetails.size()];
				Iterator<AssessmentDetailDTO> It = assessmentDetails.iterator();
				int i = 0;
				while(It.hasNext()){
					AssessmentDetailConvertor assessmentDetailConverter = new AssessmentDetailConvertor();
					assessmentDetailConverter.setTypeFromDTO(It.next());
					assessmentDetailTypes[i++] = assessmentDetailConverter.getOracleType();
				}
				getOracleType().setAssessmentDetailTab(new AssessmentDetailTabType(assessmentDetailTypes));
			}
					
        } catch (SQLException e) {
			throw new MAATApplicationException(e); 
		}  
	}

}
