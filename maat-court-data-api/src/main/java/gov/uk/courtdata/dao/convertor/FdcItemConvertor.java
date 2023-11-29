package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.FdcItemsType;
import gov.uk.courtdata.dto.application.FdcAdjustmentReasonDTO;
import gov.uk.courtdata.dto.application.FdcItemDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author obod-l
 *
 */
public class FdcItemConvertor extends Convertor {

	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public FdcItemDTO getDTO() {
		
		return (FdcItemDTO)this.getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public FdcItemsType getOracleType() throws MAATApplicationException,
			MAATSystemException {
		
		if ( getDbType() == null )		{
			setType( new FdcItemsType() );
		}
		
		if ( getDbType() instanceof FdcItemsType )	{
			
			return (FdcItemsType)getDbType();
			
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTO(java.lang.Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		
		if ( dto instanceof FdcItemDTO  ){
			
			this.setDto(dto);
			
		}else{
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + FdcItemDTO.class.getName());
			
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
		this.setDto( new FdcItemDTO() );	// create the new DTO

		try	{
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setId( convertorHelper.toLong(getOracleType().getId()));
			
			if(getOracleType().getAdjustmentReason() != null){
				
				FdcAdjustmentReasonDTO adjustmentReason =  new FdcAdjustmentReasonDTO();
				
				adjustmentReason.setCode(convertorHelper.toString(getOracleType().getAdjustmentReason()));
			
				getDTO().setAdjustmentReason( adjustmentReason);
			}
			
			getDTO().setPaidAsClaimed(convertorHelper.toBoolean(getOracleType().getPaidAsClaimed()));
			getDTO().setLatest(convertorHelper.toString(getOracleType().getLatest()));
			getDTO().setCaseId(convertorHelper.toString(getOracleType().getCaseNumber()));
			getDTO().setItemType(convertorHelper.toString(getOracleType().getItemType()));
			getDTO().setCourtCode(convertorHelper.toString(getOracleType().getCourtCode()));
			getDTO().setSupplierCode(convertorHelper.toString(getOracleType().getSupplierCode()));
			getDTO().setCost( convertorHelper.toDouble(getOracleType().getCost()));
			getDTO().setCostDate(convertorHelper.toDate(getOracleType().getDateCreated()));
			getDTO().setVat(convertorHelper.toDouble(getOracleType().getVat()));
			
		}catch (NullPointerException nex){
			throw new MAATApplicationException( "AreaConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
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
			MAATSystemException {
		
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try	{
			
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			if(getDTO().getAdjustmentReason() != null){
				
				getOracleType().setAdjustmentReason(convertorHelper.toString( 	getDTO().getAdjustmentReason().getCode()));
				
			}
			
			getOracleType().setPaidAsClaimed(	convertorHelper.toBoolean(getDTO().getPaidAsClaimed()));
			getOracleType().setLatest(	convertorHelper.toString(getDTO().getLatest()));
			getOracleType().setCaseNumber( convertorHelper.toString( getDTO().getCaseId()));
			getOracleType().setItemType( convertorHelper.toString( getDTO().getItemType()));
			getOracleType().setCourtCode( convertorHelper.toString( getDTO().getCourtCode()));
			getOracleType().setSupplierCode( convertorHelper.toString( getDTO().getSupplierCode()));
			getOracleType().setCost(convertorHelper.toDouble(getDTO().getCost()));
			getOracleType().setDateCreated( convertorHelper.toDate(getDTO().getCostDate()));
			getOracleType().setId( convertorHelper.toLong(getDTO().getId()));
			getOracleType().setVat( convertorHelper.toDouble(getDTO().getVat()));
		
			
			
		}catch (NullPointerException nex){
			throw new MAATApplicationException( "DrcFileConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}

		

	}

}
