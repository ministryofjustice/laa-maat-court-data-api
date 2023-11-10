package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.ThirdPartyType;
import gov.uk.courtdata.dto.application.ThirdPartyOwnerDTO;
import gov.uk.courtdata.dto.application.ThirdPartyTypeDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class ThirdPartyOwnerConvertor extends Convertor{

	
	
	
	@Override
	public ThirdPartyOwnerDTO getDTO() {
		
		return (ThirdPartyOwnerDTO)getDto();
	}

	@Override
	public ThirdPartyType getOracleType() throws MAATApplicationException,
			MAATSystemException {

		if ( getDbType() == null )		{
			setType( new ThirdPartyType() );
		}
		
		if ( getDbType() instanceof ThirdPartyType )	{
			
			return (ThirdPartyType)getDbType();
			
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
		
		if ( dto instanceof ThirdPartyOwnerDTO  ){
			
			this.setDto(dto);
			
		}else{
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ThirdPartyOwnerDTO.class.getName());
			
		}
		
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		
		
		// save it
		this.setType( oracleType );
		this.setDto( new ThirdPartyOwnerDTO() );	// create the new DTO

		try	{
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			getDTO().setId( convertorHelper.toLong(getOracleType().getId()));
			
			if(getOracleType().getOwnerRelation() != null){
				
				ThirdPartyTypeDTO thirdPartyTypeDTO = new ThirdPartyTypeDTO();
				
				thirdPartyTypeDTO.setCode(convertorHelper.toString(getOracleType().getOwnerRelation()));
				getDTO().setThirdPartyType(thirdPartyTypeDTO);
				
			}
			
			getDTO().setRopdId(convertorHelper.toLong(getOracleType().getRopdId()));
			getDTO().setOwnerName(convertorHelper.toString(getOracleType().getOwnerName()));
			getDTO().setOwnerRelation(convertorHelper.toString(getOracleType().getOwnerRelation()));
			getDTO().setOtherRelation(convertorHelper.toString(getOracleType().getOtherDescription()));
			
			
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "AreaConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
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
			MAATSystemException {
		
		/*
		 * update the oracle type object converting all of the dto attributes to oracleType attributes
		 */
		try	{
			
			setType( null );	// force new type to be instantiated 
			setDTO( dto );	// if the dto class type is not right for this conversion an exception is thrown
			
			ConvertorHelper convertorHelper = new ConvertorHelper();
			
			if(getDTO().getOwnerRelation() != null){
				
				getOracleType().setOwnerRelation(convertorHelper.toString( 	getDTO().getThirdPartyType().getCode()));
			}
	
			getOracleType().setId( convertorHelper.toLong(		getDTO().getId() ));
			
			getOracleType().setRopdId(convertorHelper.toLong(		getDTO().getRopdId()));
			getOracleType().setOwnerName(convertorHelper.toString( 	getDTO().getOwnerName()));
			getOracleType().setOwnerRelation(convertorHelper.toString( getDTO().getOwnerRelation()));
			getOracleType().setOtherDescription(convertorHelper.toString( getDTO().getOtherRelation()));
			
				
		}catch (NullPointerException nex){
			/*
			 * This will happen if the dto object has not been set
			 */
			
			throw new MAATApplicationException( "ThirdPartyOwnerConvertor - the embedded dto is null");
			
		}catch (SQLException ex ){
			
			throw new MAATSystemException( ex );
		}

	}

}
