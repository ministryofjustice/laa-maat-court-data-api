/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.CrownCourtSummaryType;
import gov.uk.courtdata.dao.oracle.OutcomeTabtype;
import gov.uk.courtdata.dao.oracle.OutcomeType;
import gov.uk.courtdata.dto.application.CrownCourtSummaryDTO;
import gov.uk.courtdata.dto.application.OutcomeDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class CrownCourtSummaryConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public CrownCourtSummaryDTO getDTO()
	{
		return (CrownCourtSummaryDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public CrownCourtSummaryType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new CrownCourtSummaryType() );
		}
		
		if ( getDbType() instanceof CrownCourtSummaryType )
		{
			return (CrownCourtSummaryType)getDbType();
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
		if ( dto instanceof CrownCourtSummaryDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + CrownCourtSummaryDTO.class.getName());
	}

	/**
	 * Updates the local instance of the DTO by converting the dao in the
	 * oracle type object passed as a parameter
	 * @see Convertor#setDTOFromType(Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException{
		
		// save it
		this.setType( oracleType );
		this.setDto( new CrownCourtSummaryDTO() );	// create the new DTO
		
		try{
			ConvertorHelper convertorHelper 	= new ConvertorHelper();	
			getDTO().setCcRepId(				convertorHelper.toLong( 	getOracleType().getCcRepId()  ));
			getDTO().setCcRepType(				convertorHelper.toSysGenString( 	getOracleType().getCcRepType()));
			getDTO().setCcRepOrderDate( 		convertorHelper.toDate( 	getOracleType().getCcReporderDate()  	));
			getDTO().setSentenceOrderDate(      convertorHelper.toDate( 	getOracleType().getSentenceOrderDate() ));
			getDTO().setCcWithDrawalDate(		convertorHelper.toDate(		getOracleType().getCcWithdrawalDate()	));	
			getDTO().setInPrisoned(				convertorHelper.toBoolean(	getOracleType().getCcImprisoned()));
			getDTO().setBenchWarrantyIssued(	convertorHelper.toBoolean(	getOracleType().getBenchWarrantIssued()));
			getDTO().setRepOrderDecision(		convertorHelper.toSysGenString(getOracleType().getCcReporderDecision()));

			
			if ( getOracleType().getEvidenceFeeObject() != null ){
				EvidenceFeeConvertor convertor = new EvidenceFeeConvertor();
				convertor.setDTOFromType( getOracleType().getEvidenceFeeObject()  );
				getDTO().setEvidenceProvisionFee(  convertor.getDTO() );
			}
			
			
			
			/*
			 * convert the multiple outcome collection
			 */
			getDTO().setOutcomeDTOs( new ArrayList<OutcomeDTO>() );
			
			if ( getOracleType().getCcOutcomeTab() != null ){
			
				OutcomeTabtype outcomeTabtype = getOracleType().getCcOutcomeTab();
				OutcomeType[]	outcomeTypeArray	= outcomeTabtype.getArray();
				OutcomeConvertor outcomeConvertor = new  OutcomeConvertor();
				
				for ( int i = 0; i < outcomeTypeArray.length; i++ ){
					
					outcomeConvertor.setDTOFromType( outcomeTypeArray[i] );
					getDTO().getOutcomeDTOs().add(outcomeConvertor.getDTO());
				}				
			}
			
			
		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "CrownCourtSummaryConvertor - the embedded dto is null");
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
			ConvertorHelper convertorHelper 			= new ConvertorHelper();
			getOracleType().setCcRepId(				    convertorHelper.toLong( 	getDTO().getCcRepId()  ));
			getOracleType().setCcRepType(				convertorHelper.toSysGenString( 	getDTO().getCcRepType()));
			getOracleType().setCcReporderDate( 		    convertorHelper.toDate( 	getDTO().getCcRepOrderDate()  	));
			getOracleType().setSentenceOrderDate(		convertorHelper.toDate( 	getDTO().getSentenceOrderDate() ));
			getOracleType().setCcWithdrawalDate(		convertorHelper.toDate(		getDTO().getCcWithDrawalDate()	));	
			getOracleType().setCcImprisoned(convertorHelper.toBoolean(getDTO().getInPrisoned()));
			getOracleType().setBenchWarrantIssued(convertorHelper.toBoolean(getDTO().getBenchWarrantyIssued()));
			getOracleType().setCcReporderDecision(convertorHelper.toSysGenString(getDTO().getRepOrderDecision()));
			
		    if ( getDTO().getEvidenceProvisionFee() != null ){
				EvidenceFeeConvertor convertor = new EvidenceFeeConvertor();
				convertor.setTypeFromDTO( getDTO().getEvidenceProvisionFee() );
				getOracleType().setEvidenceFeeObject( convertor.getOracleType() );
			}
			
			
			
			if ( getDTO().getOutcomeDTOs() != null ){
				
				OutcomeType[] outcomeTypes	 = new OutcomeType[ getDTO().getOutcomeDTOs().size() ];
			
				Iterator<OutcomeDTO>	itr		= getDTO().getOutcomeDTOs().iterator();
				
				int idx = 0;
				
				OutcomeConvertor outcomeConvertor = new OutcomeConvertor();
				while ( itr.hasNext() ){
					
					outcomeConvertor.setTypeFromDTO( itr.next() );
					outcomeTypes[idx++] = outcomeConvertor.getOracleType();
				}
				
				OutcomeTabtype outcomeTabtype = new OutcomeTabtype(outcomeTypes);
				getOracleType().setCcOutcomeTab(outcomeTabtype);
			}else{
				
				getOracleType().setCcOutcomeTab( new OutcomeTabtype() );
			}
			
	
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "CrownCourtSummaryConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
