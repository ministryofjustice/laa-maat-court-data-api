package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.oracle.CorrespondenceType;
import gov.uk.courtdata.dao.oracle.PrintDatesTabtype;
import gov.uk.courtdata.dao.oracle.PrintDatesType;
import gov.uk.courtdata.dto.application.CorrespondenceDTO;
import gov.uk.courtdata.dto.application.PrintDateDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author SWAN-D
 *
 */
public class CorrespondenceConvertor extends Convertor
{
	
	 private static Log log = LogFactory.getLog(CorrespondenceConvertor.class);

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public CorrespondenceDTO getDTO()
	{
		return (CorrespondenceDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public CorrespondenceType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new CorrespondenceType() );
		}
		
		if ( getDbType() instanceof CorrespondenceType )
		{
			return (CorrespondenceType)getDbType();
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
		if ( dto instanceof CorrespondenceDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + CorrespondenceDTO.class.getName());
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
		this.setDto( new CorrespondenceDTO() );	// create the new DTO

		try{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setId(				convertorHelper.toLong( getOracleType().getId() ));
			getDTO().setGeneratedDate(	convertorHelper.toDate(	getOracleType().getGeneratedDate() ));
			getDTO().setLastPrintDate(	convertorHelper.toDate(	getOracleType().getPrintedDate() ));
			getDTO().setTemplateName(   convertorHelper.toString( getOracleType().getTemplateName()  ));
			getDTO().setOriginalEmailDate(   convertorHelper.toDate( getOracleType().getOriginalEmailDate()  )); // MW - 30/03/2017 - FIP Changes
			
			CorrespondenceTypeConvertor typeConverter = new CorrespondenceTypeConvertor();
			if(getOracleType().getCorrespondenceTypeObject() != null){
				typeConverter.setDTOFromType(getOracleType().getCorrespondenceTypeObject());				
			}	
			getDTO().setCorrespondenceType(typeConverter.getDTO());
	
			PrintDateConvertor printDateConvertor = new  PrintDateConvertor();
			PrintDatesTabtype printDatesTabtype  = getOracleType().getPrintDatesTab();
	    	/*
	    	 * set an empty collection
	    	 */
			getDTO().setPrintDateDTOs(new ArrayList<PrintDateDTO>() );

			if ( printDatesTabtype != null ){
				
				PrintDatesType[] printDatesTypes = printDatesTabtype.getArray();
				for ( int i = 0; i < printDatesTypes.length; i++ ){
					
					printDateConvertor.setDTOFromType( printDatesTypes[i] );
					getDTO().getPrintDateDTOs().add( printDateConvertor.getDTO() );
				}
			}

			
			
		}
		catch (NullPointerException nex){
			throw new MAATApplicationException( "CrownCourtOverviewConvertor - the embedded dto is null");
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
			this.setDTO(dto);
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId( 				convertorHelper.toLong( getDTO().getId() ) );
			getOracleType().setGeneratedDate(	convertorHelper.toDate(	getDTO().getGeneratedDate() ));	
			getOracleType().setPrintedDate(		convertorHelper.toDate(	getDTO().getLastPrintDate() ));
			getOracleType().setTemplateName(    convertorHelper.toString(getDTO().getTemplateName() ));
			getOracleType().setOriginalEmailDate(    convertorHelper.toDate(getDTO().getOriginalEmailDate() )); // MW - 30/03/2017 - FIP Changes
			CorrespondenceTypeConvertor typeConverter = new CorrespondenceTypeConvertor();
			if(getOracleType().getCorrespondenceTypeObject() != null){
				typeConverter.setTypeFromDTO(getDTO().getCorrespondenceType());
			}
			getOracleType().setCorrespondenceTypeObject(typeConverter.getOracleType());
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "CrownCourtOverviewConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
