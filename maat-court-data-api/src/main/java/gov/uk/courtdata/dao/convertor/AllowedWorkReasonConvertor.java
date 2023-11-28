/**
 * 
 */
package gov.uk.courtdata.dao.convertor;


import gov.uk.courtdata.dao.oracle.AllowedWorkReasonType;
import gov.uk.courtdata.dao.oracle.NewWorkReasonTabtype;
import gov.uk.courtdata.dao.oracle.NewWorkReasonType;
import gov.uk.courtdata.dto.application.AllowedWorkReasonDTO;
import gov.uk.courtdata.dto.application.NewWorkReasonDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author obod-l
 *
 */
public class AllowedWorkReasonConvertor extends Convertor {

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public AllowedWorkReasonDTO getDTO() {
		
		return (AllowedWorkReasonDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public AllowedWorkReasonType getOracleType() throws MAATApplicationException, MAATSystemException{
		
		if ( getDbType() == null ){
			
			setType( new AllowedWorkReasonType() );
		}
		
		if ( getDbType() instanceof AllowedWorkReasonType )	{
			
			return (AllowedWorkReasonType)getDbType();
		}else{
			
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
	public void setDTO(Object dto) throws MAATApplicationException {
		
		if ( dto instanceof AllowedWorkReasonDTO  )
			
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + AllowedWorkReasonDTO.class.getName());
	
	}

	
	

	/**
	 * Updates the local instance of the DTO by converting the dao in the
	 * oracle type object passed as a parameter
	 * @see Convertor#setDTOFromType(Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType) throws MAATApplicationException, MAATSystemException	{
		// save it
		this.setType( oracleType );
		this.setDto( new AllowedWorkReasonDTO() ); // create the new DTO

		try{

			/*
			 * Allowed Work Reason for Passport
			 */ 
			NewWorkReasonTabtype allowedWorkReasonPassportTabtype = getOracleType().getPassport();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setPassportWorkReason(new ArrayList<NewWorkReasonDTO>());
	    	
	    	if ( allowedWorkReasonPassportTabtype != null ){
	    		
	    		NewWorkReasonType[]  newWorkReasonTypes = allowedWorkReasonPassportTabtype.getArray();
	    		
	    		NewWorkReasonConvertor newWorkReasonConvertor = new NewWorkReasonConvertor();
	    		
	    		for ( int i = 0; i < newWorkReasonTypes.length ; i++ ) {
	    			
	    			newWorkReasonConvertor.setDTOFromType(newWorkReasonTypes[i] );
	    			getDTO().getPassportWorkReason().add(newWorkReasonConvertor.getDTO());
	    		}
			
	    	}
	    	
	    	/*
			 * Allowed Work Reason for Means Assessment
			 */ 
			NewWorkReasonTabtype  allowedWorkReasonMeansTabtype = getOracleType().getMeans();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setMeansAssessmentWorkReason(new ArrayList<NewWorkReasonDTO>());
	    	
	    	if ( allowedWorkReasonMeansTabtype != null ){
	    		
	    		NewWorkReasonType[]  newWorkReasonTypes = allowedWorkReasonMeansTabtype.getArray();
	    		
	    		NewWorkReasonConvertor newWorkReasonConvertor = new NewWorkReasonConvertor();
	    		
	    		for ( int i = 0; i < newWorkReasonTypes.length ; i++ ) {
	    			
	    			newWorkReasonConvertor.setDTOFromType(newWorkReasonTypes[i] );
	    			getDTO().getMeansAssessmentWorkReason().add(newWorkReasonConvertor.getDTO());
	    		}
			
	    	}			
	    	
	    	
	    	/*
			 * Allowed Work Reason for Eligibility
			 */ 
			NewWorkReasonTabtype  allowedWorkReasonEligibilityTabtype = getOracleType().getEligibility();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setEligibilityWorkReason(new ArrayList<NewWorkReasonDTO>());
	    	
	    	if ( allowedWorkReasonEligibilityTabtype != null ){
	    		
	    		NewWorkReasonType[]  newWorkReasonTypes = allowedWorkReasonEligibilityTabtype.getArray();
	    		
	    		NewWorkReasonConvertor newWorkReasonConvertor = new NewWorkReasonConvertor();
	    		
	    		for ( int i = 0; i < newWorkReasonTypes.length ; i++ ) {
	    			
	    			newWorkReasonConvertor.setDTOFromType(newWorkReasonTypes[i] );
	    			getDTO().getEligibilityWorkReason().add(newWorkReasonConvertor.getDTO());
	    		}
			
	    	}		
	    	
	    	allowedWorkReasonForCrownCourtHardship();
	    	
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}		
	}

	
	private void allowedWorkReasonForCrownCourtHardship() throws MAATApplicationException, MAATSystemException, SQLException {
		
		/*
		 * Allowed Work Reason for Crown Court Hardship
		 */ 
		NewWorkReasonTabtype  allowedWorkReasonCrownCourtHardshipTabtype = getOracleType().getCchardship();
    	/*
    	 * set an empty collection
    	 */
		getDTO().setCrownCourtHardshipWorkReason(new ArrayList<NewWorkReasonDTO>());
    	
    	if ( allowedWorkReasonCrownCourtHardshipTabtype != null ){
    		
    		NewWorkReasonType[]  newWorkReasonTypes = allowedWorkReasonCrownCourtHardshipTabtype.getArray();
    		
    		NewWorkReasonConvertor newWorkReasonConvertor = new NewWorkReasonConvertor();
    		
    		for ( int i = 0; i < newWorkReasonTypes.length ; i++ ) {
    			
    			newWorkReasonConvertor.setDTOFromType(newWorkReasonTypes[i] );
    			getDTO().getCrownCourtHardshipWorkReason().add(newWorkReasonConvertor.getDTO());
    		}
		
    	}		
		
	}
	
	
	
	
	/**
	 * Updates the local instance of the Oracle type by converting the dao in the
	 * dto object passed as a parameter
	 * @see Convertor#setTypeFromDTO(Object)
	 */
	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,	MAATSystemException {
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try {
			
			setType( null );	// force new type to be instantiated 
			setDTO( dto );
			
			/*
			 * Allowed Work Reason for Passport
			 */
	    	if(getDTO().getPassportWorkReason() != null){
	   
	    		NewWorkReasonType[]  newWorkReasonTypes = new NewWorkReasonType[getDTO().getPassportWorkReason().size()];
	   
	    		Iterator<NewWorkReasonDTO> itr =  getDTO().getPassportWorkReason().iterator();
	    		
	    		int	idx = 0;
	    		while ( ( itr != null ) && ( itr.hasNext() ) ){
	    			
	    			NewWorkReasonConvertor newWorkReasonConvertor = new NewWorkReasonConvertor();
	    			NewWorkReasonDTO newWorkReasonDTO  =  itr.next();
	    			newWorkReasonConvertor.setTypeFromDTO(newWorkReasonDTO);
	    			newWorkReasonTypes[idx++] = newWorkReasonConvertor.getOracleType();
	    		}
	   
	    		NewWorkReasonTabtype newWorkReasonTabtype = new NewWorkReasonTabtype(newWorkReasonTypes);
	    		getOracleType().setPassport(newWorkReasonTabtype);
	    	}
	    	
	    	
	    	/*
			 * Allowed Work Reason for Means Assessment
			 */
	    	if(getDTO().getMeansAssessmentWorkReason() != null){
	   
	    		NewWorkReasonType[]  newWorkReasonTypes = new NewWorkReasonType[getDTO().getMeansAssessmentWorkReason().size()];
	   
	    		Iterator<NewWorkReasonDTO> itr =  getDTO().getMeansAssessmentWorkReason().iterator();
	    		
	    		int	idx = 0;
	    		while ( ( itr != null ) && ( itr.hasNext() ) ){
	    			
	    			NewWorkReasonConvertor newWorkReasonConvertor = new NewWorkReasonConvertor();
	    			NewWorkReasonDTO newWorkReasonDTO  =  itr.next();
	    			newWorkReasonConvertor.setTypeFromDTO(newWorkReasonDTO);
	    			newWorkReasonTypes[idx++] = newWorkReasonConvertor.getOracleType();
	    		}
	   
	    		NewWorkReasonTabtype newWorkReasonTabtype = new NewWorkReasonTabtype(newWorkReasonTypes);
	    		getOracleType().setMeans(newWorkReasonTabtype);
	    	}
	    	
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
		}catch (SQLException ex ){
			throw new MAATSystemException( ex );
		}
	}


}
