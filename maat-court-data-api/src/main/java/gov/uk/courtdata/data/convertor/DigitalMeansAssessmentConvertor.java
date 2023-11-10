/*
 * Mark Whitaker  - 30/03/2017
 * 
 * Introduced as part of the Future Initiatives Project (FIP) to support passing of
 * digital means assessments between the application and the database.
 * 
 */

package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.oracle.DigiMeansAssessType;
import gov.uk.courtdata.dto.application.DigitisedMeansAssessmentDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;


public class DigitalMeansAssessmentConvertor extends Convertor 
{
	
	 private static Log log = LogFactory.getLog(DigitalMeansAssessmentConvertor.class);

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public DigitisedMeansAssessmentDTO getDTO()
	{
		return (DigitisedMeansAssessmentDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public DigiMeansAssessType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new DigiMeansAssessType() );
		}
		
		if ( getDbType() instanceof DigiMeansAssessType )
		{
			return (DigiMeansAssessType)getDbType();
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
		if ( dto instanceof DigitisedMeansAssessmentDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + DigitisedMeansAssessmentDTO.class.getName());
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
		this.setDto( new DigitisedMeansAssessmentDTO() );	// create the new DTO

		try{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setId(convertorHelper.toLong( getOracleType().getId() ));
			getDTO().setMaatId(convertorHelper.toLong( getOracleType().getMaatId() ));
			getDTO().setDateCreated(convertorHelper.toDate( getOracleType().getDateCreated() ));
			getDTO().setOriginalEmailDate(convertorHelper.toDate( getOracleType().getOriginalEmailDate() ));	
		}
		catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "CrownCourtOverviewConvertor - the embedded dto is null");
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
			this.setDTO(dto);
			ConvertorHelper convertorHelper = new ConvertorHelper();
	
			getOracleType().setId(convertorHelper.toLong(getDTO().getId()));
			getOracleType().setMaatId(convertorHelper.toLong(getDTO().getMaatId()));
			getOracleType().setDateCreated(convertorHelper.toDate(getDTO().getDateCreated()));
			getOracleType().setOriginalEmailDate(convertorHelper.toDate(getDTO().getOriginalEmailDate()));
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "CrownCourtOverviewConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
