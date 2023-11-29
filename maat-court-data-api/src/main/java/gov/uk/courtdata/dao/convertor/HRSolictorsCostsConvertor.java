package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.HrSolicitorCostType;
import gov.uk.courtdata.dto.application.HRSolicitorsCostsDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;

public class HRSolictorsCostsConvertor extends Convertor {

	@Override
	public HRSolicitorsCostsDTO getDTO()
	{
		return (HRSolicitorsCostsDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public HrSolicitorCostType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new HrSolicitorCostType() );
		}
		
		if ( getDbType() instanceof HrSolicitorCostType )
		{
			return (HrSolicitorCostType)getDbType();
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
		if ( dto instanceof HRSolicitorsCostsDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " 
												+ dto.getClass().getName() 
												+ " instead of " 
												+ HRSolicitorsCostsDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new HRSolicitorsCostsDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setSolicitorHours(				convertorHelper.toDouble(getOracleType().getSolicitorHours()));
			getDTO().setSolicitorRate(				convertorHelper.toCurrency(getOracleType().getSolicitorRate()));
			getDTO().setSolicitorVat(				convertorHelper.toCurrency(getOracleType().getSolicitorVat()));
			getDTO().setSolicitorDisb(				convertorHelper.toCurrency(getOracleType().getSolicitorDisb()));
			getDTO().setSolicitorEstimatedTotalCost(	convertorHelper.toCurrency(getOracleType().getSolicitorEstTotalCost()));
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
			getOracleType().setSolicitorHours(		convertorHelper.toDouble(getDTO().getSolicitorHours(		)));
			getOracleType().setSolicitorRate(		convertorHelper.toCurrency(getDTO().getSolicitorRate(		)));
			getOracleType().setSolicitorVat(		convertorHelper.toCurrency(getDTO().getSolicitorVat(		)));
			getOracleType().setSolicitorDisb(		convertorHelper.toCurrency(getDTO().getSolicitorDisb(		)));
			getOracleType().setSolicitorEstTotalCost(	convertorHelper.toCurrency(getDTO().getSolicitorEstimatedTotalCost()));


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

}
