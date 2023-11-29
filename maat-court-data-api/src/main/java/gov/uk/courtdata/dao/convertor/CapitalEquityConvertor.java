package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.*;
import gov.uk.courtdata.dto.application.CapitalEquityDTO;
import gov.uk.courtdata.dto.application.CapitalOtherDTO;
import gov.uk.courtdata.dto.application.CapitalPropertyDTO;
import gov.uk.courtdata.dto.application.EquityDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class CapitalEquityConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public CapitalEquityDTO getDTO()
	{
		return (CapitalEquityDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public CapitalEquityType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new CapitalEquityType() );
		}
		
		if ( getDbType() instanceof CapitalEquityType )
		{
			return (CapitalEquityType)getDbType();
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
		if ( dto instanceof CapitalEquityDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + CapitalEquityDTO.class.getName());
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
		this.setDto( new CapitalEquityDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setAvailable(					convertorHelper.toBoolean(	getOracleType().getAvailable()));
			getDTO().setNoCapitalDeclared(			convertorHelper.toBoolean( 	getOracleType().getNoCapitalDeclared()    	));
			getDTO().setSuffientVeriToCoverCase( 	convertorHelper.toBoolean( 	getOracleType().getSufficientVeriToCoverCase() 	));
			getDTO().setVerifiedEquityToCoverCase( 	convertorHelper.toBoolean( 	getOracleType().getVerifiedEquityToCoverCase()   	));
			getDTO().setSuffientDeclToCoverCase( 	convertorHelper.toBoolean( 	getOracleType().getSufficientDeclToCoverCase() 	));
			getDTO().setDeclaredEquityToCoverCase( 	convertorHelper.toBoolean( 	getOracleType().getDeclaredEquityToCoverCase()   	));
			getDTO().setTotalCapital(				convertorHelper.toSysGenCurrency(	getOracleType().getTotalCapital() 			));
			getDTO().setTotalEquity(				convertorHelper.toCurrency(	getOracleType().getTotalEquity() 			));
			getDTO().setTotalCapitalAndEquity(		convertorHelper.toCurrency(	getOracleType().getTotalCapitalAndEquity()	));
			
			
			/*
			 * Equity objects
			 */
			EquityTabtype equityTabType = getOracleType().getEquityTab();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setEquityObjects( new ArrayList<EquityDTO>() );	    		
	    	
	    	if ( equityTabType != null )
	    	{
	    		EquityType[] 		equityTypes = equityTabType.getArray();
				EquityConvertor eConvertor	= new EquityConvertor();
	    		
	    		for ( int i = 0; i < equityTypes.length ; i++ )
	    		{
	    			eConvertor.setDTOFromType( equityTypes[i]  );
	    			getDTO().getEquityObjects().add( eConvertor.getDTO() );
	    		}
	    	}
	    	
	    	/*
			 * Motor Vehicle Ownership
			 */
			MotorVehicleOwnerConvertor vehicleOwnerConvertor = 	new MotorVehicleOwnerConvertor();
			if(getOracleType().getMotorVehicleOwnerObj() != null){
				
				vehicleOwnerConvertor.setDTOFromType(getOracleType().getMotorVehicleOwnerObj());
			}
			getDTO().setMotorVehicleOwnerDTO(vehicleOwnerConvertor.getDTO()); 
			
			CapitalEvidenceSummaryConvertor cesConvertor	= new CapitalEvidenceSummaryConvertor();
			if ( getOracleType().getCapitalEvidenceSummaryObj() != null )
			{				
				cesConvertor.setDTOFromType(getOracleType().getCapitalEvidenceSummaryObj());				
			}
			getDTO().setCapitalEvidenceSummary( cesConvertor.getDTO() );
			
			/*
			 * Capital properties
			 */
			CapitalPropertiesTabType capitalPropertiesTabType = getOracleType().getCapitalPropertiesTab();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setCapitalProperties( new ArrayList<CapitalPropertyDTO>() );	    		
	    	
	    	if ( capitalPropertiesTabType != null )
	    	{
	    		CapitalPropertyType[] 		capitalProperties 	=  capitalPropertiesTabType.getArray();
	    		CapitalPropertyConvertor 	cpConvertor			= new CapitalPropertyConvertor();
	    		
	    		for ( int i = 0; i < capitalProperties.length ; i++ )
	    		{
	    			cpConvertor.setDTOFromType( capitalProperties[i]  );
	    			getDTO().getCapitalProperties().add( cpConvertor.getDTO() );
	    		}
	    	}
	    	
			/*
			 * Capital other
			 */
			CapitalOtherTabType capitalOtherTabType = getOracleType().getCapitalOtherTab();
	    	/*
	    	 * set an empty collection
	    	 */
    		getDTO().setCapitalOther( new ArrayList<CapitalOtherDTO>() );	    		
	    	
	    	if ( capitalOtherTabType != null )
	    	{
	    		CapitalOtherType[] 			capitalOthers	 	=  capitalOtherTabType.getArray();	    		
	    		CapitalOtherConvertor 		coConvertor			= new CapitalOtherConvertor();
	    		
	    		for ( int i = 0; i < capitalOthers.length ; i++ )
	    		{
	    			coConvertor.setDTOFromType( capitalOthers[i]  );
	    			getDTO().getCapitalOther().add( coConvertor.getDTO() );
	    		}
			
	    	}			
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
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
			setDTO( dto );
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setAvailable(					convertorHelper.toBoolean(	getDTO().getAvailable()				));
   			getOracleType().setNoCapitalDeclared(			convertorHelper.toBoolean( 	getDTO().getNoCapitalDeclared()    	));
			getOracleType().setSufficientVeriToCoverCase( 	convertorHelper.toBoolean( 	getDTO().getSuffientVeriToCoverCase()  	));
			getOracleType().setVerifiedEquityToCoverCase( 	convertorHelper.toBoolean( 	getDTO().getVerifiedEquityToCoverCase()  	));
			getOracleType().setSufficientDeclToCoverCase( 	convertorHelper.toBoolean( 	getDTO().getSuffientDeclToCoverCase()  	));
			getOracleType().setDeclaredEquityToCoverCase( 	convertorHelper.toBoolean( 	getDTO().getDeclaredEquityToCoverCase()  	));
			getOracleType().setTotalCapital(				convertorHelper.toCurrency(	getDTO().getTotalCapital() 			));
			getOracleType().setTotalEquity(			    	convertorHelper.toCurrency(	getDTO().getTotalEquity() 			));
			getOracleType().setTotalCapitalAndEquity(		convertorHelper.toCurrency(	getDTO().getTotalCapitalAndEquity()	));				
			
			/*
			 * Equity
			 */
	    	if ( getDTO().getEquityObjects() != null )
	    	{
	    		EquityType[] 		equityObjects 	=  new EquityType[ getDTO().getEquityObjects().size() ];	    		
	    		Iterator<EquityDTO>	it				= getDTO().getEquityObjects().iterator();
	    		int					idx 			= 0;
    			EquityConvertor 	convertor		= new EquityConvertor();
    			
	    		while ( ( it != null ) && ( it.hasNext() ) )
	    		{
	    			EquityDTO 		equityDto 	= it.next();
	    			
	    			convertor.setTypeFromDTO( equityDto );
	    			equityObjects[idx++] = convertor.getOracleType();
	    		}
	    		EquityTabtype EquityTabtype = new EquityTabtype(equityObjects);
	    		getOracleType().setEquityTab(EquityTabtype);
	    	}
	    	
			/*
			 * Motor Vehicle Ownership
			 */
	    	MotorVehicleOwnerConvertor motorVehicleOwnerConvertor = new MotorVehicleOwnerConvertor();
	    	if(getDTO().getMotorVehicleOwnerDTO() != null){
	    		motorVehicleOwnerConvertor.setTypeFromDTO(getDTO().getMotorVehicleOwnerDTO());
	    	}
	    	getOracleType().setMotorVehicleOwnerObj(motorVehicleOwnerConvertor.getOracleType());
	    	
			/*
			 * Capital Evidence Summary
			 */
			CapitalEvidenceSummaryConvertor 	ceConvertor	= new CapitalEvidenceSummaryConvertor();
			if ( getDTO().getCapitalEvidenceSummary() != null )
			{
				ceConvertor.setTypeFromDTO( getDTO().getCapitalEvidenceSummary() );
			}
			getOracleType().setCapitalEvidenceSummaryObj( ceConvertor.getOracleType() );
			
			/*
			 * Capital properties
			 */
	    	if ( getDTO().getCapitalProperties() != null )
	    	{
	    		CapitalPropertyType[] 		capitalProperties 	=  new CapitalPropertyType[ getDTO().getCapitalProperties().size() ];	    		
	    		
	    		
	    		Iterator<CapitalPropertyDTO>	it				= getDTO().getCapitalProperties().iterator();
	    		int	idx = 0;
	    		while ( ( it != null ) && ( it.hasNext() ) )
	    		{
	    			CapitalPropertyConvertor 	cpConvertor			= new CapitalPropertyConvertor();
	    			CapitalPropertyDTO cpDto = it.next();
	    			cpConvertor.setTypeFromDTO( cpDto );
	    			capitalProperties[idx++] = cpConvertor.getOracleType();
	    		}
	    		CapitalPropertiesTabType capitalPropertyTabType = new CapitalPropertiesTabType(capitalProperties);
	    		getOracleType().setCapitalPropertiesTab(capitalPropertyTabType);
	    	}
	    	
			/*
			 * Capital other
			 */
	    	if ( getDTO().getCapitalOther() != null )
	    	{
	    		CapitalOtherType[] 		capitalOtherTypes 	=  new CapitalOtherType[ getDTO().getCapitalOther().size() ];	    		
	    		
	    		
	    		Iterator<CapitalOtherDTO>	it				= getDTO().getCapitalOther().iterator();
	    		int	idx = 0;
	    		while ( ( it != null ) && ( it.hasNext() ) )
	    		{
	    			CapitalOtherConvertor 	coConvertor			= new CapitalOtherConvertor();
	    			CapitalOtherDTO coDto	 = it.next();
	    			coConvertor.setTypeFromDTO( coDto );
	    			capitalOtherTypes[idx++] = coConvertor.getOracleType();
	    		}
	    		CapitalOtherTabType capitalOtherTabType = new CapitalOtherTabType(capitalOtherTypes);
	    		getOracleType().setCapitalOtherTab(capitalOtherTabType);
	    	}
	    	
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}


}
