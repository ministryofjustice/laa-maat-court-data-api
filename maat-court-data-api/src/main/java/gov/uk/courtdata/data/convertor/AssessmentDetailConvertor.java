package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.AssessmentDetailType;
import gov.uk.courtdata.data.oracle.FrequencyType;
import gov.uk.courtdata.dto.application.AssessmentDetailDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class AssessmentDetailConvertor extends Convertor {

	@Override
	public AssessmentDetailDTO getDTO() {
		
		return (AssessmentDetailDTO) getDto();
	}

	@Override
	public AssessmentDetailType getOracleType() throws MAATApplicationException,
			MAATSystemException {

		if ( getDbType() == null )
		{
			setType( new AssessmentDetailType() );
		}
		
		if ( getDbType() instanceof AssessmentDetailType )
		{
			return (AssessmentDetailType)getDbType();
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
		if ( dto instanceof AssessmentDetailDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + AssessmentDetailDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new AssessmentDetailDTO() );	// create the new DTO

		ConvertorHelper convertorHelper = new ConvertorHelper();
		try {
			getDTO().setId(					convertorHelper.toLong(getOracleType().getId()));
			getDTO().setCriteriaDetailsId(	convertorHelper.toLong(getOracleType().getCriteriaDetailId()));
			getDTO().setApplicantAmount(	convertorHelper.toDouble(getOracleType().getApplicantAmount()));
			getDTO().setPartnerAmount(		convertorHelper.toDouble(getOracleType().getPartnerAmount()));
			getDTO().setDescription(		convertorHelper.toString(getOracleType().getDescription()));
			getDTO().setDetailCode(			convertorHelper.toString(getOracleType().getDetailCode()));
			
			getDTO().setTimestamp(			getOracleType().getTimeStamp());
			
			FrequencyType applicantfreqType = getOracleType().getApplicantFreqObject();
			FrequencyConvertor frequencyConverter = new FrequencyConvertor();
			frequencyConverter.setDTOFromType(applicantfreqType);
			getDTO().setApplicantFrequency(frequencyConverter.getDTO());

			FrequencyType partnerfreqType = getOracleType().getPartnerFreqObject();
			frequencyConverter.setDTOFromType(partnerfreqType);
			getDTO().setPartnerFrequency(frequencyConverter.getDTO());
			
		} catch (SQLException e) {
			throw new MAATApplicationException(e);
		}
	}

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {

		try 
		{
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			setType( null );	// force new type to be instantiated 
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId(                 convertorHelper.toLong(	getDTO().getId()));		        
	        getOracleType().setCriteriaDetailId(   convertorHelper.toLong(	getDTO().getCriteriaDetailsId()));	
	        getOracleType().setApplicantAmount(    convertorHelper.toDouble(getDTO().getApplicantAmount()));
	        getOracleType().setDescription(        convertorHelper.toString(getDTO().getDescription()));	                       
	        getOracleType().setDetailCode(         convertorHelper.toString(getDTO().getDetailCode()));	                       
	        getOracleType().setPartnerAmount(      convertorHelper.toDouble(getDTO().getPartnerAmount()));
	        getOracleType().setTimeStamp(          getDTO().getTimestamp());	
	        
			FrequencyConvertor frequencyConverter = new FrequencyConvertor();
			if(getDTO().getApplicantFrequency() != null)
			{
				frequencyConverter.setTypeFromDTO(getDTO().getApplicantFrequency());
			}
	        getOracleType().setApplicantFreqObject(frequencyConverter.getOracleType());	
	
	        if(getDTO().getPartnerFrequency() != null)
	        {
	        	frequencyConverter.setTypeFromDTO(getDTO().getPartnerFrequency());
	        }
	        getOracleType().setPartnerFreqObject( frequencyConverter.getOracleType());	
		
		} catch (SQLException e) {
			throw new MAATApplicationException(e);
		}
	}

}
