package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ApplicationType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APPLICATION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,91,91,12,12,12,2002,2,91,91,12,91,91,91,12,12,93,12,2002,2002,2002,2002,2002,2002,2002,2002,2002,2002,2002,2002,2002,2003,2002,2002,2003,12,12,91,2002,2,12,12,91,91,12,12,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[48];
  static
  {
    _factory[7] = CmuType.getORADataFactory();
    _factory[19] = ContraryInterestType.getORADataFactory();
    _factory[20] = DecisionReasonType.getORADataFactory();
    _factory[21] = RepStatusType.getORADataFactory();
    _factory[22] = CaseTypeType.getORADataFactory();
    _factory[23] = OffenceTypeType.getORADataFactory();
    _factory[24] = MagsCourtType.getORADataFactory();
    _factory[25] = OutcomeType.getORADataFactory();
    _factory[26] = ApplicantDetailsType.getORADataFactory();
    _factory[27] = SupplierType.getORADataFactory();
    _factory[28] = LSCTransferType.getORADataFactory();
    _factory[29] = AreaTransferType.getORADataFactory();
    _factory[30] = PassportAssessmentType.getORADataFactory();
    _factory[31] = AssessmentType.getORADataFactory();
    _factory[32] = AssessmentSummaryTabType.getORADataFactory();
    _factory[33] = CrownCourtOverviewType.getORADataFactory();
    _factory[34] = CapitalEquityType.getORADataFactory();
    _factory[35] = ApplicantLinksTabtype.getORADataFactory();
    _factory[39] = AllowedWorkReasonType.getORADataFactory();
    _factory[47] = DigiMeansAssessTabtype.getORADataFactory();
  }
  protected static final ApplicationType _ApplicationTypeFactory = new ApplicationType();

  public static ORADataFactory getORADataFactory()
  { return _ApplicationTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[48], _sqlType, _factory); }
  public ApplicationType()
  { _init_struct(true); }
  public ApplicationType(java.math.BigDecimal repId, String caseId, java.sql.Timestamp dateReceived, java.sql.Timestamp dateStatusSet, String arrestSummonsNo, String statusReason, String iojResult, CmuType cmuObject, java.math.BigDecimal areaId, java.sql.Timestamp committalDate, java.sql.Timestamp magsCourtOutcomeDate, String courtCustody, java.sql.Timestamp decisionDate, java.sql.Timestamp statusDueDate, java.sql.Timestamp magsWithdrawalDate, String welshCorrespondence, String partner, java.sql.Timestamp timeStamp, String alertMessage, ContraryInterestType contraryInterestObject, DecisionReasonType decisionReasonObject, RepStatusType statusObject, CaseTypeType caseTypeObject, OffenceTypeType offenceTypeObject, MagsCourtType magsCourtObject, OutcomeType magsOutcomeObject, ApplicantDetailsType applicantDetailsObject, SupplierType supplierObject, LSCTransferType lscTransfersObject, AreaTransferType areaTransfersObject, PassportAssessmentType passportAssessmentObject, AssessmentType currentAssessmentObject, AssessmentSummaryTabType assessmentsSummaryTab, CrownCourtOverviewType crownCourtOverviewObject, CapitalEquityType capitalEquityObject, ApplicantLinksTabtype applicantLinksTab, String transactionId, String retrial, java.sql.Timestamp appSignedDate, AllowedWorkReasonType allowedWorkReasons, java.math.BigDecimal usn, String iojResultNote, String solicitorName, java.sql.Timestamp efmDateStamp, java.sql.Timestamp hearingDate, String solicitorEmail, String solicitorAdminEmail, DigiMeansAssessTabtype digiMeansAssessments) throws SQLException
  { _init_struct(true);
    setRepId(repId);
    setCaseId(caseId);
    setDateReceived(dateReceived);
    setDateStatusSet(dateStatusSet);
    setArrestSummonsNo(arrestSummonsNo);
    setStatusReason(statusReason);
    setIojResult(iojResult);
    setCmuObject(cmuObject);
    setAreaId(areaId);
    setCommittalDate(committalDate);
    setMagsCourtOutcomeDate(magsCourtOutcomeDate);
    setCourtCustody(courtCustody);
    setDecisionDate(decisionDate);
    setStatusDueDate(statusDueDate);
    setMagsWithdrawalDate(magsWithdrawalDate);
    setWelshCorrespondence(welshCorrespondence);
    setPartner(partner);
    setTimeStamp(timeStamp);
    setAlertMessage(alertMessage);
    setContraryInterestObject(contraryInterestObject);
    setDecisionReasonObject(decisionReasonObject);
    setStatusObject(statusObject);
    setCaseTypeObject(caseTypeObject);
    setOffenceTypeObject(offenceTypeObject);
    setMagsCourtObject(magsCourtObject);
    setMagsOutcomeObject(magsOutcomeObject);
    setApplicantDetailsObject(applicantDetailsObject);
    setSupplierObject(supplierObject);
    setLscTransfersObject(lscTransfersObject);
    setAreaTransfersObject(areaTransfersObject);
    setPassportAssessmentObject(passportAssessmentObject);
    setCurrentAssessmentObject(currentAssessmentObject);
    setAssessmentsSummaryTab(assessmentsSummaryTab);
    setCrownCourtOverviewObject(crownCourtOverviewObject);
    setCapitalEquityObject(capitalEquityObject);
    setApplicantLinksTab(applicantLinksTab);
    setTransactionId(transactionId);
    setRetrial(retrial);
    setAppSignedDate(appSignedDate);
    setAllowedWorkReasons(allowedWorkReasons);
    setUsn(usn);
    setIojResultNote(iojResultNote);
    setSolicitorName(solicitorName);
    setEfmDateStamp(efmDateStamp);
    setHearingDate(hearingDate);
    setSolicitorEmail(solicitorEmail);
    setSolicitorAdminEmail(solicitorAdminEmail);
    setDigiMeansAssessments(digiMeansAssessments);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ApplicationType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ApplicationType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getRepId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setRepId(java.math.BigDecimal repId) throws SQLException
  { _struct.setAttribute(0, repId); }


  public String getCaseId() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setCaseId(String caseId) throws SQLException
  { _struct.setAttribute(1, caseId); }


  public java.sql.Timestamp getDateReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setDateReceived(java.sql.Timestamp dateReceived) throws SQLException
  { _struct.setAttribute(2, dateReceived); }


  public java.sql.Timestamp getDateStatusSet() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateStatusSet(java.sql.Timestamp dateStatusSet) throws SQLException
  { _struct.setAttribute(3, dateStatusSet); }


  public String getArrestSummonsNo() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setArrestSummonsNo(String arrestSummonsNo) throws SQLException
  { _struct.setAttribute(4, arrestSummonsNo); }


  public String getStatusReason() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setStatusReason(String statusReason) throws SQLException
  { _struct.setAttribute(5, statusReason); }


  public String getIojResult() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setIojResult(String iojResult) throws SQLException
  { _struct.setAttribute(6, iojResult); }


  public CmuType getCmuObject() throws SQLException
  { return (CmuType) _struct.getAttribute(7); }

  public void setCmuObject(CmuType cmuObject) throws SQLException
  { _struct.setAttribute(7, cmuObject); }


  public java.math.BigDecimal getAreaId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(8); }

  public void setAreaId(java.math.BigDecimal areaId) throws SQLException
  { _struct.setAttribute(8, areaId); }


  public java.sql.Timestamp getCommittalDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(9); }

  public void setCommittalDate(java.sql.Timestamp committalDate) throws SQLException
  { _struct.setAttribute(9, committalDate); }


  public java.sql.Timestamp getMagsCourtOutcomeDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(10); }

  public void setMagsCourtOutcomeDate(java.sql.Timestamp magsCourtOutcomeDate) throws SQLException
  { _struct.setAttribute(10, magsCourtOutcomeDate); }


  public String getCourtCustody() throws SQLException
  { return (String) _struct.getAttribute(11); }

  public void setCourtCustody(String courtCustody) throws SQLException
  { _struct.setAttribute(11, courtCustody); }


  public java.sql.Timestamp getDecisionDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(12); }

  public void setDecisionDate(java.sql.Timestamp decisionDate) throws SQLException
  { _struct.setAttribute(12, decisionDate); }


  public java.sql.Timestamp getStatusDueDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(13); }

  public void setStatusDueDate(java.sql.Timestamp statusDueDate) throws SQLException
  { _struct.setAttribute(13, statusDueDate); }


  public java.sql.Timestamp getMagsWithdrawalDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(14); }

  public void setMagsWithdrawalDate(java.sql.Timestamp magsWithdrawalDate) throws SQLException
  { _struct.setAttribute(14, magsWithdrawalDate); }


  public String getWelshCorrespondence() throws SQLException
  { return (String) _struct.getAttribute(15); }

  public void setWelshCorrespondence(String welshCorrespondence) throws SQLException
  { _struct.setAttribute(15, welshCorrespondence); }


  public String getPartner() throws SQLException
  { return (String) _struct.getAttribute(16); }

  public void setPartner(String partner) throws SQLException
  { _struct.setAttribute(16, partner); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(17); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(17, timeStamp); }


  public String getAlertMessage() throws SQLException
  { return (String) _struct.getAttribute(18); }

  public void setAlertMessage(String alertMessage) throws SQLException
  { _struct.setAttribute(18, alertMessage); }


  public ContraryInterestType getContraryInterestObject() throws SQLException
  { return (ContraryInterestType) _struct.getAttribute(19); }

  public void setContraryInterestObject(ContraryInterestType contraryInterestObject) throws SQLException
  { _struct.setAttribute(19, contraryInterestObject); }


  public DecisionReasonType getDecisionReasonObject() throws SQLException
  { return (DecisionReasonType) _struct.getAttribute(20); }

  public void setDecisionReasonObject(DecisionReasonType decisionReasonObject) throws SQLException
  { _struct.setAttribute(20, decisionReasonObject); }


  public RepStatusType getStatusObject() throws SQLException
  { return (RepStatusType) _struct.getAttribute(21); }

  public void setStatusObject(RepStatusType statusObject) throws SQLException
  { _struct.setAttribute(21, statusObject); }


  public CaseTypeType getCaseTypeObject() throws SQLException
  { return (CaseTypeType) _struct.getAttribute(22); }

  public void setCaseTypeObject(CaseTypeType caseTypeObject) throws SQLException
  { _struct.setAttribute(22, caseTypeObject); }


  public OffenceTypeType getOffenceTypeObject() throws SQLException
  { return (OffenceTypeType) _struct.getAttribute(23); }

  public void setOffenceTypeObject(OffenceTypeType offenceTypeObject) throws SQLException
  { _struct.setAttribute(23, offenceTypeObject); }


  public MagsCourtType getMagsCourtObject() throws SQLException
  { return (MagsCourtType) _struct.getAttribute(24); }

  public void setMagsCourtObject(MagsCourtType magsCourtObject) throws SQLException
  { _struct.setAttribute(24, magsCourtObject); }


  public OutcomeType getMagsOutcomeObject() throws SQLException
  { return (OutcomeType) _struct.getAttribute(25); }

  public void setMagsOutcomeObject(OutcomeType magsOutcomeObject) throws SQLException
  { _struct.setAttribute(25, magsOutcomeObject); }


  public ApplicantDetailsType getApplicantDetailsObject() throws SQLException
  { return (ApplicantDetailsType) _struct.getAttribute(26); }

  public void setApplicantDetailsObject(ApplicantDetailsType applicantDetailsObject) throws SQLException
  { _struct.setAttribute(26, applicantDetailsObject); }


  public SupplierType getSupplierObject() throws SQLException
  { return (SupplierType) _struct.getAttribute(27); }

  public void setSupplierObject(SupplierType supplierObject) throws SQLException
  { _struct.setAttribute(27, supplierObject); }


  public LSCTransferType getLscTransfersObject() throws SQLException
  { return (LSCTransferType) _struct.getAttribute(28); }

  public void setLscTransfersObject(LSCTransferType lscTransfersObject) throws SQLException
  { _struct.setAttribute(28, lscTransfersObject); }


  public AreaTransferType getAreaTransfersObject() throws SQLException
  { return (AreaTransferType) _struct.getAttribute(29); }

  public void setAreaTransfersObject(AreaTransferType areaTransfersObject) throws SQLException
  { _struct.setAttribute(29, areaTransfersObject); }


  public PassportAssessmentType getPassportAssessmentObject() throws SQLException
  { return (PassportAssessmentType) _struct.getAttribute(30); }

  public void setPassportAssessmentObject(PassportAssessmentType passportAssessmentObject) throws SQLException
  { _struct.setAttribute(30, passportAssessmentObject); }


  public AssessmentType getCurrentAssessmentObject() throws SQLException
  { return (AssessmentType) _struct.getAttribute(31); }

  public void setCurrentAssessmentObject(AssessmentType currentAssessmentObject) throws SQLException
  { _struct.setAttribute(31, currentAssessmentObject); }


  public AssessmentSummaryTabType getAssessmentsSummaryTab() throws SQLException
  { return (AssessmentSummaryTabType) _struct.getAttribute(32); }

  public void setAssessmentsSummaryTab(AssessmentSummaryTabType assessmentsSummaryTab) throws SQLException
  { _struct.setAttribute(32, assessmentsSummaryTab); }


  public CrownCourtOverviewType getCrownCourtOverviewObject() throws SQLException
  { return (CrownCourtOverviewType) _struct.getAttribute(33); }

  public void setCrownCourtOverviewObject(CrownCourtOverviewType crownCourtOverviewObject) throws SQLException
  { _struct.setAttribute(33, crownCourtOverviewObject); }


  public CapitalEquityType getCapitalEquityObject() throws SQLException
  { return (CapitalEquityType) _struct.getAttribute(34); }

  public void setCapitalEquityObject(CapitalEquityType capitalEquityObject) throws SQLException
  { _struct.setAttribute(34, capitalEquityObject); }


  public ApplicantLinksTabtype getApplicantLinksTab() throws SQLException
  { return (ApplicantLinksTabtype) _struct.getAttribute(35); }

  public void setApplicantLinksTab(ApplicantLinksTabtype applicantLinksTab) throws SQLException
  { _struct.setAttribute(35, applicantLinksTab); }


  public String getTransactionId() throws SQLException
  { return (String) _struct.getAttribute(36); }

  public void setTransactionId(String transactionId) throws SQLException
  { _struct.setAttribute(36, transactionId); }

  public String getRetrial() throws SQLException
  { return (String) _struct.getAttribute(37); }

  public void setRetrial(String retrial) throws SQLException
  { _struct.setAttribute(37, retrial); }


  public java.sql.Timestamp getAppSignedDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(38); }

  public void setAppSignedDate(java.sql.Timestamp appSignedDate) throws SQLException
  { _struct.setAttribute(38, appSignedDate); }


  public AllowedWorkReasonType getAllowedWorkReasons() throws SQLException
  { return (AllowedWorkReasonType) _struct.getAttribute(39); }

  public void setAllowedWorkReasons(AllowedWorkReasonType allowedWorkReasons) throws SQLException
  { _struct.setAttribute(39, allowedWorkReasons); }


  public java.math.BigDecimal getUsn() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(40); }

  public void setUsn(java.math.BigDecimal usn) throws SQLException
  { _struct.setAttribute(40, usn); }


  public String getIojResultNote() throws SQLException
  { return (String) _struct.getAttribute(41); }

  public void setIojResultNote(String iojResultNote) throws SQLException
  { _struct.setAttribute(41, iojResultNote); }


  public String getSolicitorName() throws SQLException
  { return (String) _struct.getAttribute(42); }

  public void setSolicitorName(String solicitorName) throws SQLException
  { _struct.setAttribute(42, solicitorName); }


  public java.sql.Timestamp getEfmDateStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(43); }

  public void setEfmDateStamp(java.sql.Timestamp efmDateStamp) throws SQLException
  { _struct.setAttribute(43, efmDateStamp); }


  public java.sql.Timestamp getHearingDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(44); }

  public void setHearingDate(java.sql.Timestamp hearingDate) throws SQLException
  { _struct.setAttribute(44, hearingDate); }


  public String getSolicitorEmail() throws SQLException
  { return (String) _struct.getAttribute(45); }

  public void setSolicitorEmail(String solicitorEmail) throws SQLException
  { _struct.setAttribute(45, solicitorEmail); }


  public String getSolicitorAdminEmail() throws SQLException
  { return (String) _struct.getAttribute(46); }

  public void setSolicitorAdminEmail(String solicitorAdminEmail) throws SQLException
  { _struct.setAttribute(46, solicitorAdminEmail); }


  public DigiMeansAssessTabtype getDigiMeansAssessments() throws SQLException
  { return (DigiMeansAssessTabtype) _struct.getAttribute(47); }

  public void setDigiMeansAssessments(DigiMeansAssessTabtype digiMeansAssessments) throws SQLException
  { _struct.setAttribute(47, digiMeansAssessments); }

}
