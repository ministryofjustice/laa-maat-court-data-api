package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.DrcFileType;
import gov.uk.courtdata.dto.application.DrcFileRefDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author obod-l
 *
 */
public class DrcFileConvertor extends Convertor {


	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public DrcFileRefDTO getDTO() {
		
		return (DrcFileRefDTO)getDto();
	}

	
	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public DrcFileType getOracleType() throws MAATApplicationException,
			MAATSystemException {
		
		if ( getDbType() == null )	{
			setType( new DrcFileType() );
		}
		
		if ( getDbType() instanceof DrcFileType ) {
			
			return (DrcFileType)getDbType();
		}
		else{
			
			return null;  // temp fix, could cause null pointer exception
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTO(java.lang.Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		
		if ( dto instanceof DrcFileRefDTO  ){
			
			this.setDto(dto);
			
		}else{
			
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + DrcFileRefDTO.class.getName());
			
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
		this.setDto( new DrcFileRefDTO() );	// create the new DTO

		try	{
			
			ConvertorHelper convertorHelper = new ConvertorHelper();

			getDTO().setContribFileId(	convertorHelper.toLong(	getOracleType().getContribFileId() ));
			getDTO().setDateAcknowledged(convertorHelper.toDate(getOracleType().getDateAcknowledged()));
			getDTO().setDateSent(convertorHelper.toDate(getOracleType().getDateSent()));
			getDTO().setAcknowledgeCode(convertorHelper.toString(getOracleType().getAcknowledgeCode()));
			//getDTO().setTimestamp(timestamp)
			
		}catch (NullPointerException nex){
			throw new MAATApplicationException( "DrcFileConvertor - the embedded dto is null");
			
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
			
			getOracleType().setContribFileId(	convertorHelper.toLong( 	getDTO().getContribFileId()));
			getOracleType().setAcknowledgeCode(	getDTO().getAcknowledgeCode());
			getOracleType().setDateAcknowledged(	convertorHelper.toDate(	getDTO().getDateAcknowledged()));
			getOracleType().setDateSent(	convertorHelper.toDate(	getDTO().getDateSent()));
			
			
		}catch (NullPointerException nex){
			throw new MAATApplicationException( "DrcFileConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}

	}

}
