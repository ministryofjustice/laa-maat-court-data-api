/**
 * 
 */
package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.*;
import gov.uk.courtdata.dto.application.CapitalOtherDTO;
import gov.uk.courtdata.dto.application.EvidenceDTO;
import gov.uk.courtdata.dto.application.ExtraEvidenceDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class CapitalOtherConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.data.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public CapitalOtherDTO getDTO()
	{
		return (CapitalOtherDTO)getDto();
	}
	
	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public CapitalOtherType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new CapitalOtherType() );
		}
		
		if ( getDbType() instanceof CapitalOtherType )
		{
			return (CapitalOtherType)getDbType();
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
		if ( dto instanceof CapitalOtherDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + CapitalOtherDTO.class.getName());
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
		this.setDto( new CapitalOtherDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setId(					convertorHelper.toLong(		getOracleType().getId()));
			getDTO().setOtherDescription(	convertorHelper.toString( 	getOracleType().getOtherDescription() 	));
			getDTO().setAssetAmount( 		convertorHelper.toCurrency( 	getOracleType().getAssetAmount()  		));
			
			getDTO().setAccountOwner( 		convertorHelper.toString( 	getOracleType().getAccountOwner()  		));
			getDTO().setBankName( 			convertorHelper.toString( 	getOracleType().getBankName()  		));
			getDTO().setBranchSortCode( 	convertorHelper.toString( 	getOracleType().getBranch()  		));
			
			getDTO().setVerifiedAmount(		convertorHelper.toCurrency(	getOracleType().getVerifiedAmount() 	));
			getDTO().setDateEntered(		convertorHelper.toSysGenDate(		getOracleType().getDateEntered() 		));
			getDTO().setVerifiedDate(		convertorHelper.toSysGenDate(		getOracleType().getVerifiedDate()		));
			getDTO().setVerifiedBy(			convertorHelper.toSysGenString(	getOracleType().getVerifiedBy()			));	
			getDTO().setUndeclared(			convertorHelper.toBoolean(getOracleType().getUndeclared()));
			
			
			/*
			 * set up empty collection for he capital evidence table
			 */
			getDTO().setCapitalEvidence( new ArrayList<EvidenceDTO>() );
			
			if ( getOracleType().getCapitalEvidenceTab() != null )
			{
				CapitalEvidenceType[] 		cevTab 		= getOracleType().getCapitalEvidenceTab().getArray();
				CapitalEvidenceConvertor ceConvertor = new CapitalEvidenceConvertor();
				
				for ( int i = 0; i < cevTab.length; i++ )
				{
					ceConvertor.setDTOFromType(  cevTab[i] );
					getDTO().getCapitalEvidence().add( ceConvertor.getDTO() );
				}
			}

			/*
			 * convert the extra evidence collection
			 */
			getDTO().setExtraEvidence( new ArrayList<ExtraEvidenceDTO>() );
			if ( getOracleType().getExtraEvidenceTab() != null )
			{
				ExtraEvidenceTabtype 	eett 		= getOracleType().getExtraEvidenceTab();
				ExtraEvidenceType[]		eettArray	= eett.getArray();
				ExtraEvidenceConvertor eec			= new ExtraEvidenceConvertor();
				for ( int i = 0; i < eettArray.length; i++ )
				{
					eec.setDTOFromType( eettArray[i] );
					getDTO().getExtraEvidence().add( eec.getDTO() );
				}				
			}

			/**
			 * Handle the complex type: CapitalType
			 */
			CapitalTypeType  capitalTypeType = getOracleType().getCapitalTypeObject();
			if(capitalTypeType != null){
				CapitalTypeConvertor capitalTypeConvertor = new CapitalTypeConvertor();
				capitalTypeConvertor.setDTOFromType ( capitalTypeType);
				getDTO().setCapitalTypeDTO( capitalTypeConvertor.getDTO() );
			}

			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getOracleType().getAssetStatusObject() != null )
			{
				assConvertor.setDTOFromType( getOracleType().getAssetStatusObject() );
			}
			getDTO().setAssessmentStatus( assConvertor.getDTO() ); // if type is null this will initialise a valid empty object
		
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
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
            setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId(					convertorHelper.toLong(		getDTO().getId()));
			getOracleType().setOtherDescription(	convertorHelper.toString( 	getDTO().getOtherDescription() 	));
			getOracleType().setAssetAmount( 		convertorHelper.toCurrency( 	getDTO().getAssetAmount()  		));
			
			getOracleType().setAccountOwner( 	convertorHelper.toString( 	getDTO().getAccountOwner()  		));
			getOracleType().setBranch( 			convertorHelper.toString(getDTO().getBranchSortCode()		));			
			getOracleType().setBankName(convertorHelper.toString(getDTO().getBankName()  		));
			
			getOracleType().setVerifiedAmount(		convertorHelper.toCurrency(	getDTO().getVerifiedAmount() 	));
			getOracleType().setDateEntered(			convertorHelper.toSysGenDate(		getDTO().getDateEntered() 		));
			getOracleType().setVerifiedDate(		convertorHelper.toSysGenDate(		getDTO().getVerifiedDate()		));
			getOracleType().setVerifiedBy(			convertorHelper.toSysGenString(	getDTO().getVerifiedBy()		));
			getOracleType().setUndeclared(			convertorHelper.toBoolean(getDTO().getUndeclared()));
			
			if ( getDTO().getCapitalEvidence() != null )
			{
				CapitalEvidenceType[] 			ceTypes = new CapitalEvidenceType[ getDTO().getCapitalEvidence().size() ];
				Iterator<EvidenceDTO>	it		= getDTO().getCapitalEvidence().iterator();
				int idx = 0;
				
				CapitalEvidenceConvertor convertor = new CapitalEvidenceConvertor();
				while ( it.hasNext() )
				{
					convertor.setTypeFromDTO( it.next() );
					ceTypes[idx++] = convertor.getOracleType();
				}
				CapitalEvidenceTabType ceTabType = new CapitalEvidenceTabType( ceTypes );
				getOracleType().setCapitalEvidenceTab( ceTabType );
			}
			
			
			if ( getDTO().getExtraEvidence() != null )
			{
				ExtraEvidenceType[] 	eeTypes = new ExtraEvidenceType[ getDTO().getExtraEvidence().size() ];
				Iterator<ExtraEvidenceDTO>	it		= getDTO().getExtraEvidence().iterator();
				int idx = 0;
				
				ExtraEvidenceConvertor 	eec		= new ExtraEvidenceConvertor();
				while ( it.hasNext() )
				{
					eec.setTypeFromDTO( it.next() );
					eeTypes[idx++] = eec.getOracleType();
				}
				ExtraEvidenceTabtype eeTabType = new ExtraEvidenceTabtype( eeTypes );
				getOracleType().setExtraEvidenceTab( eeTabType );
			}			
			
			/**
			 * handle the complex type: CapitalType
			 */
			CapitalTypeConvertor capitalTypeConvertor = new CapitalTypeConvertor();
			if (getDTO().getCapitalTypeDTO() != null)
			{
					capitalTypeConvertor.setTypeFromDTO( getDTO().getCapitalTypeDTO());
								
			}
			getOracleType().setCapitalTypeObject(capitalTypeConvertor.getOracleType() );
			
			
			AssessmentStatusConvertor assConvertor	= new AssessmentStatusConvertor();
			if ( getDTO().getAssessmentStatus() != null )
			{
				assConvertor.setTypeFromDTO( getDTO().getAssessmentStatus() );
			}
			getOracleType().setAssetStatusObject( assConvertor.getOracleType() ); // if type is null this will initialise a valid empty object
			
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "PropertyConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}


}
