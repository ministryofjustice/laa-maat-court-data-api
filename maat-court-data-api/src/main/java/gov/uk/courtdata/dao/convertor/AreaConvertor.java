package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.AreaType;
import gov.uk.courtdata.dao.oracle.CMUTabType;
import gov.uk.courtdata.dao.oracle.CmuType;
import gov.uk.courtdata.dto.application.AreaDTO;
import gov.uk.courtdata.dto.application.CaseManagementUnitDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents an administrative area
 * @author OBOD-L
 *
 */
public class AreaConvertor extends Convertor
{

	/** default constructor */
	public AreaConvertor() {
	}

	/**
	 * This will create a local instance of the DTO appropriate for this convertor
	 * @see Convertor#getDto()
	 */
	@Override
	public AreaDTO getDTO()
	{
		return (AreaDTO)this.getDto();
	}



	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDto(java.lang.Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException
	{
		if ( dto instanceof AreaDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + AreaDTO.class.getName());
	}

	
	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.dto.GenericDTO#getOracleType()
	 */
	@Override
	public AreaType getOracleType() 
	{
		if ( getDbType() == null )
		{
			setType( new AreaType() );
		}
		
		if ( getDbType() instanceof AreaType )
		{
			return (AreaType)getDbType();
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException, MAATSystemException
	{	
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try
		{
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId( 			convertorHelper.toLong( 	getDTO().getAreaId() ) );
			getOracleType().setCode(		convertorHelper.toString(	getDTO().getCode() ));	
			getOracleType().setDescription(	convertorHelper.toString(	getDTO().getDescription() ));
			getOracleType().setEnabled(		convertorHelper.toBoolean( 	getDTO().isEnabled()  ) );
			getOracleType().setTimeStamp(	convertorHelper.toDate(		getDTO().getTimeStamp() ));
			
			
			if ( ( getDTO().getCaseManagementUnits() != null ) &&
				 ( getDTO().getCaseManagementUnits().size() > 0 ) )
			{
				CmuType[]						cmus 	= new CmuType[ getDTO().getCaseManagementUnits().size() ];
				Iterator<CaseManagementUnitDTO> cIt		= getDTO().getCaseManagementUnits().iterator();
				int idx = 0;
				
				CaseManagementUnitConvertor cmuConvert = new CaseManagementUnitConvertor();
				
				while ( cIt != null && cIt.hasNext() )
				{
					CaseManagementUnitDTO		cmuDTO 	= cIt.next();
					cmuConvert.setTypeFromDTO(  cmuDTO );
					cmus[idx++] = cmuConvert.getOracleType();	// get a type from the individual DTO
				}
				CMUTabType cmuTabtype = new CMUTabType( cmus );
				
				getOracleType().setCmuTab( cmuTabtype );
			}
			
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "CaseManagementUnitConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

	@Override
	public void setDTOFromType(Object oracleType) throws MAATApplicationException, MAATSystemException
	{
		// save it
		this.setType( oracleType );
		this.setDto( new AreaDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setAreaId(			convertorHelper.toLong( 	getOracleType().getId() ));
			getDTO().setCode(			convertorHelper.toString(	getOracleType().getCode() ));
			getDTO().setDescription(	convertorHelper.toString(	getOracleType().getDescription() ));
			getDTO().setEnabled(		convertorHelper.toBoolean(	getOracleType().getEnabled() ));
			getDTO().setTimeStamp(		convertorHelper.toDate(		getOracleType().getTimeStamp()	));
	    	/*
	    	 * convert cmus
	    	 */
	    	
			CMUTabType cmuTabtype = getOracleType().getCmuTab();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setCaseManagementUnits( new ArrayList<CaseManagementUnitDTO>() );
	    	
	    	if ( cmuTabtype != null )
	    	{
	    		CmuType[] cmus 	=  cmuTabtype.getArray();
	    		
				CaseManagementUnitConvertor cmuConvert = new CaseManagementUnitConvertor();
	    		
	    		for ( int i = 0; i < cmus.length ; i++ )
	    		{
	    			cmuConvert.setDTOFromType( cmus[i] );
	    			getDTO().getCaseManagementUnits().add( cmuConvert.getDTO() );
	    		}
	    	}
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "AreaConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}
	
}
