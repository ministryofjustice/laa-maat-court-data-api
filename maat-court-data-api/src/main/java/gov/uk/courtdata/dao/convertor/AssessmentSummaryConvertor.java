/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.AssessmentSummaryType;
import gov.uk.courtdata.dto.application.AssessmentSummaryDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class AssessmentSummaryConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public AssessmentSummaryDTO getDTO()
	{
		return (AssessmentSummaryDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public AssessmentSummaryType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new AssessmentSummaryType() );
		}
		
		if ( getDbType() instanceof AssessmentSummaryType )
		{
			return (AssessmentSummaryType)getDbType();
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
		if ( dto instanceof AssessmentSummaryDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + AssessmentSummaryDTO.class.getName());
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
		this.setDto( new AssessmentSummaryDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
	         
			getDTO().setId(             convertorHelper.toLong(getOracleType().getId()                   ));
			getDTO().setType(			convertorHelper.toString( 	getOracleType().getType()    		));
			getDTO().setStatus(			convertorHelper.toString( 	getOracleType().getStatus()    		));
			getDTO().setResult(			convertorHelper.toString( 	getOracleType().getResult() 		));
			getDTO().setAssessmentDate(	convertorHelper.toDate(		getOracleType().getAssessmentDate()	));
			getDTO().setReviewType(		convertorHelper.toString( 	getOracleType().getReviewType()    		));
			
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "AssessmentSummaryConvertor - the embedded dto is null");
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
			getOracleType().setId(              convertorHelper.toLong(     getDTO().getId()            ));
			getOracleType().setType(			convertorHelper.toString( 	getDTO().getType()    		));
			getOracleType().setStatus(			convertorHelper.toString( 	getDTO().getStatus()    	));
			getOracleType().setResult(			convertorHelper.toString( 	getDTO().getResult() 		));
			getOracleType().setAssessmentDate(	convertorHelper.toDate(		getDTO().getAssessmentDate()));	
			getOracleType().setReviewType(		convertorHelper.toString( 	getDTO().getReviewType()	));
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "AssessmentSummaryConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}



}
