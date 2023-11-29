package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.FdcNotesType;
import gov.uk.courtdata.dto.application.NoteDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author obod-l
 *
 */
public class FdcNoteConvertor extends Convertor {

	
	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public NoteDTO getDTO() {
		
		return (NoteDTO)this.getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public FdcNotesType getOracleType() throws MAATApplicationException,
			MAATSystemException {
		
		if ( getDbType() == null )		{
			setType( new FdcNotesType() );
		}
		
		if ( getDbType() instanceof FdcNotesType )	{
			
			return (FdcNotesType)getDbType();
			
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#setDTO(java.lang.Object)
	 */
	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		
		if ( dto instanceof NoteDTO  ){
			
			this.setDto(dto);
			
		}else{
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + NoteDTO.class.getName());
			
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
		this.setDto( new NoteDTO() );	// create the new DTO

		try	{
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setNoteId( convertorHelper.toLong(getOracleType().getId()));
				
			getDTO().setFdcId(convertorHelper.toLong(getOracleType().getFdcId()));
			getDTO().setNote(convertorHelper.toString(getOracleType().getNote()));
			getDTO().setUserCreated(convertorHelper.toString(getOracleType().getUserCreated()));
			getDTO().setDateCreated(convertorHelper.toDate(getOracleType().getTimeStamp()));
			
		}catch (NullPointerException nex){
			throw new MAATApplicationException( "FdcNoteConvertor - the embedded dto is null");
			
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
			
			getOracleType().setId(convertorHelper.toLong(getDTO().getNoteId()));
			getOracleType().setFdcId(convertorHelper.toLong(getDTO().getFdcId()));
			getOracleType().setNote(convertorHelper.toString( getDTO().getNote()));
			getOracleType().setUserCreated(convertorHelper.toString( getDTO().getUserCreated() ));
			getOracleType().setTimeStamp(convertorHelper.toDate(getDTO().getDateCreated()));
			
		}catch (NullPointerException nex){
			throw new MAATApplicationException( "FdcNoteConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}

		

	}
	
	
}
