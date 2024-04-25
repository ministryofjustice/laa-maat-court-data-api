package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.*;
import gov.uk.courtdata.dto.application.ContributionSummaryDTO;
import gov.uk.courtdata.dto.application.CorrespondenceDTO;
import gov.uk.courtdata.dto.application.CrownCourtOverviewDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class CrownCourtOverviewConvertor extends Convertor
{

	/* (non-Javadoc)
	 * @see uk.gov.lsc.maat.bus.dao.dao.convertor.Convertor#getDTO()
	 */
	@Override
	public CrownCourtOverviewDTO getDTO()
	{
		return (CrownCourtOverviewDTO)getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public CrownCourtOverviewType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new CrownCourtOverviewType() );
		}
		
		if ( getDbType() instanceof CrownCourtOverviewType )
		{
			return (CrownCourtOverviewType)getDbType();
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
		if ( dto instanceof CrownCourtOverviewDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + CrownCourtOverviewDTO.class.getName());
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
		this.setDto( new CrownCourtOverviewDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setAvailable(	convertorHelper.toBoolean(	getOracleType().getAvailable()));

			convertCrownCourtSummaryFromType();
			convertContributionsFromType();
			convertApplicantPaymentDetailsFromType();
			convertAppealFromType();
			convertCorrespondenceFromType();
			convertContributionSummaryFromType();
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "CrownCourtOverviewConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}

	private void convertCrownCourtSummaryFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		CrownCourtSummaryType	crownCourtSummaryType		= getOracleType().getCrownCourtSummaryObject();

		if ( crownCourtSummaryType != null )
		{
			CrownCourtSummaryConvertor convertor	= new CrownCourtSummaryConvertor();
			convertor.setDTOFromType(crownCourtSummaryType);
			getDTO().setCrownCourtSummaryDTO( convertor.getDTO() );
		}
	}

	private void convertContributionsFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		ContributionsType	contributionsType		= getOracleType().getContributionsObject();

		if ( contributionsType != null )
		{
			ContributionsConvertor convertor	= new ContributionsConvertor();
			convertor.setDTOFromType(contributionsType);
			getDTO().setContribution( convertor.getDTO() );
		}
	}

	private void convertApplicantPaymentDetailsFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		ApplPaymentDetailsType applicantPaymentType = getOracleType().getApplPaymentDetailsObject();
		if ( applicantPaymentType != null )
		{
			ApplicantPaymentDetailsConvertor apdConvert	= new ApplicantPaymentDetailsConvertor();
			apdConvert.setDTOFromType(applicantPaymentType);
			getDTO().setApplicantPaymentDetailsDTO( apdConvert.getDTO() );
		}
	}

	private void convertAppealFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if( getOracleType().getAppealObject() != null){

			AppealConvertor appealConvertor = new AppealConvertor();
			appealConvertor.setDTOFromType(getOracleType().getAppealObject());
			getDTO().setAppealDTO(appealConvertor.getDTO());
		}
	}

	private void convertCorrespondenceFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		getDTO().setCorrespondence( new ArrayList<CorrespondenceDTO>() );
		if ( getOracleType().getCorrespondenceTab() != null )
		{
			CorrespondenceTabType 		corrTabType = getOracleType().getCorrespondenceTab();
			CorrespondenceType[] 		corrArray	= corrTabType.getArray();

			CorrespondenceConvertor cConvertor = new CorrespondenceConvertor();
			for ( int i = 0; i < corrArray.length; i++  )
			{
				cConvertor.setDTOFromType( corrArray[i] );
				getDTO().getCorrespondence().add( cConvertor.getDTO() );
			}
		}
	}

	private void convertContributionSummaryFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		getDTO().setContributionSummary( new ArrayList<ContributionSummaryDTO>() );
		if ( getOracleType().getContributionSummaryTab() != null )
		{
			ContributionSummaryTabType 	contribTabType	= getOracleType().getContributionSummaryTab();
			ContributionSummaryType[] 	contribArray	= contribTabType.getArray();

			ContributionSummaryConvertor cConvertor = new ContributionSummaryConvertor();
			for ( int i = 0; i < contribArray.length; i++  )
			{
				cConvertor.setDTOFromType( contribArray[i] );
				getDTO().getContributionSummary().add( cConvertor.getDTO() );
			}
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
			this.setDTO(dto);
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getOracleType().setAvailable(convertorHelper.toBoolean(getDTO().getAvailable()));

			convertCrownCourtSummaryFromDTO();
			convertContributionsFromDTO();
			convertApplicantPaymentDetailsFromDTO();
			convertAppealFromDTO();
			convertCorrespondenceFromDTO();
			convertContributionSummaryFromDTO();
		}		
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "CrownCourtOverviewConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

	private void convertCrownCourtSummaryFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		CrownCourtSummaryConvertor 	ccConvertor	= new CrownCourtSummaryConvertor();
		if ( getDTO().getCrownCourtSummaryDTO() != null )
		{
			ccConvertor.setTypeFromDTO( getDTO().getCrownCourtSummaryDTO() );
		}
		getOracleType().setCrownCourtSummaryObject( ccConvertor.getOracleType() );
	}

	private void convertContributionsFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		ContributionsConvertor 	conConvertor	= new ContributionsConvertor();
		if ( getDTO().getContribution() != null )
		{
			conConvertor.setTypeFromDTO( getDTO().getContribution() );
		}
		getOracleType().setContributionsObject( conConvertor.getOracleType() );
	}

	private void convertApplicantPaymentDetailsFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		ApplicantPaymentDetailsConvertor apdConvert	= new ApplicantPaymentDetailsConvertor();
		if ( getDTO().getApplicantPaymentDetailsDTO() != null )
		{
			apdConvert.setTypeFromDTO( getDTO().getApplicantPaymentDetailsDTO() );
		}
		getOracleType().setApplPaymentDetailsObject( apdConvert.getOracleType() );
	}

	private void convertAppealFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		if (getDTO().getAppealDTO() != null){
			AppealConvertor appealConvertor = new AppealConvertor();
			appealConvertor.setTypeFromDTO(getDTO().getAppealDTO());
			getOracleType().setAppealObject(appealConvertor.getOracleType());
		}
	}

	private void convertCorrespondenceFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( ( getDTO().getCorrespondence() != null ) && ( getDTO().getCorrespondence().size() > 0 ) )
		{
			CorrespondenceType[]			ct	= new CorrespondenceType[ getDTO().getCorrespondence().size() ];
			Iterator<CorrespondenceDTO> 	cIt	= getDTO().getCorrespondence().iterator();
			int idx = 0;

			CorrespondenceConvertor cConvertor = new CorrespondenceConvertor();

			while ( cIt != null && cIt.hasNext() )
			{
				CorrespondenceDTO			coDTO 	= cIt.next();
				cConvertor.setTypeFromDTO(  coDTO );
				ct[idx++] = cConvertor.getOracleType();	// get a type from the individual DTO
			}
			CorrespondenceTabType cott	= new CorrespondenceTabType( ct );
			getOracleType().setCorrespondenceTab(cott);
		}
	}

	private void convertContributionSummaryFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( ( getDTO().getContributionSummary() != null ) && ( getDTO().getContributionSummary().size() > 0 ) )
		{
			ContributionSummaryType[]			ct	= new ContributionSummaryType[ getDTO().getContributionSummary().size() ];
			Iterator<ContributionSummaryDTO> 	cIt	= getDTO().getContributionSummary().iterator();
			int idx = 0;

			ContributionSummaryConvertor cConvertor = new ContributionSummaryConvertor();

			while ( cIt != null && cIt.hasNext() )
			{
				ContributionSummaryDTO	coDTO 	= cIt.next();
				cConvertor.setTypeFromDTO(  coDTO );
				ct[idx++] = cConvertor.getOracleType();	// get a type from the individual DTO
			}
			ContributionSummaryTabType contribSummary	= new ContributionSummaryTabType( ct );
			getOracleType().setContributionSummaryTab(contribSummary);
		}
	}
}
