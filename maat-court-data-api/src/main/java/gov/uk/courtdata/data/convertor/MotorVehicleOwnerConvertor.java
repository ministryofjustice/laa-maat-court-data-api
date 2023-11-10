package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.MotorVehicleOwnerType;
import gov.uk.courtdata.data.oracle.MvoRegTabtype;
import gov.uk.courtdata.data.oracle.MvoRegType;
import gov.uk.courtdata.dto.application.MotorVehicleOwnerDTO;
import gov.uk.courtdata.dto.application.VehicleRegistrationMarkDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class MotorVehicleOwnerConvertor extends Convertor {

	@Override
	public MotorVehicleOwnerDTO getDTO() {
		// TODO Auto-generated method stub
		return (MotorVehicleOwnerDTO)getDto();
	}
	
	@Override
	public MotorVehicleOwnerType getOracleType() throws MAATApplicationException,MAATSystemException {

		if ( getDbType() == null )		{
			setType( new MotorVehicleOwnerType() );
		}
		
		if ( getDbType() instanceof MotorVehicleOwnerType )	{
			
			return (MotorVehicleOwnerType)getDbType();
			
		}else{
			/*
			 * fatal error ???? write a handler in the GenericDTO
			 */
			//throw new DAOApplicationException( Constants.INVALID_DTO_TYPE_CLASS );
			return null;  // temp fix, could cause null pointer exception
		}
	}

		
	
	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		
		if ( dto instanceof MotorVehicleOwnerDTO  ){
			
			this.setDto(dto);
			
		}else{
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + MotorVehicleOwnerDTO.class.getName());
			
		}
		
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		
		
		// save it
		this.setType( oracleType );
		this.setDto( new MotorVehicleOwnerDTO() );	// create the new DTO

		try	{
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setId( convertorHelper.toLong(getOracleType().getId()));
			getDTO().setNoVehicleDeclared(convertorHelper.toBoolean(getOracleType().getNoVehicleDeclared()));
			getDTO().setAvailable(convertorHelper.toBoolean(getOracleType().getAvailable()));
		
			/*
	    	 * set an empty collection
	    	 */
			getDTO().setVehicleRegistrationMarkDTOs(new ArrayList<VehicleRegistrationMarkDTO>());
	
			if ( getOracleType().getRegistrationTab() != null ){
	
				MvoRegTabtype mvoRegTabtype = getOracleType().getRegistrationTab();
				MvoRegType[] movRegTypeArray = mvoRegTabtype.getArray();
				
				VehicleRegistrationMarkConvertor vehicleRegistrationMarkConvertor = new VehicleRegistrationMarkConvertor();
				
				for ( int i = 0; i < movRegTypeArray.length; i++ ){
					
					vehicleRegistrationMarkConvertor.setDTOFromType( movRegTypeArray[i] );
					getDTO().getVehicleRegistrationMarkDTOs().add(vehicleRegistrationMarkConvertor.getDTO());
				}	
				
					
			}
	
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "MotorVehicleOwnerConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
			throw new MAATSystemException( ex );
		}		
		
	}
	
	
	
	
	
	//Create VRM in the database

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,	MAATSystemException {
	
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try	{
			setType( null );	// force new type to be instantiated
            setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getOracleType().setId(convertorHelper.toLong(		getDTO().getId() ));
			getOracleType().setNoVehicleDeclared(convertorHelper.toBoolean(getDTO().getNoVehicleDeclared()));
			getOracleType().setAvailable(convertorHelper.toBoolean(getDTO().getAvailable()));
			
			if ( ( getDTO().getVehicleRegistrationMarkDTOs() != null ) && ( getDTO().getVehicleRegistrationMarkDTOs()).size() > 0 ) {

				MvoRegType[] movRegTypes = new MvoRegType[getDTO().getVehicleRegistrationMarkDTOs().size()];
				
				Iterator<VehicleRegistrationMarkDTO> vehicleRegistrations = getDTO().getVehicleRegistrationMarkDTOs().iterator();  

				int idx = 0;
				
				while (vehicleRegistrations.hasNext() ){
			
					VehicleRegistrationMarkDTO vehicleRegistrationMarkDTO = (VehicleRegistrationMarkDTO)vehicleRegistrations.next();
			        VehicleRegistrationMarkConvertor vehicleRegistrationMarkConvertor = new VehicleRegistrationMarkConvertor();
                    vehicleRegistrationMarkConvertor.setTypeFromDTO(vehicleRegistrationMarkDTO);
                    movRegTypes[idx++] = vehicleRegistrationMarkConvertor.getOracleType();
            
				}
				getOracleType().setRegistrationTab(new MvoRegTabtype(movRegTypes));
				
			}else{
			
				getOracleType().setRegistrationTab(new MvoRegTabtype());
			}
			
		
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */

			throw new MAATApplicationException( "MotorVehicleOwnerConvertor - the embedded dto is null");
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}
	}

}
