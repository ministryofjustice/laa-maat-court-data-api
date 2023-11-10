package gov.uk.courtdata.data.convertor;

import gov.uk.courtdata.data.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.data.oracle.FrequencyType;
import gov.uk.courtdata.dto.application.FrequenciesDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class FrequencyConvertor extends Convertor {

	@Override
	public FrequenciesDTO getDTO() {

		return (FrequenciesDTO) getDto();
	}

	@Override
	public FrequencyType getOracleType() throws MAATApplicationException,
			MAATSystemException {

		if ( getDbType() == null )
		{
			setType( new FrequencyType() );
		}
		
		if ( getDbType() instanceof FrequencyType )
		{
			return (FrequencyType)getDbType();
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

	@Override
	public void setDTO(Object dto) throws MAATApplicationException {
		if ( dto instanceof FrequenciesDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + FrequenciesDTO.class.getName());
	
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new FrequenciesDTO() );	// create the new DTO

		ConvertorHelper convertorHelper = new ConvertorHelper();
		try {
			getDTO().setCode(			convertorHelper.toString(	getOracleType().getCode()));
			getDTO().setDescription(	convertorHelper.toString(	getOracleType().getDescription()));
			getDTO().setAnnualWeighting(convertorHelper.toLong(		getOracleType().getAnnualWeighting()));
		
		} catch (SQLException e) {
			throw new MAATApplicationException(e);
		}
	}

	@Override
	public void setTypeFromDTO(Object dto) throws MAATApplicationException,
			MAATSystemException {
		
		
		try 
		{
			setType( null );	// force new type to be instantiated 
			setDTO(dto);
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setCode(            convertorHelper.toString(getDTO().getCode()));		
			getOracleType().setDescription(     convertorHelper.toString(getDTO().getDescription()));	
			getOracleType().setAnnualWeighting( convertorHelper.toLong(  getDTO().getAnnualWeighting()));

		} catch (SQLException e) {
			throw new MAATApplicationException(e);
		}
	}

}
