/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.ExtraEvidenceType;
import gov.uk.courtdata.dto.application.ExtraEvidenceDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

/**
 * @author SWAN-D
 *
 */
public class ExtraEvidenceConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public ExtraEvidenceDTO getDTO()
	{
		return (ExtraEvidenceDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public ExtraEvidenceType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new ExtraEvidenceType() );
		}
		
		if ( getDbType() instanceof ExtraEvidenceType )
		{
			return (ExtraEvidenceType)getDbType();
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
		if ( dto instanceof ExtraEvidenceDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ExtraEvidenceDTO.class.getName());
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
		this.setDto( new ExtraEvidenceDTO() );	// create the new DTO

		try
		{
			ConvertorHelper 				convertorHelper = new ConvertorHelper();
			getDTO().setId(					convertorHelper.toLong(		getOracleType().getId()				));
			getDTO().setOtherDescription(	convertorHelper.toString(	NOT_APPLICABLE 						));
			getDTO().setDateReceived(		convertorHelper.toDate(		getOracleType().getDateReceived()	));
			getDTO().setOtherText(          convertorHelper.toString(   getOracleType().getOtherText() 		));
			getDTO().setMandatory(          convertorHelper.toBoolean(  getOracleType().getMandatory() 		));
			getDTO().setAdhoc(	          	convertorHelper.toString(  getOracleType().getAdhoc() 			));
			
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
			
			throw new MAATApplicationException( "ExtraEvidenceConvertor - the embedded dto is null");
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
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setId(				convertorHelper.toLong(		getDTO().getId()          ));
			getOracleType().setDateReceived(	convertorHelper.toDate(		getDTO().getDateReceived()));
			getOracleType().setOtherText(       convertorHelper.toString(   getDTO().getOtherText()   ));
			getOracleType().setMandatory(       convertorHelper.toBoolean(  getDTO().getMandatory()   )); 
			getOracleType().setAdhoc(       	convertorHelper.toString(  getDTO().getAdhoc()  	  )); 
			
			EvidenceTypeConvertor evConvertor = new EvidenceTypeConvertor();
			
			if(getDTO().getEvidenceTypeDTO() != null){
				evConvertor.setTypeFromDTO(getDTO().getEvidenceTypeDTO());
			}
			getOracleType().setEvidenceTypeObject( evConvertor.getOracleType() );
		}		
		catch (NullPointerException nex)
		{
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ExtraEvidenceConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}


}
