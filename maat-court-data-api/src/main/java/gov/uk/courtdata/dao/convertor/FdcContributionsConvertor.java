/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.*;
import gov.uk.courtdata.dto.application.FdcContributionDTO;
import gov.uk.courtdata.dto.application.FdcItemDTO;
import gov.uk.courtdata.dto.application.NoteDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author obod-l
 *
 */
public class FdcContributionsConvertor extends Convertor {

	
	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public FdcContributionDTO getDTO() {
		
		return (FdcContributionDTO)this.getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public FdcContributionsType getOracleType() throws MAATApplicationException,
			MAATSystemException {
		
		if ( getDbType() == null ){
			
			setType( new FdcContributionsType() );
		}
		
		if ( getDbType() instanceof FdcContributionsType )	{
			
			return (FdcContributionsType)getDbType();
			
		}else {
			
			return null;  // temp fix, could cause null pointer exception
		}
	}

	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		
		if ( dto instanceof FdcContributionDTO  ){
			this.setDto(dto);
		}else{
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + FdcContributionDTO.class.getName());
		}
	}

	/**
	 * Updates the local instance of the DTO by converting the dao in the
	 * oracle type object passed as a parameter
	 * @see Convertor#setDTOFromType(Object)
	 */
	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		
		// save it
		this.setType( oracleType );
		this.setDto( new FdcContributionDTO() );	// create the new DTO
		
		try{
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			//getDTO().setTimestamp(	 getOracleType().get
			getDTO().setId(		convertorHelper.toLong( getOracleType().getId() ));
			getDTO().setRepId(convertorHelper.toLong( getOracleType().getRepId() )	);
			getDTO().setDateCalculated(convertorHelper.toDate(getOracleType().getDateCalculated()));
			getDTO().setDateCreated(convertorHelper.toDate(getOracleType().getDateCreated()));
			getDTO().setDateReplaced(convertorHelper.toDate(getOracleType().getDateReplaced()));
			getDTO().setAgfsCost(convertorHelper.toDouble(getOracleType().getAgfsCost()));
			getDTO().setLgfsCost(convertorHelper.toDouble(getOracleType().getLgfsCost()));
			getDTO().setFinalCost(convertorHelper.toDouble(getOracleType().getFinalCost()));
			getDTO().setJudicialApportionment(convertorHelper.toDouble(getOracleType().getJaPercent()));
			getDTO().setVat(convertorHelper.toDouble(getOracleType().getVat()));
			getDTO().setStatus(convertorHelper.toString(getOracleType().getStatus()));
			getDTO().setManualAcceleration(convertorHelper.toBoolean(getOracleType().getAccelerate()));
			getDTO().setAgfsComplete(convertorHelper.toBoolean(getOracleType().getAgfsComplete()));
			getDTO().setLgfsComplete(convertorHelper.toBoolean(getOracleType().getLgfsComplete()));
			
			FdcItemConvertor fdcItemConvertor = new FdcItemConvertor();
			
			FdcItemsTabtype agfsCostsItemsTabtype = getOracleType().getAgfsCosts();
			
			FdcItemsTabtype lgfsCostsItemsTabtype = getOracleType().getLgfsCosts();
			/*
	    	 * set an empty collection
	    	 */
			getDTO().setAgfsCosts(new ArrayList<FdcItemDTO>() );
			
			getDTO().setLgfsCosts(new ArrayList<FdcItemDTO>() );
			
			
			if ( agfsCostsItemsTabtype != null ){
				
				FdcItemsType[] agfsCostTypes = agfsCostsItemsTabtype.getArray();
				
				for ( int i = 0; i < agfsCostTypes.length; i++ )	{
					
					fdcItemConvertor.setDTOFromType(agfsCostTypes[i] );
					getDTO().getAgfsCosts().add(fdcItemConvertor.getDTO());
					
				}
			}
			
			if ( lgfsCostsItemsTabtype != null ){
				
				FdcItemsType[] lgfsCostTypes = lgfsCostsItemsTabtype.getArray();
				
				for ( int i = 0; i < lgfsCostTypes.length; i++ )	{
					
					fdcItemConvertor.setDTOFromType(lgfsCostTypes[i] );
					getDTO().getLgfsCosts().add(fdcItemConvertor.getDTO());
					
				}
			}
			
			
			FdcNoteConvertor fdcNoteConvertor = new FdcNoteConvertor();
			
			FdcNotesTabtype fdcNotesTabtype = getOracleType().getNotesTab();
			
			/*
	    	 * set an empty collection
	    	 */
			getDTO().setNotes(new ArrayList<NoteDTO>());
			
			if ( fdcNotesTabtype != null ){
	
				FdcNotesType[] fdcNotesTypes =  fdcNotesTabtype.getArray();
				
				for ( int i = 0; i < fdcNotesTypes.length; i++ )	{
					
					fdcNoteConvertor.setDTOFromType(fdcNotesTypes[i]);
					getDTO().getNotes().add(fdcNoteConvertor.getDTO());
					
				}
			}
			
			if(getOracleType().getDrcFile() != null){
				
				DrcFileConvertor drcFileConvertor = new DrcFileConvertor();
				
				drcFileConvertor.setDTOFromType(getOracleType().getDrcFile());
				getDTO().setDrcFileRef(drcFileConvertor.getDTO());
				
			}
			
		}catch(NullPointerException nex){
			
		} catch (SQLException sqle) {
			
			throw new MAATSystemException( sqle );
		}
		
	}
	
	/**
	 * Updates the local instance of the Oracle type by converting the dao in the
	 * dto object passed as a parameter
	 * @see Convertor#setTypeFromDTO(Object)
	 */

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {
		
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try{
			
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
	        //getOracleType().setTimeStamp(          	getDTO().getTimestamp());
			getOracleType().setId(convertorHelper.toLong(getDTO().getId()));
			getOracleType().setRepId(convertorHelper.toLong(getDTO().getRepId()));
			getOracleType().setDateCalculated(	convertorHelper.toDate(	getDTO().getDateCalculated() ) );
			getOracleType().setDateCreated(		convertorHelper.toDate(		getDTO().getDateCreated() ));
			getOracleType().setDateReplaced(	convertorHelper.toDate(	getDTO().getDateReplaced() ));
			getOracleType().setAgfsCost(convertorHelper.toDouble(getDTO().getAgfsCost()));
			getOracleType().setLgfsCost(convertorHelper.toDouble(getDTO().getLgfsCost()));
			getOracleType().setFinalCost(convertorHelper.toDouble(getDTO().getFinalCost()));
			getOracleType().setJaPercent( convertorHelper.toDouble(getDTO().getJudicialApportionment()));
			getOracleType().setVat(convertorHelper.toDouble(getDTO().getVat()));
			getOracleType().setStatus(convertorHelper.toString(getDTO().getStatus()));
			getOracleType().setAccelerate(convertorHelper.toBoolean(getDTO().getManualAcceleration()));
			getOracleType().setAgfsComplete(convertorHelper.toBoolean(getDTO().getAgfsComplete()));
			getOracleType().setLgfsComplete(convertorHelper.toBoolean(getDTO().getLgfsComplete()));
			
			
			if ( ( getDTO().getAgfsCosts() != null ) && ( getDTO().getAgfsCosts().size() > 0 ) ){

				FdcItemsType[] 		fdcItemsTypes = new FdcItemsType[ getDTO().getAgfsCosts().size() ];
				Iterator<FdcItemDTO> agfsCosts		= getDTO().getAgfsCosts().iterator();
				int idx = 0;
				while ( agfsCosts.hasNext() ){
					
					FdcItemDTO fdcItemDTO	= (FdcItemDTO)agfsCosts.next();
                    FdcItemConvertor fdcItemConvertor = new FdcItemConvertor();
                    fdcItemConvertor.setTypeFromDTO(fdcItemDTO);
					
                    fdcItemsTypes[idx++]		= fdcItemConvertor.getOracleType();
				}
				
				getOracleType().setAgfsCosts(new FdcItemsTabtype(fdcItemsTypes));
				
			}else{
				
				getOracleType().setAgfsCosts(new FdcItemsTabtype());
			}
			
			
			
			
			if ( ( getDTO().getLgfsCosts() != null ) && ( getDTO().getLgfsCosts().size() > 0 ) ){

				FdcItemsType[] 		fdcItemsTypes = new FdcItemsType[ getDTO().getLgfsCosts().size() ];
				Iterator<FdcItemDTO> lgfsCosts		= getDTO().getLgfsCosts().iterator();
				int idx = 0;
				while ( lgfsCosts.hasNext() ){
					
					FdcItemDTO fdcItemDTO	= (FdcItemDTO)lgfsCosts.next();
                    
                    FdcItemConvertor fdcItemConvertor = new FdcItemConvertor();
                    fdcItemConvertor.setTypeFromDTO(fdcItemDTO);
					
                    fdcItemsTypes[idx++]		= fdcItemConvertor.getOracleType();
				}
				
				getOracleType().setLgfsCosts(new FdcItemsTabtype(fdcItemsTypes));
				
			}else{
				
				getOracleType().setLgfsCosts(new FdcItemsTabtype());
			}
				
			
			if ( getDTO().getDrcFileRef() != null )	{
				
				DrcFileConvertor drcFileConvertor = new DrcFileConvertor();
				
				drcFileConvertor.setTypeFromDTO(getDTO().getDrcFileRef());
				getOracleType().setDrcFile(drcFileConvertor.getOracleType());
				
			}
	
		}		
		catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "FdcContributionsConvertor - the embedded dto is null");
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}
		
		
	}

}
