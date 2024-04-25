/**
 * 
 */
package gov.uk.courtdata.dao.convertor;

import gov.uk.courtdata.dao.convertor.helper.ConvertorHelper;
import gov.uk.courtdata.dao.oracle.*;
import gov.uk.courtdata.dto.application.ApplicantLinkDTO;
import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.dto.application.AssessmentSummaryDTO;
import gov.uk.courtdata.dto.application.DigitisedMeansAssessmentDTO;
import gov.uk.courtdata.validator.MAATApplicationException;
import gov.uk.courtdata.validator.MAATSystemException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author SWAN-D
 *
 */
public class ApplicationConvertor extends Convertor
{

	/**
	 * Returns the instance of the DTO cast to the appropriate class
	 * @see Convertor#getDTO()
	 */
	@Override
	public ApplicationDTO getDTO()
	{
		return (ApplicationDTO)this.getDto();
	}

	/**
	 * Returns the instance of the OracleType class cast appropriately
	 * @see Convertor#getOracleType()
	 */
	@Override
	public ApplicationType getOracleType() throws MAATApplicationException,
			MAATSystemException
	{
		if ( getDbType() == null )
		{
			setType( new ApplicationType() );
		}
		
		if ( getDbType() instanceof ApplicationType )
		{
			return (ApplicationType)getDbType();
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
		if ( dto instanceof ApplicationDTO  )
			this.setDto(dto);
		else
			throw new MAATApplicationException( " Invalid DTO type for conversion got " + dto.getClass().getName() + " wanted " + ApplicationDTO.class.getName());
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
		this.setDto( new ApplicationDTO() );	// create the new DTO

		try
		{
			ConvertorHelper convertorHelper = new ConvertorHelper();
			getDTO().setTimestamp(				convertorHelper.toZonedDateTime(getOracleType().getTimeStamp()));
			
			getDTO().setRepId(					convertorHelper.toLong( 	getOracleType().getRepId() ));
			getDTO().setAreaId(					convertorHelper.toLong( 	getOracleType().getAreaId() ));
			getDTO().setUsn(                    convertorHelper.toLong( 	getOracleType().getUsn() ));
			getDTO().setCaseId(					convertorHelper.toString(	getOracleType().getCaseId()  ));
			getDTO().setArrestSummonsNo(		convertorHelper.toString(	getOracleType().getArrestSummonsNo()  ));
			getDTO().setStatusReason(			convertorHelper.toString(	getOracleType().getStatusReason()  ));
			getDTO().setIojResult(				convertorHelper.toString(	getOracleType().getIojResult()  ));
			getDTO().setIojResultNote(			convertorHelper.toString(	getOracleType().getIojResultNote()  ));
			
			getDTO().setSolicitorName(			convertorHelper.toString(	getOracleType().getSolicitorName()  ));
			getDTO().setSolicitorEmail(			convertorHelper.toString(	getOracleType().getSolicitorEmail()  ));
			getDTO().setSolicitorAdminEmail(	convertorHelper.toString(	getOracleType().getSolicitorAdminEmail()  ));
			
			getDTO().setAlertMessage(			convertorHelper.toString(	getOracleType().getAlertMessage() ));
			getDTO().setDateStatusSet(			convertorHelper.toSysGenDate(	getOracleType().getDateStatusSet() ));
			getDTO().setDateStatusDue(			convertorHelper.toDate(		getOracleType().getStatusDueDate() ));
			getDTO().setDateReceived(			convertorHelper.toDate(		getOracleType().getDateReceived() ));
			getDTO().setDateOfSignature(		convertorHelper.toDate(		getOracleType().getAppSignedDate() ));
			getDTO().setCommittalDate(			convertorHelper.toDate(		getOracleType().getCommittalDate() ));
			getDTO().setDateStamp(				convertorHelper.toDate(		getOracleType().getEfmDateStamp() ));
			getDTO().setHearingDate(			convertorHelper.toDate(		getOracleType().getHearingDate() ));
			getDTO().setMagsCourtOutcomeDate(	convertorHelper.toSysGenDate(	getOracleType().getMagsCourtOutcomeDate() ));
			getDTO().setMagsWithdrawalDate(  	convertorHelper.toDate(		getOracleType().getMagsWithdrawalDate()));
			getDTO().setCourtCustody( 			convertorHelper.toBoolean(	getOracleType().getCourtCustody()));
			getDTO().setRetrial( 				convertorHelper.toBoolean(	getOracleType().getRetrial()));
			getDTO().setWelshCorrepondence(		convertorHelper.toBoolean(	getOracleType().getWelshCorrespondence() ));
			getDTO().setDecisionDate(			convertorHelper.toDate(	getOracleType().getDecisionDate()));
			getDTO().setApplicantHasPartner(	convertorHelper.toBoolean( 	getOracleType().getPartner() ));

			convertContraryInterestFromType();
			convertRepStatusFromType();
			convertCaseDetailsFromType();
			convertOffenceFromType();
			convertMagsCourtFromType();
			convertMagsOutcomeFromType();
			convertApplicantDetailsFromType();
			convertSupplierFromType();
			convertLscTransferFromType();
			convertAreaTransferFromType();
			convertAssessmentFromType();
			convertCrownCourtOverviewFromType();
			convertCapitalEquityFromType();
			convertDecisionReasonFromType();
			convertCaseManagementUnitFromType();
			convertAssessmentSummaryFromType();
			convertApplicantLinksFromType();
			convertPassportedFromType();
			convertAllowedWorkReasonsFromType();
			convertDigitalMeansAssessmentFromType();

			getDTO().setTransactionId(convertorHelper.toString(getOracleType().getTransactionId()));

		}
		catch (NegativeArraySizeException nasx )
		{
			throw new MAATSystemException( nasx );
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "ApplicantConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}		
	}

	private void convertContraryInterestFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		ContraryInterestConvertor ciConvertor	= new ContraryInterestConvertor();
		if ( getOracleType().getContraryInterestObject() != null )
		{
			ciConvertor.setDTOFromType( getOracleType().getContraryInterestObject() );
		}
		getDTO().setPartnerContraryInterestDTO( ciConvertor.getDTO() ); // if type is null this will initialise a valid empty object
	}

	private void convertRepStatusFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getStatusObject() != null )
		{
			RepStatusConvertor convertor	= new RepStatusConvertor();
			convertor.setDTOFromType( getOracleType().getStatusObject() );
			getDTO().setStatusDTO( convertor.getDTO() );
		}
	}

	private void convertCaseDetailsFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getCaseTypeObject() != null )
		{
			CaseDetailConvertor caseDetailConvertor	= new CaseDetailConvertor();
			caseDetailConvertor.setDTOFromType( getOracleType().getCaseTypeObject() );
			getDTO().setCaseDetailsDTO( caseDetailConvertor.getDTO() );
		}
	}

	private void convertOffenceFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getOffenceTypeObject() != null )
		{
			OffenceConvertor offenceConvertor	= new OffenceConvertor();
			offenceConvertor.setDTOFromType( getOracleType().getOffenceTypeObject() );
			getDTO().setOffenceDTO( offenceConvertor.getDTO() );
		}
	}

	private void convertMagsCourtFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getMagsCourtObject() != null )
		{
			MagsCourtConvertor magsCourtConvertor	= new MagsCourtConvertor();
			magsCourtConvertor.setDTOFromType( getOracleType().getMagsCourtObject() );
			getDTO().setMagsCourtDTO( magsCourtConvertor.getDTO() );
		}
	}

	private void convertMagsOutcomeFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getMagsOutcomeObject() != null )
		{
			OutcomeConvertor outcomeConvertor	= new OutcomeConvertor();
			outcomeConvertor.setDTOFromType( getOracleType().getMagsOutcomeObject() );
			getDTO().setMagsOutcomeDTO( outcomeConvertor.getDTO() );
		}
	}

	private void convertApplicantDetailsFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getApplicantDetailsObject() != null )
		{
			ApplicantConvertor applicantConvertor	= new ApplicantConvertor();
			applicantConvertor.setDTOFromType( getOracleType().getApplicantDetailsObject() );
			getDTO().setApplicantDTO( applicantConvertor.getDTO() );
		}
	}

	private void convertSupplierFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getSupplierObject() != null )
		{
			SupplierConvertor supplierConvertor	= new SupplierConvertor();
			supplierConvertor.setDTOFromType( getOracleType().getSupplierObject() );
			getDTO().setSupplierDTO( supplierConvertor.getDTO() );
		}
	}

	private void convertLscTransferFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getLscTransfersObject() != null )
		{
			LSCTransferConvertor lscTransferConvertor	= new LSCTransferConvertor();
			lscTransferConvertor.setDTOFromType( getOracleType().getLscTransfersObject() );
			getDTO().setLscTransferDTO( lscTransferConvertor.getDTO() );
		}
	}

	private void convertAreaTransferFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getAreaTransfersObject() != null )
		{
			AreaTransferConvertor convertor	= new AreaTransferConvertor();
			convertor.setDTOFromType( getOracleType().getAreaTransfersObject() );
			getDTO().setAreaTransferDTO( convertor.getDTO() );
		}
	}

	private void convertAssessmentFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getCurrentAssessmentObject() != null )
		{
			AssessmentConvertor assessmentConvertor	= new AssessmentConvertor();
			assessmentConvertor.setDTOFromType( getOracleType().getCurrentAssessmentObject() );
			getDTO().setAssessmentDTO( assessmentConvertor.getDTO() );
		}
	}

	private void convertCrownCourtOverviewFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getCrownCourtOverviewObject() != null )
		{
			CrownCourtOverviewConvertor crownCourtOverviewConvertor	= new CrownCourtOverviewConvertor();
			crownCourtOverviewConvertor.setDTOFromType( getOracleType().getCrownCourtOverviewObject() );
			getDTO().setCrownCourtOverviewDTO( crownCourtOverviewConvertor.getDTO() );
		}
	}

	private void convertCapitalEquityFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getCapitalEquityObject() != null )
		{
			CapitalEquityConvertor capitalEquityConvertor	= new CapitalEquityConvertor();
			capitalEquityConvertor.setDTOFromType( getOracleType().getCapitalEquityObject() );
			getDTO().setCapitalEquityDTO( capitalEquityConvertor.getDTO() );
		}
	}

	private void convertDecisionReasonFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if(getOracleType().getDecisionReasonObject() != null){
			RepOrderDecisionConverter decisionConverter = new RepOrderDecisionConverter();
			decisionConverter.setDTOFromType(getOracleType().getDecisionReasonObject());
			getDTO().setRepOrderDecision(decisionConverter.getDTO());
		}
	}

	private void convertCaseManagementUnitFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		CaseManagementUnitConvertor caseManagementUnitConverter = new CaseManagementUnitConvertor();
		if(getOracleType().getCmuObject() != null){
			caseManagementUnitConverter.setDTOFromType(getOracleType().getCmuObject());
		}
		getDTO().setCaseManagementUnitDTO(caseManagementUnitConverter.getDTO());
	}

	private void convertAssessmentSummaryFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		AssessmentSummaryConvertor 	convertor					= new AssessmentSummaryConvertor();
		AssessmentSummaryTabType	assessmentSummaryTabType 	= getOracleType().getAssessmentsSummaryTab();
		/*
		 * set an empty collection
		 */
		getDTO().setAssessmentSummary( new ArrayList<AssessmentSummaryDTO>() );

		if ( assessmentSummaryTabType != null )
		{
			AssessmentSummaryType[] assessmentSummaryTypes = assessmentSummaryTabType.getArray();
			for ( int i = 0; i < assessmentSummaryTypes.length; i++ )
			{
				convertor.setDTOFromType( assessmentSummaryTypes[i] );
				getDTO().getAssessmentSummary().add( convertor.getDTO() );
			}
		}
	}

	private void convertApplicantLinksFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		ApplicantLinkConvertor 	alconvertor				= new ApplicantLinkConvertor();
		ApplicantLinksTabtype	applicantLinksTabtype 	= getOracleType().getApplicantLinksTab();
		/*
		 * set an empty collection
		 */
		getDTO().setApplicantLinks( new ArrayList<ApplicantLinkDTO>() );

		if ( applicantLinksTabtype != null )
		{
			ApplicantLinkType[] applicantLinkTypes = applicantLinksTabtype.getArray();
			for ( int i = 0; i < applicantLinkTypes.length; i++ )
			{
				alconvertor.setDTOFromType( applicantLinkTypes[i] );
				getDTO().getApplicantLinks().add( alconvertor.getDTO() );
			}
		}
	}

	private void convertPassportedFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		PassportAssessmentType passportType = getOracleType().getPassportAssessmentObject();

		if(passportType != null){
			PassportedConvertor passportConvertor = new PassportedConvertor();
			passportConvertor.setDTOFromType(passportType);
			getDTO().setPassportedDTO(passportConvertor.getDTO());
		}
	}

	private void convertAllowedWorkReasonsFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		if ( getOracleType().getAllowedWorkReasons() != null ) {
			AllowedWorkReasonConvertor allowedWorkReasonConvertor = new AllowedWorkReasonConvertor();
			allowedWorkReasonConvertor.setDTOFromType( getOracleType().getAllowedWorkReasons() );
			getDTO().setAllowedWorkReasonDTO(allowedWorkReasonConvertor.getDTO() ) ;
		}
	}

	private void convertDigitalMeansAssessmentFromType() throws MAATSystemException, MAATApplicationException, SQLException {
		// MW - 30/03/2017 - FIP Changes
		DigitalMeansAssessmentConvertor dmaConvertor	= new DigitalMeansAssessmentConvertor();
		DigiMeansAssessTabtype	dmaTabtype 	= getOracleType().getDigiMeansAssessments();

		getDTO().setMeansAssessments( new ArrayList<DigitisedMeansAssessmentDTO>() );

		if ( dmaTabtype != null )
		{
			DigiMeansAssessType[] digiMeansAssessmentTypes = dmaTabtype.getArray();
			for ( int i = 0; i < digiMeansAssessmentTypes.length; i++ )
			{
				dmaConvertor.setDTOFromType(digiMeansAssessmentTypes[i] );
				getDTO().getMeansAssessments().add(dmaConvertor.getDTO() );
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
			setDTO( dto );	
			ConvertorHelper convertorHelper 			= new ConvertorHelper();
			getOracleType().setTimeStamp(				convertorHelper.toTimestamp(getDTO().getTimestamp()));
			
			getOracleType().setRepId( 					convertorHelper.toLong( 	getDTO().getRepId() ) );
			getOracleType().setAreaId(					convertorHelper.toLong( 	getDTO().getAreaId() ));
			getOracleType().setUsn(                     convertorHelper.toLong( 	getDTO().getUsn() ));
			getOracleType().setStatusReason(			convertorHelper.toString(	getDTO().getStatusReason()  ));
			getOracleType().setCaseId(					convertorHelper.toString(	getDTO().getCaseId()  ));
			getOracleType().setArrestSummonsNo(			convertorHelper.toString(	getDTO().getArrestSummonsNo()  ));
			getOracleType().setIojResult(				convertorHelper.toString(	getDTO().getIojResult()  ));
			getOracleType().setIojResultNote(			convertorHelper.toString(	getDTO().getIojResultNote()  ));
			
			getOracleType().setSolicitorName(           convertorHelper.toString(	getDTO().getSolicitorName()  ));
			getOracleType().setSolicitorEmail(          convertorHelper.toString(	getDTO().getSolicitorEmail()  ));
			getOracleType().setSolicitorAdminEmail(     convertorHelper.toString(	getDTO().getSolicitorAdminEmail()  ));
			
			getOracleType().setAlertMessage(  			convertorHelper.toString(	getDTO().getAlertMessage() ));
			getOracleType().setDateStatusSet(			convertorHelper.toSysGenDate(	getDTO().getDateStatusSet() ));
			getOracleType().setDateReceived(			convertorHelper.toDate(		getDTO().getDateReceived() ));
			getOracleType().setAppSignedDate(			convertorHelper.toDate(		getDTO().getDateOfSignature() ));
			getOracleType().setCommittalDate(			convertorHelper.toDate(		getDTO().getCommittalDate() ));
			getOracleType().setMagsCourtOutcomeDate(	convertorHelper.toSysGenDate(	getDTO().getMagsCourtOutcomeDate() ));
			
			getOracleType().setEfmDateStamp(	convertorHelper.toDate(	getDTO().getDateStamp() ));
			getOracleType().setHearingDate(		convertorHelper.toDate(	getDTO().getHearingDate() ));
			
			getOracleType().setMagsWithdrawalDate(		convertorHelper.toDate(		getDTO().getMagsWithdrawalDate()));
			getOracleType().setCourtCustody(			convertorHelper.toBoolean(	getDTO().isCourtCustody()));
			getOracleType().setRetrial(					convertorHelper.toBoolean(	getDTO().isRetrial()));
			getOracleType().setWelshCorrespondence(		convertorHelper.toBoolean(	getDTO().isWelshCorrepondence() ));
			getOracleType().setDecisionDate(			convertorHelper.toDate(getDTO().getDecisionDate()));
			getOracleType().setStatusDueDate(			convertorHelper.toDate(		getDTO().getDateStatusDue()));
			getOracleType().setPartner( 				convertorHelper.toBoolean( 	getDTO().getApplicantHasPartner    () ) );


			/*
			 * if the dto is not null then convert the content to the corresponding type and set this on the
			 * application type. It the dto is null, then a new, initialised but empty type will be stored on the 
			 * application. This is necessary to meet the DB interface requirements
			 */

			convertContraryInterestFromDTO();
			convertRepStatusFromDTO();
			convertCaseDetailsFromDTO();
			convertOffenceFromDTO();
			convertMagsCourtFromDTO();
			convertMagsOutcomeFromDTO();
			convertApplicantDetailsFromDTO();
			convertSupplierFromDTO();
			convertLscTransferFromDTO();
			convertAreaTransferFromDTO();
			convertAssessmentFromDTO();
			convertCrownCourtOverviewFromDTO();
			convertCapitalEquityFromDTO();
			convertDecisionReasonFromDTO();
			convertCaseManagementUnitFromDTO();
			convertAssessmentSummaryFromDTO();
			convertApplicantLinksFromDTO();
			convertPassportedFromDTO();
			convertAllowedWorkReasonsFromDTO();
			convertDigitalMeansAssessmentFromDTO();

			getOracleType().setTransactionId(convertorHelper.toString(getDTO().getTransactionId()));
		}
		catch (NullPointerException nex)
		{
			throw new MAATApplicationException( "ApplicantConvertor - the embedded dto is null");
		}
		catch (SQLException ex )
		{
			throw new MAATSystemException( ex );
		}
	}

	private void convertContraryInterestFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		ContraryInterestConvertor ciConvertor	= new ContraryInterestConvertor();
		if ( getDTO().getPartnerContraryInterestDTO() != null )
		{
			ciConvertor.setTypeFromDTO( getDTO().getPartnerContraryInterestDTO() );
		}
		getOracleType().setContraryInterestObject( ciConvertor.getOracleType() ); // if type is null this will initialise a valid empty object
	}

	private void convertRepStatusFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		RepStatusConvertor rsConvertor	= new RepStatusConvertor();
		if ( getDTO().getStatusDTO() != null )
		{
			rsConvertor.setTypeFromDTO( getDTO().getStatusDTO() );
		}
		getOracleType().setStatusObject( rsConvertor.getOracleType() );
	}

	private void convertCaseDetailsFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		CaseDetailConvertor caseDetailConvertor	= new CaseDetailConvertor();
		if ( getDTO().getCaseDetailsDTO() != null )
		{
			caseDetailConvertor.setTypeFromDTO( getDTO().getCaseDetailsDTO() );
		}
		getOracleType().setCaseTypeObject( caseDetailConvertor.getOracleType() );
	}

	private void convertOffenceFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		OffenceConvertor offenceConvertor	= new OffenceConvertor();
		if ( getDTO().getOffenceDTO() != null )
		{
			offenceConvertor.setTypeFromDTO( getDTO().getOffenceDTO() );
		}
		getOracleType().setOffenceTypeObject( offenceConvertor.getOracleType() );
	}

	private void convertMagsCourtFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		MagsCourtConvertor magsCourtConvertor	= new MagsCourtConvertor();
		if ( getDTO().getMagsCourtDTO() != null )
		{
			magsCourtConvertor.setTypeFromDTO( getDTO().getMagsCourtDTO() );
		}
		getOracleType().setMagsCourtObject( magsCourtConvertor.getOracleType() );
	}

	private void convertMagsOutcomeFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		OutcomeConvertor moConvertor	= new OutcomeConvertor();
		if ( getDTO().getMagsOutcomeDTO() != null )
		{
			moConvertor.setTypeFromDTO( getDTO().getMagsOutcomeDTO() );
		}
		getOracleType().setMagsOutcomeObject( moConvertor.getOracleType() );
	}

	private void convertApplicantDetailsFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		ApplicantConvertor apConvertor	= new ApplicantConvertor();
		if ( getDTO().getApplicantDTO() != null )
		{
			apConvertor.setTypeFromDTO( getDTO().getApplicantDTO() );
		}
		getOracleType().setApplicantDetailsObject( apConvertor.getOracleType() );
	}

	private void convertSupplierFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		SupplierConvertor supConvertor	= new SupplierConvertor();
		if ( getDTO().getSupplierDTO() != null )
		{
			supConvertor.setTypeFromDTO( getDTO().getSupplierDTO() );
		}
		getOracleType().setSupplierObject( supConvertor.getOracleType() );
	}

	private void convertLscTransferFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		LSCTransferConvertor ltConvertor	= new LSCTransferConvertor();
		if ( getDTO().getLscTransferDTO() != null )
		{
			ltConvertor.setTypeFromDTO( getDTO().getLscTransferDTO() );
		}
		getOracleType().setLscTransfersObject( ltConvertor.getOracleType() );
	}

	private void convertAreaTransferFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		AreaTransferConvertor atConvertor	= new AreaTransferConvertor();
		if ( getDTO().getAreaTransferDTO() != null )
		{
			atConvertor.setTypeFromDTO( getDTO().getAreaTransferDTO() );
		}
		getOracleType().setAreaTransfersObject( atConvertor.getOracleType() );
	}

	private void convertAssessmentFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		AssessmentConvertor assConvertor	= new AssessmentConvertor();
		if ( getDTO().getAssessmentDTO() != null )
		{
			assConvertor.setTypeFromDTO( getDTO().getAssessmentDTO() );
		}
		getOracleType().setCurrentAssessmentObject( assConvertor.getOracleType() );
	}

	private void convertCrownCourtOverviewFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		CrownCourtOverviewConvertor ccoConvertor	= new CrownCourtOverviewConvertor();
		if ( getDTO().getCrownCourtOverviewDTO() != null )
		{
			ccoConvertor.setTypeFromDTO( getDTO().getCrownCourtOverviewDTO() );
		}
		getOracleType().setCrownCourtOverviewObject( ccoConvertor.getOracleType() );
	}

	private void convertCapitalEquityFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		CapitalEquityConvertor ceConvertor	= new CapitalEquityConvertor();
		if ( getDTO().getCapitalEquityDTO() != null )
		{
			ceConvertor.setTypeFromDTO( getDTO().getCapitalEquityDTO() );
		}
		getOracleType().setCapitalEquityObject( ceConvertor.getOracleType() );
	}

	private void convertDecisionReasonFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		RepOrderDecisionConverter rdConvertor	= new RepOrderDecisionConverter();
		if ( getDTO().getRepOrderDecision() != null )
		{
			rdConvertor.setTypeFromDTO( getDTO().getRepOrderDecision() );
		}
		getOracleType().setDecisionReasonObject(rdConvertor.getOracleType() );
	}

	private void convertCaseManagementUnitFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		CaseManagementUnitConvertor caseManagementUnitConvertor = new CaseManagementUnitConvertor();
		if(getDTO().getCaseManagementUnitDTO() != null) {
			caseManagementUnitConvertor.setTypeFromDTO(getDTO().getCaseManagementUnitDTO());
		}
		getOracleType().setCmuObject(caseManagementUnitConvertor.getOracleType());
	}

	private void convertAssessmentSummaryFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		AssessmentSummaryConvertor 	convertor	= new AssessmentSummaryConvertor();
		AssessmentSummaryType[] assessmentSummaryTypes = new AssessmentSummaryType[ getDTO().getAssessmentSummary().size() ];
		if ( ( getDTO().getAssessmentSummary() != null ) && ( getDTO().getAssessmentSummary().size() > 0 ) )
		{
			Iterator<AssessmentSummaryDTO> asit				= getDTO().getAssessmentSummary().iterator();
			int idx = 0;
			while ( asit.hasNext() )
			{
				AssessmentSummaryDTO assessmentSummaryDTO	= asit.next();
				convertor.setTypeFromDTO( assessmentSummaryDTO );
				assessmentSummaryTypes[idx++]		= convertor.getOracleType();
			}

		}
		getOracleType().setAssessmentsSummaryTab(new AssessmentSummaryTabType( assessmentSummaryTypes ));
	}

	private void convertApplicantLinksFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		ApplicantLinkConvertor 	alconvertor			= new ApplicantLinkConvertor();
		ApplicantLinkType[] applicantLinkTypes		= new ApplicantLinkType[ getDTO().getApplicantLinks().size() ];
		if ( ( getDTO().getApplicantLinks() != null ) && ( getDTO().getApplicantLinks().size() > 0 ) )
		{
			Iterator<ApplicantLinkDTO> aplit				= getDTO().getApplicantLinks().iterator();
			int idx = 0;
			while ( aplit.hasNext() )
			{
				ApplicantLinkDTO applicantLinkDTO	= aplit.next();
				alconvertor.setTypeFromDTO( applicantLinkDTO );
				applicantLinkTypes[idx++]		= alconvertor.getOracleType();
			}

		}
		getOracleType().setApplicantLinksTab(new ApplicantLinksTabtype( applicantLinkTypes ));
	}

	private void convertPassportedFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		if(getDTO().getPassportedDTO() != null){
			PassportedConvertor passportConvertor = new PassportedConvertor();
			passportConvertor.setTypeFromDTO(getDTO().getPassportedDTO());
			getOracleType().setPassportAssessmentObject(passportConvertor.getOracleType());
		}
	}

	private void convertAllowedWorkReasonsFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		if(getDTO().getAllowedWorkReasonDTO() != null){

			AllowedWorkReasonConvertor allowedWorkReasonConvertor = new AllowedWorkReasonConvertor();
			allowedWorkReasonConvertor.setTypeFromDTO(getDTO().getAllowedWorkReasonDTO());
			getOracleType().setAllowedWorkReasons(allowedWorkReasonConvertor.getOracleType());
		}
	}

	private void convertDigitalMeansAssessmentFromDTO() throws MAATSystemException, MAATApplicationException, SQLException {
		// MW - 30/03/2017 - FIP Changes
		DigitalMeansAssessmentConvertor 	dmaConvertor			= new DigitalMeansAssessmentConvertor();
		DigiMeansAssessType[] digiMeansAssessmentTypes		= new DigiMeansAssessType[ getDTO().getMeansAssessments().size() ];

		if ( ( getDTO().getMeansAssessments() != null ) && ( getDTO().getMeansAssessments().size() > 0 ) )
		{
			Iterator<DigitisedMeansAssessmentDTO> dmaIt				= getDTO().getMeansAssessments().iterator();
			int idx = 0;
			while ( dmaIt.hasNext() )
			{
				DigitisedMeansAssessmentDTO dmaDTO	= dmaIt.next();
				dmaConvertor.setTypeFromDTO( dmaDTO );
				digiMeansAssessmentTypes[idx++]		= dmaConvertor.getOracleType();
			}

		}
		getOracleType().setDigiMeansAssessments(new DigiMeansAssessTabtype( digiMeansAssessmentTypes ));
	}
}
