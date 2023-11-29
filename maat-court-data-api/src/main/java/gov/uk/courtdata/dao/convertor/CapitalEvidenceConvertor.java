package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.CapitalEvidenceType;
import gov.uk.courtdata.dto.application.EvidenceDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class CapitalEvidenceConvertor extends Convertor {

	@Override
	public EvidenceDTO getDTO()
	{
		return (EvidenceDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public CapitalEvidenceType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new CapitalEvidenceType() );
		}
		
		if ( getDbType() instanceof CapitalEvidenceType )
		{
			return (CapitalEvidenceType)getDbType();
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
		if ( dto instanceof EvidenceDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " 
												+ dto.getClass().getName() 
												+ " instead of " 
												+ EvidenceDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new EvidenceDTO() );	// create the new DTO

		try
		{
			ConvertorHelper 				convertorHelper = new ConvertorHelper();
			getDTO().setId(					convertorHelper.toLong(		getOracleType().getId()));
			getDTO().setOtherDescription(	convertorHelper.toString(	getOracleType().getOtherDescription()));
			getDTO().setDateReceived(		convertorHelper.toDate(		getOracleType().getDateReceived()));
			
			
			EvidenceTypeConvertor evConvertor = new EvidenceTypeConvertor();
			
			if(getOracleType().getEvidenceTypeObject() != null){
				evConvertor.setDTOFromType(getOracleType().getEvidenceTypeObject());
			}
			getDTO().setEvidenceTypeDTO(evConvertor.getDTO());

		}
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( this.getClass().getName() + " - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try
		{
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId(				convertorHelper.toLong(		getDTO().getId()));
			getOracleType().setDateReceived(	convertorHelper.toDate(		getDTO().getDateReceived()));
			getOracleType().setOtherDescription(convertorHelper.toString(	getDTO().getOtherDescription()));
			
			EvidenceTypeConvertor evConvertor = new EvidenceTypeConvertor();
			
			if(getDTO().getEvidenceTypeDTO() != null){
				evConvertor.setTypeFromDTO(getDTO().getEvidenceTypeDTO());
			}
			getOracleType().setEvidenceTypeObject(evConvertor.getOracleType());
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( this.getClass().getName() + " - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

}
