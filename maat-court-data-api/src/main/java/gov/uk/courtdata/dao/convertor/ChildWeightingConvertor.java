package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.AssessmentChildWeightingType;
import gov.uk.courtdata.dto.application.ChildWeightingDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class ChildWeightingConvertor extends Convertor
{

	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public ChildWeightingDTO getDTO()
	{
		return (ChildWeightingDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public AssessmentChildWeightingType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new AssessmentChildWeightingType() );
		}
		
		if ( getDbType() instanceof AssessmentChildWeightingType )
		{
			return (AssessmentChildWeightingType)getDbType();
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
		if ( dto instanceof ChildWeightingDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ChildWeightingDTO.class.getName());
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
		this.setDto( new ChildWeightingDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setId               (convertorHelper.toLong(		getOracleType().getId               () ));
			getDTO().setWeightingId      (convertorHelper.toLong(		getOracleType().getChildWeightingId     () ));
			getDTO().setLowerAgeRange    (convertorHelper.toInteger(	getOracleType().getLowerAgeRange    () ));
			getDTO().setUpperAgeRange    (convertorHelper.toInteger(	getOracleType().getUpperAgeRange    () ));
			getDTO().setWeightingFactor  (convertorHelper.toDouble(		getOracleType().getWeightingFactor  () ));
			getDTO().setNoOfChildren     (convertorHelper.toInteger(	getOracleType().getNoOfChildren     () ));
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "NewWorkReasonConvertor - the embedded dto is null");
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
			getOracleType().setId               (convertorHelper.toLong(		getDTO().getId               () ));
			getOracleType().setChildWeightingId (convertorHelper.toLong(		getDTO().getWeightingId () ));
			getOracleType().setLowerAgeRange    (convertorHelper.toInteger(	    getDTO().getLowerAgeRange    () ));
			getOracleType().setUpperAgeRange    (convertorHelper.toInteger(	    getDTO().getUpperAgeRange    () ));
			getOracleType().setWeightingFactor  (convertorHelper.toDouble(		getDTO().getWeightingFactor  () ));
			getOracleType().setNoOfChildren     (convertorHelper.toInteger(	    getDTO().getNoOfChildren     () ));
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "NewWorkReasonConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
