package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.HRDetailTabType;
import gov.uk.courtdata.dao.oracle.HrDetailType;
import gov.uk.courtdata.dao.oracle.HrSectionType;
import gov.uk.courtdata.dto.application.HRDetailDTO;
import gov.uk.courtdata.dto.application.HRSectionDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class HRSectionConvertor extends Convertor {

	@Override
	public HRSectionDTO getDTO()
	{
		return (HRSectionDTO)getDto();
	}

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getOracleType()
	 */
	@Override
	public HrSectionType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new HrSectionType() );
		}
		
		if ( getDbType() instanceof HrSectionType )
		{
			return (HrSectionType)getDbType();
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
		if ( dto instanceof HRSectionDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " 
												+ dto.getClass().getName() 
												+ " instead of " 
												+ HRSectionDTO.class.getName());
	}

	@Override
	public void setDTOFromType(Object oracleType)
			throws MAATApplicationException, MAATSystemException {
		// save it
		this.setType( oracleType );
		this.setDto( new HRSectionDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setAnnualTotal(			convertorHelper.toCurrency(getOracleType().getAnnualTotal()));
			getDTO().setApplicantAnnualTotal(	convertorHelper.toCurrency(getOracleType().getAppAnnualTotal()));
			getDTO().setPartnerAnnualTotal(		convertorHelper.toCurrency(getOracleType().getPartnerAnnualTotal()));
			
			getDTO().setDetail(new ArrayList<HRDetailDTO>());
			HRDetailTabType detailTabType = getOracleType().getHrDetailTab();
			if(detailTabType != null)
			{
				HRDetailConvertor detailConverter = new HRDetailConvertor();
				HrDetailType[] detailTypes = detailTabType.getArray();
				for(int i=0; i< detailTypes.length;i++)
				{
					detailConverter.setDTOFromType(detailTypes[i]);
					getDTO().getDetail().add(detailConverter.getDTO());
				}
			}
			
			HRDetailTypeConvertor detailTypeConvertor = new HRDetailTypeConvertor();
			if(getOracleType().getHrDetailTypeObject() != null)
			{				
				detailTypeConvertor.setDTOFromType(getOracleType().getHrDetailTypeObject());				
			}
			getDTO().setDetailType(detailTypeConvertor.getDTO());
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
			getOracleType().setAnnualTotal( convertorHelper.toCurrency(getDTO().getAnnualTotal()));
			
			//Hr Detail Type
			HRDetailTypeConvertor detailTypeConverter = new HRDetailTypeConvertor();
			if(getDTO().getDetailType() != null)
			{				
				detailTypeConverter.setTypeFromDTO(getDTO().getDetailType());				
			}
			getOracleType().setHrDetailTypeObject(detailTypeConverter.getOracleType());
			
			//HrSection 
			Collection<HRDetailDTO> hrDetail = getDTO().getDetail();
			if(hrDetail != null && hrDetail.size() > 0){
				HrDetailType[] hrDetailTypes = new HrDetailType[hrDetail.size()];
				
				Iterator<HRDetailDTO> It = hrDetail.iterator();
				int i = 0;
				while(It.hasNext()){
					HRDetailConvertor detailConverter = new HRDetailConvertor();
					detailConverter.setTypeFromDTO(It.next());
					hrDetailTypes[i++] = detailConverter.getOracleType();
				}
				getOracleType().setHrDetailTab(new HRDetailTabType(hrDetailTypes));
			}				
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
