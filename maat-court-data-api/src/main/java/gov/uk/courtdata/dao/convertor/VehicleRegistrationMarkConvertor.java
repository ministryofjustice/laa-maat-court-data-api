package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.MvoRegType;
import gov.uk.courtdata.dto.application.VehicleRegistrationMarkDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class VehicleRegistrationMarkConvertor extends Convertor {

	
	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public VehicleRegistrationMarkDTO getDTO(){
		
		return (VehicleRegistrationMarkDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public MvoRegType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new MvoRegType() );
		}
		
		if ( getDbType() instanceof MvoRegType )
		{
			return (MvoRegType)getDbType();
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
		if ( dto instanceof VehicleRegistrationMarkDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + VehicleRegistrationMarkDTO.class.getName());
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
		this.setDto( new VehicleRegistrationMarkDTO() );	// create the new DTO

		try{
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setVehicleRegistrationMark(convertorHelper.toString(	getOracleType().getRegistration() ));
			
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "VehicleRegistrationMarkConvertor - the embedded dto is null");
		}
		catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}		
	}


	/**
	 * Updates the local instance of the Oracle type by converting the dao in the
	 * dto object passed as a parameter
	 * @see Convertor#setTypeFromDTO(Object)
	 */
	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,	MAATSystemException{
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try	{
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setRegistration(convertorHelper.toString( 	getDTO().getVehicleRegistrationMark() ));
		
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "VehicleRegistrationMarkConvertor - the embedded dto is null");
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}
	}

}
