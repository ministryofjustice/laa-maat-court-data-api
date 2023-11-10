package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ReportDataType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.REPORT_DATATYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,12,91,12,12,91,2,2,2,2,2,2,2,12,12,91,2,12,12,91,91,91,91,12,2,2,2,2,2,12,12,12,12,12,12,93,12,12,91,12,2,2,2,2,2,2,91,12,91,91,91,2,12,12,91,12,91,91,2,91,91,12,12,91,2,2,2,2,91,2,91,12,12,12,12,12,12,12,12,2,91,12,12,12,12,12,12,12,12,12,12,91,91,91,91,91,91,12,2,2,2,12,12,12,12,12,12,12,12,12,12,12,12,12,91,12,2,12,93,12,12,12,91,12,12,91,2005,2,12,91,12,2,2,12,12,12,12,12,12,2,2,2,2,2,2,2,2 };
  protected static ORADataFactory[] _factory = new ORADataFactory[148];
  protected static final ReportDataType _ReportDataTypeFactory = new ReportDataType();

  public static ORADataFactory getORADataFactory()
  { return _ReportDataTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[148], _sqlType, _factory); }
  public ReportDataType()
  { _init_struct(true); }
  public ReportDataType(java.math.BigDecimal reportId, java.math.BigDecimal areaId, String areaName, java.sql.Timestamp runDate, String timePeriodCovered, String cmuName, java.sql.Timestamp weekBeginning, java.math.BigDecimal processedInTwo, java.math.BigDecimal processedOutTwo, java.math.BigDecimal processedInThree, java.math.BigDecimal processedOutThree, java.math.BigDecimal processedInSix, java.math.BigDecimal processedOutSix, java.math.BigDecimal processedTotal, String staffName, String typeOfWork, java.sql.Timestamp dateReceived, java.math.BigDecimal maatId, String caseId, String appSurname, java.sql.Timestamp datePending, java.sql.Timestamp dateSent, java.sql.Timestamp dateCompleted, java.sql.Timestamp dateReturned, String dwpType, java.math.BigDecimal dwpTotalChecks, java.math.BigDecimal dwpPassed, java.math.BigDecimal dwpFailed, java.math.BigDecimal dwpNotDetermined, java.math.BigDecimal dwpSearchNotPossible, String userName, String roleDescription, String userRoleEnabled, String enabled, String transFromAreaName, String transFromCmuName, java.sql.Timestamp timeStamp, String transToAreaName, String transToCmuName, java.sql.Timestamp userLoginDate, String caseStatusDescr, java.math.BigDecimal processedGranted, java.math.BigDecimal processedFailed, java.math.BigDecimal dwpDeleted, java.math.BigDecimal dwpDeceased, java.math.BigDecimal dwpSuperseded, java.math.BigDecimal cmuId, java.sql.Timestamp drivingEventDate, String caseType, java.sql.Timestamp magistrateCommittalDate, java.sql.Timestamp assessmentDate, java.sql.Timestamp outcomeDate, java.math.BigDecimal numberOfDays, String appFirstName, String court, java.sql.Timestamp evidenceDueDate, String transferType, java.sql.Timestamp referralDate, java.sql.Timestamp dateOfBirth, java.math.BigDecimal fdcAmountSent, java.sql.Timestamp fdcCalculationDate, java.sql.Timestamp fdcDateSent, String empStatus, String crownCourtOutcome, java.sql.Timestamp sentenceOrderDate, java.math.BigDecimal totalDeclaredCapital, java.math.BigDecimal totalAgfsComponent, java.math.BigDecimal totalLgfsComponent, java.math.BigDecimal agfsAdjustmentAmt, java.sql.Timestamp dateAgfsAdjusted, java.math.BigDecimal lgfsAdjustmentAmt, java.sql.Timestamp dateLgfsAdjusted, String agfsAdjustmentReason, String lgfsAdjustmentReason, String supplierCode, String caseNumber, String offenceType, String magsOutcome, String assessmentResult, String assessmentReason, java.math.BigDecimal contributionAmount, java.sql.Timestamp contributionDateSent, String appealType, String arrestSummonsNumber, String crownCourtCode, String crownCourtName, String iojResult, String iojAppealResult, String hearingResult, String hearingType, String repType, String errorMessage, java.sql.Timestamp dateCaseCreated, java.sql.Timestamp repOrderDecisionDate, java.sql.Timestamp ccRepOrderDate, java.sql.Timestamp fullAssessmentDate, java.sql.Timestamp incomeEvidencePrintDate, java.sql.Timestamp contrNoticeOrderPrintDate, String capitalEquityStatus, java.math.BigDecimal processedInSeven, java.math.BigDecimal processedOutSeven, java.math.BigDecimal appsNotCompleted, String fullAssessmentOutstanding, String incEvidReqNeedsPrinting, String contrNotOrdNeedsPrinting, String capitalEquityIncomplete, String xtraFailureReason, String previousFdcFileSent, String nullMaatCommittalDate, String nullMaatAsn, String committalDateUnmatched, String asnUnmatched, String lastNameUnmatched, String firstNameUnmatched, String dobUnmatched, java.sql.Timestamp dateOfLastMatchAttempt, String crownCourtImprisoned, java.math.BigDecimal numberOfCharges, String xhibitFileName, java.sql.Timestamp xhibitFileTimestamp, String benchWarrantIssued, String repOrderStatus, String xhibitCcOutcome, java.sql.Timestamp xhibitCcOutcomeDate, String ccRepOrderDecision, String ccRepOrderWithdrawn, java.sql.Timestamp ccRoWithdrawalDate, oracle.sql.CLOB chargeDetails, java.math.BigDecimal workTypeOrder, String billScenario, java.sql.Timestamp dateBillPaid, String altMaatRefs, java.math.BigDecimal totalAsns, java.math.BigDecimal dummyAsns, String contrNoticeNeedsPrinting, String contrOrderNeedsPrinting, String incEvidReqNeedsGenerating, String incEvidRemNeedsPrinting, String capEvidReqNeedsGenerating, String capEvidReqNeedsPrinting, java.math.BigDecimal volTotalTrials, java.math.BigDecimal volContrNoticesNeedPrint, java.math.BigDecimal volContrOrdersNeedPrint, java.math.BigDecimal volIncEvidReqsNeedPrint, java.math.BigDecimal volIncEvidRemsNeedPrint, java.math.BigDecimal volCapEvidReqsNeedPrint, java.math.BigDecimal volIncEvidReqsNeedGen, java.math.BigDecimal volCapEvidReqsNeedGen) throws SQLException
  { _init_struct(true);
    setReportId(reportId);
    setAreaId(areaId);
    setAreaName(areaName);
    setRunDate(runDate);
    setTimePeriodCovered(timePeriodCovered);
    setCmuName(cmuName);
    setWeekBeginning(weekBeginning);
    setProcessedInTwo(processedInTwo);
    setProcessedOutTwo(processedOutTwo);
    setProcessedInThree(processedInThree);
    setProcessedOutThree(processedOutThree);
    setProcessedInSix(processedInSix);
    setProcessedOutSix(processedOutSix);
    setProcessedTotal(processedTotal);
    setStaffName(staffName);
    setTypeOfWork(typeOfWork);
    setDateReceived(dateReceived);
    setMaatId(maatId);
    setCaseId(caseId);
    setAppSurname(appSurname);
    setDatePending(datePending);
    setDateSent(dateSent);
    setDateCompleted(dateCompleted);
    setDateReturned(dateReturned);
    setDwpType(dwpType);
    setDwpTotalChecks(dwpTotalChecks);
    setDwpPassed(dwpPassed);
    setDwpFailed(dwpFailed);
    setDwpNotDetermined(dwpNotDetermined);
    setDwpSearchNotPossible(dwpSearchNotPossible);
    setUserName(userName);
    setRoleDescription(roleDescription);
    setUserRoleEnabled(userRoleEnabled);
    setEnabled(enabled);
    setTransFromAreaName(transFromAreaName);
    setTransFromCmuName(transFromCmuName);
    setTimeStamp(timeStamp);
    setTransToAreaName(transToAreaName);
    setTransToCmuName(transToCmuName);
    setUserLoginDate(userLoginDate);
    setCaseStatusDescr(caseStatusDescr);
    setProcessedGranted(processedGranted);
    setProcessedFailed(processedFailed);
    setDwpDeleted(dwpDeleted);
    setDwpDeceased(dwpDeceased);
    setDwpSuperseded(dwpSuperseded);
    setCmuId(cmuId);
    setDrivingEventDate(drivingEventDate);
    setCaseType(caseType);
    setMagistrateCommittalDate(magistrateCommittalDate);
    setAssessmentDate(assessmentDate);
    setOutcomeDate(outcomeDate);
    setNumberOfDays(numberOfDays);
    setAppFirstName(appFirstName);
    setCourt(court);
    setEvidenceDueDate(evidenceDueDate);
    setTransferType(transferType);
    setReferralDate(referralDate);
    setDateOfBirth(dateOfBirth);
    setFdcAmountSent(fdcAmountSent);
    setFdcCalculationDate(fdcCalculationDate);
    setFdcDateSent(fdcDateSent);
    setEmpStatus(empStatus);
    setCrownCourtOutcome(crownCourtOutcome);
    setSentenceOrderDate(sentenceOrderDate);
    setTotalDeclaredCapital(totalDeclaredCapital);
    setTotalAgfsComponent(totalAgfsComponent);
    setTotalLgfsComponent(totalLgfsComponent);
    setAgfsAdjustmentAmt(agfsAdjustmentAmt);
    setDateAgfsAdjusted(dateAgfsAdjusted);
    setLgfsAdjustmentAmt(lgfsAdjustmentAmt);
    setDateLgfsAdjusted(dateLgfsAdjusted);
    setAgfsAdjustmentReason(agfsAdjustmentReason);
    setLgfsAdjustmentReason(lgfsAdjustmentReason);
    setSupplierCode(supplierCode);
    setCaseNumber(caseNumber);
    setOffenceType(offenceType);
    setMagsOutcome(magsOutcome);
    setAssessmentResult(assessmentResult);
    setAssessmentReason(assessmentReason);
    setContributionAmount(contributionAmount);
    setContributionDateSent(contributionDateSent);
    setAppealType(appealType);
    setArrestSummonsNumber(arrestSummonsNumber);
    setCrownCourtCode(crownCourtCode);
    setCrownCourtName(crownCourtName);
    setIojResult(iojResult);
    setIojAppealResult(iojAppealResult);
    setHearingResult(hearingResult);
    setHearingType(hearingType);
    setRepType(repType);
    setErrorMessage(errorMessage);
    setDateCaseCreated(dateCaseCreated);
    setRepOrderDecisionDate(repOrderDecisionDate);
    setCcRepOrderDate(ccRepOrderDate);
    setFullAssessmentDate(fullAssessmentDate);
    setIncomeEvidencePrintDate(incomeEvidencePrintDate);
    setContrNoticeOrderPrintDate(contrNoticeOrderPrintDate);
    setCapitalEquityStatus(capitalEquityStatus);
    setProcessedInSeven(processedInSeven);
    setProcessedOutSeven(processedOutSeven);
    setAppsNotCompleted(appsNotCompleted);
    setFullAssessmentOutstanding(fullAssessmentOutstanding);
    setIncEvidReqNeedsPrinting(incEvidReqNeedsPrinting);
    setContrNotOrdNeedsPrinting(contrNotOrdNeedsPrinting);
    setCapitalEquityIncomplete(capitalEquityIncomplete);
    setXtraFailureReason(xtraFailureReason);
    setPreviousFdcFileSent(previousFdcFileSent);
    setNullMaatCommittalDate(nullMaatCommittalDate);
    setNullMaatAsn(nullMaatAsn);
    setCommittalDateUnmatched(committalDateUnmatched);
    setAsnUnmatched(asnUnmatched);
    setLastNameUnmatched(lastNameUnmatched);
    setFirstNameUnmatched(firstNameUnmatched);
    setDobUnmatched(dobUnmatched);
    setDateOfLastMatchAttempt(dateOfLastMatchAttempt);
    setCrownCourtImprisoned(crownCourtImprisoned);
    setNumberOfCharges(numberOfCharges);
    setXhibitFileName(xhibitFileName);
    setXhibitFileTimestamp(xhibitFileTimestamp);
    setBenchWarrantIssued(benchWarrantIssued);
    setRepOrderStatus(repOrderStatus);
    setXhibitCcOutcome(xhibitCcOutcome);
    setXhibitCcOutcomeDate(xhibitCcOutcomeDate);
    setCcRepOrderDecision(ccRepOrderDecision);
    setCcRepOrderWithdrawn(ccRepOrderWithdrawn);
    setCcRoWithdrawalDate(ccRoWithdrawalDate);
    setChargeDetails(chargeDetails);
    setWorkTypeOrder(workTypeOrder);
    setBillScenario(billScenario);
    setDateBillPaid(dateBillPaid);
    setAltMaatRefs(altMaatRefs);
    setTotalAsns(totalAsns);
    setDummyAsns(dummyAsns);
    setContrNoticeNeedsPrinting(contrNoticeNeedsPrinting);
    setContrOrderNeedsPrinting(contrOrderNeedsPrinting);
    setIncEvidReqNeedsGenerating(incEvidReqNeedsGenerating);
    setIncEvidRemNeedsPrinting(incEvidRemNeedsPrinting);
    setCapEvidReqNeedsGenerating(capEvidReqNeedsGenerating);
    setCapEvidReqNeedsPrinting(capEvidReqNeedsPrinting);
    setVolTotalTrials(volTotalTrials);
    setVolContrNoticesNeedPrint(volContrNoticesNeedPrint);
    setVolContrOrdersNeedPrint(volContrOrdersNeedPrint);
    setVolIncEvidReqsNeedPrint(volIncEvidReqsNeedPrint);
    setVolIncEvidRemsNeedPrint(volIncEvidRemsNeedPrint);
    setVolCapEvidReqsNeedPrint(volCapEvidReqsNeedPrint);
    setVolIncEvidReqsNeedGen(volIncEvidReqsNeedGen);
    setVolCapEvidReqsNeedGen(volCapEvidReqsNeedGen);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ReportDataType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ReportDataType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getReportId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setReportId(java.math.BigDecimal reportId) throws SQLException
  { _struct.setAttribute(0, reportId); }


  public java.math.BigDecimal getAreaId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setAreaId(java.math.BigDecimal areaId) throws SQLException
  { _struct.setAttribute(1, areaId); }


  public String getAreaName() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setAreaName(String areaName) throws SQLException
  { _struct.setAttribute(2, areaName); }


  public java.sql.Timestamp getRunDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setRunDate(java.sql.Timestamp runDate) throws SQLException
  { _struct.setAttribute(3, runDate); }


  public String getTimePeriodCovered() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setTimePeriodCovered(String timePeriodCovered) throws SQLException
  { _struct.setAttribute(4, timePeriodCovered); }


  public String getCmuName() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setCmuName(String cmuName) throws SQLException
  { _struct.setAttribute(5, cmuName); }


  public java.sql.Timestamp getWeekBeginning() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setWeekBeginning(java.sql.Timestamp weekBeginning) throws SQLException
  { _struct.setAttribute(6, weekBeginning); }


  public java.math.BigDecimal getProcessedInTwo() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(7); }

  public void setProcessedInTwo(java.math.BigDecimal processedInTwo) throws SQLException
  { _struct.setAttribute(7, processedInTwo); }


  public java.math.BigDecimal getProcessedOutTwo() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(8); }

  public void setProcessedOutTwo(java.math.BigDecimal processedOutTwo) throws SQLException
  { _struct.setAttribute(8, processedOutTwo); }


  public java.math.BigDecimal getProcessedInThree() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(9); }

  public void setProcessedInThree(java.math.BigDecimal processedInThree) throws SQLException
  { _struct.setAttribute(9, processedInThree); }


  public java.math.BigDecimal getProcessedOutThree() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(10); }

  public void setProcessedOutThree(java.math.BigDecimal processedOutThree) throws SQLException
  { _struct.setAttribute(10, processedOutThree); }


  public java.math.BigDecimal getProcessedInSix() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(11); }

  public void setProcessedInSix(java.math.BigDecimal processedInSix) throws SQLException
  { _struct.setAttribute(11, processedInSix); }


  public java.math.BigDecimal getProcessedOutSix() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(12); }

  public void setProcessedOutSix(java.math.BigDecimal processedOutSix) throws SQLException
  { _struct.setAttribute(12, processedOutSix); }


  public java.math.BigDecimal getProcessedTotal() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(13); }

  public void setProcessedTotal(java.math.BigDecimal processedTotal) throws SQLException
  { _struct.setAttribute(13, processedTotal); }


  public String getStaffName() throws SQLException
  { return (String) _struct.getAttribute(14); }

  public void setStaffName(String staffName) throws SQLException
  { _struct.setAttribute(14, staffName); }


  public String getTypeOfWork() throws SQLException
  { return (String) _struct.getAttribute(15); }

  public void setTypeOfWork(String typeOfWork) throws SQLException
  { _struct.setAttribute(15, typeOfWork); }


  public java.sql.Timestamp getDateReceived() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(16); }

  public void setDateReceived(java.sql.Timestamp dateReceived) throws SQLException
  { _struct.setAttribute(16, dateReceived); }


  public java.math.BigDecimal getMaatId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(17); }

  public void setMaatId(java.math.BigDecimal maatId) throws SQLException
  { _struct.setAttribute(17, maatId); }


  public String getCaseId() throws SQLException
  { return (String) _struct.getAttribute(18); }

  public void setCaseId(String caseId) throws SQLException
  { _struct.setAttribute(18, caseId); }


  public String getAppSurname() throws SQLException
  { return (String) _struct.getAttribute(19); }

  public void setAppSurname(String appSurname) throws SQLException
  { _struct.setAttribute(19, appSurname); }


  public java.sql.Timestamp getDatePending() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(20); }

  public void setDatePending(java.sql.Timestamp datePending) throws SQLException
  { _struct.setAttribute(20, datePending); }


  public java.sql.Timestamp getDateSent() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(21); }

  public void setDateSent(java.sql.Timestamp dateSent) throws SQLException
  { _struct.setAttribute(21, dateSent); }


  public java.sql.Timestamp getDateCompleted() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(22); }

  public void setDateCompleted(java.sql.Timestamp dateCompleted) throws SQLException
  { _struct.setAttribute(22, dateCompleted); }


  public java.sql.Timestamp getDateReturned() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(23); }

  public void setDateReturned(java.sql.Timestamp dateReturned) throws SQLException
  { _struct.setAttribute(23, dateReturned); }


  public String getDwpType() throws SQLException
  { return (String) _struct.getAttribute(24); }

  public void setDwpType(String dwpType) throws SQLException
  { _struct.setAttribute(24, dwpType); }


  public java.math.BigDecimal getDwpTotalChecks() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(25); }

  public void setDwpTotalChecks(java.math.BigDecimal dwpTotalChecks) throws SQLException
  { _struct.setAttribute(25, dwpTotalChecks); }


  public java.math.BigDecimal getDwpPassed() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(26); }

  public void setDwpPassed(java.math.BigDecimal dwpPassed) throws SQLException
  { _struct.setAttribute(26, dwpPassed); }


  public java.math.BigDecimal getDwpFailed() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(27); }

  public void setDwpFailed(java.math.BigDecimal dwpFailed) throws SQLException
  { _struct.setAttribute(27, dwpFailed); }


  public java.math.BigDecimal getDwpNotDetermined() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(28); }

  public void setDwpNotDetermined(java.math.BigDecimal dwpNotDetermined) throws SQLException
  { _struct.setAttribute(28, dwpNotDetermined); }


  public java.math.BigDecimal getDwpSearchNotPossible() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(29); }

  public void setDwpSearchNotPossible(java.math.BigDecimal dwpSearchNotPossible) throws SQLException
  { _struct.setAttribute(29, dwpSearchNotPossible); }


  public String getUserName() throws SQLException
  { return (String) _struct.getAttribute(30); }

  public void setUserName(String userName) throws SQLException
  { _struct.setAttribute(30, userName); }


  public String getRoleDescription() throws SQLException
  { return (String) _struct.getAttribute(31); }

  public void setRoleDescription(String roleDescription) throws SQLException
  { _struct.setAttribute(31, roleDescription); }


  public String getUserRoleEnabled() throws SQLException
  { return (String) _struct.getAttribute(32); }

  public void setUserRoleEnabled(String userRoleEnabled) throws SQLException
  { _struct.setAttribute(32, userRoleEnabled); }


  public String getEnabled() throws SQLException
  { return (String) _struct.getAttribute(33); }

  public void setEnabled(String enabled) throws SQLException
  { _struct.setAttribute(33, enabled); }


  public String getTransFromAreaName() throws SQLException
  { return (String) _struct.getAttribute(34); }

  public void setTransFromAreaName(String transFromAreaName) throws SQLException
  { _struct.setAttribute(34, transFromAreaName); }


  public String getTransFromCmuName() throws SQLException
  { return (String) _struct.getAttribute(35); }

  public void setTransFromCmuName(String transFromCmuName) throws SQLException
  { _struct.setAttribute(35, transFromCmuName); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(36); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(36, timeStamp); }


  public String getTransToAreaName() throws SQLException
  { return (String) _struct.getAttribute(37); }

  public void setTransToAreaName(String transToAreaName) throws SQLException
  { _struct.setAttribute(37, transToAreaName); }


  public String getTransToCmuName() throws SQLException
  { return (String) _struct.getAttribute(38); }

  public void setTransToCmuName(String transToCmuName) throws SQLException
  { _struct.setAttribute(38, transToCmuName); }


  public java.sql.Timestamp getUserLoginDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(39); }

  public void setUserLoginDate(java.sql.Timestamp userLoginDate) throws SQLException
  { _struct.setAttribute(39, userLoginDate); }


  public String getCaseStatusDescr() throws SQLException
  { return (String) _struct.getAttribute(40); }

  public void setCaseStatusDescr(String caseStatusDescr) throws SQLException
  { _struct.setAttribute(40, caseStatusDescr); }


  public java.math.BigDecimal getProcessedGranted() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(41); }

  public void setProcessedGranted(java.math.BigDecimal processedGranted) throws SQLException
  { _struct.setAttribute(41, processedGranted); }


  public java.math.BigDecimal getProcessedFailed() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(42); }

  public void setProcessedFailed(java.math.BigDecimal processedFailed) throws SQLException
  { _struct.setAttribute(42, processedFailed); }


  public java.math.BigDecimal getDwpDeleted() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(43); }

  public void setDwpDeleted(java.math.BigDecimal dwpDeleted) throws SQLException
  { _struct.setAttribute(43, dwpDeleted); }


  public java.math.BigDecimal getDwpDeceased() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(44); }

  public void setDwpDeceased(java.math.BigDecimal dwpDeceased) throws SQLException
  { _struct.setAttribute(44, dwpDeceased); }


  public java.math.BigDecimal getDwpSuperseded() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(45); }

  public void setDwpSuperseded(java.math.BigDecimal dwpSuperseded) throws SQLException
  { _struct.setAttribute(45, dwpSuperseded); }


  public java.math.BigDecimal getCmuId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(46); }

  public void setCmuId(java.math.BigDecimal cmuId) throws SQLException
  { _struct.setAttribute(46, cmuId); }


  public java.sql.Timestamp getDrivingEventDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(47); }

  public void setDrivingEventDate(java.sql.Timestamp drivingEventDate) throws SQLException
  { _struct.setAttribute(47, drivingEventDate); }


  public String getCaseType() throws SQLException
  { return (String) _struct.getAttribute(48); }

  public void setCaseType(String caseType) throws SQLException
  { _struct.setAttribute(48, caseType); }


  public java.sql.Timestamp getMagistrateCommittalDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(49); }

  public void setMagistrateCommittalDate(java.sql.Timestamp magistrateCommittalDate) throws SQLException
  { _struct.setAttribute(49, magistrateCommittalDate); }


  public java.sql.Timestamp getAssessmentDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(50); }

  public void setAssessmentDate(java.sql.Timestamp assessmentDate) throws SQLException
  { _struct.setAttribute(50, assessmentDate); }


  public java.sql.Timestamp getOutcomeDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(51); }

  public void setOutcomeDate(java.sql.Timestamp outcomeDate) throws SQLException
  { _struct.setAttribute(51, outcomeDate); }


  public java.math.BigDecimal getNumberOfDays() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(52); }

  public void setNumberOfDays(java.math.BigDecimal numberOfDays) throws SQLException
  { _struct.setAttribute(52, numberOfDays); }


  public String getAppFirstName() throws SQLException
  { return (String) _struct.getAttribute(53); }

  public void setAppFirstName(String appFirstName) throws SQLException
  { _struct.setAttribute(53, appFirstName); }


  public String getCourt() throws SQLException
  { return (String) _struct.getAttribute(54); }

  public void setCourt(String court) throws SQLException
  { _struct.setAttribute(54, court); }


  public java.sql.Timestamp getEvidenceDueDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(55); }

  public void setEvidenceDueDate(java.sql.Timestamp evidenceDueDate) throws SQLException
  { _struct.setAttribute(55, evidenceDueDate); }


  public String getTransferType() throws SQLException
  { return (String) _struct.getAttribute(56); }

  public void setTransferType(String transferType) throws SQLException
  { _struct.setAttribute(56, transferType); }


  public java.sql.Timestamp getReferralDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(57); }

  public void setReferralDate(java.sql.Timestamp referralDate) throws SQLException
  { _struct.setAttribute(57, referralDate); }


  public java.sql.Timestamp getDateOfBirth() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(58); }

  public void setDateOfBirth(java.sql.Timestamp dateOfBirth) throws SQLException
  { _struct.setAttribute(58, dateOfBirth); }


  public java.math.BigDecimal getFdcAmountSent() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(59); }

  public void setFdcAmountSent(java.math.BigDecimal fdcAmountSent) throws SQLException
  { _struct.setAttribute(59, fdcAmountSent); }


  public java.sql.Timestamp getFdcCalculationDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(60); }

  public void setFdcCalculationDate(java.sql.Timestamp fdcCalculationDate) throws SQLException
  { _struct.setAttribute(60, fdcCalculationDate); }


  public java.sql.Timestamp getFdcDateSent() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(61); }

  public void setFdcDateSent(java.sql.Timestamp fdcDateSent) throws SQLException
  { _struct.setAttribute(61, fdcDateSent); }


  public String getEmpStatus() throws SQLException
  { return (String) _struct.getAttribute(62); }

  public void setEmpStatus(String empStatus) throws SQLException
  { _struct.setAttribute(62, empStatus); }


  public String getCrownCourtOutcome() throws SQLException
  { return (String) _struct.getAttribute(63); }

  public void setCrownCourtOutcome(String crownCourtOutcome) throws SQLException
  { _struct.setAttribute(63, crownCourtOutcome); }


  public java.sql.Timestamp getSentenceOrderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(64); }

  public void setSentenceOrderDate(java.sql.Timestamp sentenceOrderDate) throws SQLException
  { _struct.setAttribute(64, sentenceOrderDate); }


  public java.math.BigDecimal getTotalDeclaredCapital() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(65); }

  public void setTotalDeclaredCapital(java.math.BigDecimal totalDeclaredCapital) throws SQLException
  { _struct.setAttribute(65, totalDeclaredCapital); }


  public java.math.BigDecimal getTotalAgfsComponent() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(66); }

  public void setTotalAgfsComponent(java.math.BigDecimal totalAgfsComponent) throws SQLException
  { _struct.setAttribute(66, totalAgfsComponent); }


  public java.math.BigDecimal getTotalLgfsComponent() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(67); }

  public void setTotalLgfsComponent(java.math.BigDecimal totalLgfsComponent) throws SQLException
  { _struct.setAttribute(67, totalLgfsComponent); }


  public java.math.BigDecimal getAgfsAdjustmentAmt() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(68); }

  public void setAgfsAdjustmentAmt(java.math.BigDecimal agfsAdjustmentAmt) throws SQLException
  { _struct.setAttribute(68, agfsAdjustmentAmt); }


  public java.sql.Timestamp getDateAgfsAdjusted() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(69); }

  public void setDateAgfsAdjusted(java.sql.Timestamp dateAgfsAdjusted) throws SQLException
  { _struct.setAttribute(69, dateAgfsAdjusted); }


  public java.math.BigDecimal getLgfsAdjustmentAmt() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(70); }

  public void setLgfsAdjustmentAmt(java.math.BigDecimal lgfsAdjustmentAmt) throws SQLException
  { _struct.setAttribute(70, lgfsAdjustmentAmt); }


  public java.sql.Timestamp getDateLgfsAdjusted() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(71); }

  public void setDateLgfsAdjusted(java.sql.Timestamp dateLgfsAdjusted) throws SQLException
  { _struct.setAttribute(71, dateLgfsAdjusted); }


  public String getAgfsAdjustmentReason() throws SQLException
  { return (String) _struct.getAttribute(72); }

  public void setAgfsAdjustmentReason(String agfsAdjustmentReason) throws SQLException
  { _struct.setAttribute(72, agfsAdjustmentReason); }


  public String getLgfsAdjustmentReason() throws SQLException
  { return (String) _struct.getAttribute(73); }

  public void setLgfsAdjustmentReason(String lgfsAdjustmentReason) throws SQLException
  { _struct.setAttribute(73, lgfsAdjustmentReason); }


  public String getSupplierCode() throws SQLException
  { return (String) _struct.getAttribute(74); }

  public void setSupplierCode(String supplierCode) throws SQLException
  { _struct.setAttribute(74, supplierCode); }


  public String getCaseNumber() throws SQLException
  { return (String) _struct.getAttribute(75); }

  public void setCaseNumber(String caseNumber) throws SQLException
  { _struct.setAttribute(75, caseNumber); }


  public String getOffenceType() throws SQLException
  { return (String) _struct.getAttribute(76); }

  public void setOffenceType(String offenceType) throws SQLException
  { _struct.setAttribute(76, offenceType); }


  public String getMagsOutcome() throws SQLException
  { return (String) _struct.getAttribute(77); }

  public void setMagsOutcome(String magsOutcome) throws SQLException
  { _struct.setAttribute(77, magsOutcome); }


  public String getAssessmentResult() throws SQLException
  { return (String) _struct.getAttribute(78); }

  public void setAssessmentResult(String assessmentResult) throws SQLException
  { _struct.setAttribute(78, assessmentResult); }


  public String getAssessmentReason() throws SQLException
  { return (String) _struct.getAttribute(79); }

  public void setAssessmentReason(String assessmentReason) throws SQLException
  { _struct.setAttribute(79, assessmentReason); }


  public java.math.BigDecimal getContributionAmount() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(80); }

  public void setContributionAmount(java.math.BigDecimal contributionAmount) throws SQLException
  { _struct.setAttribute(80, contributionAmount); }


  public java.sql.Timestamp getContributionDateSent() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(81); }

  public void setContributionDateSent(java.sql.Timestamp contributionDateSent) throws SQLException
  { _struct.setAttribute(81, contributionDateSent); }


  public String getAppealType() throws SQLException
  { return (String) _struct.getAttribute(82); }

  public void setAppealType(String appealType) throws SQLException
  { _struct.setAttribute(82, appealType); }


  public String getArrestSummonsNumber() throws SQLException
  { return (String) _struct.getAttribute(83); }

  public void setArrestSummonsNumber(String arrestSummonsNumber) throws SQLException
  { _struct.setAttribute(83, arrestSummonsNumber); }


  public String getCrownCourtCode() throws SQLException
  { return (String) _struct.getAttribute(84); }

  public void setCrownCourtCode(String crownCourtCode) throws SQLException
  { _struct.setAttribute(84, crownCourtCode); }


  public String getCrownCourtName() throws SQLException
  { return (String) _struct.getAttribute(85); }

  public void setCrownCourtName(String crownCourtName) throws SQLException
  { _struct.setAttribute(85, crownCourtName); }


  public String getIojResult() throws SQLException
  { return (String) _struct.getAttribute(86); }

  public void setIojResult(String iojResult) throws SQLException
  { _struct.setAttribute(86, iojResult); }


  public String getIojAppealResult() throws SQLException
  { return (String) _struct.getAttribute(87); }

  public void setIojAppealResult(String iojAppealResult) throws SQLException
  { _struct.setAttribute(87, iojAppealResult); }


  public String getHearingResult() throws SQLException
  { return (String) _struct.getAttribute(88); }

  public void setHearingResult(String hearingResult) throws SQLException
  { _struct.setAttribute(88, hearingResult); }


  public String getHearingType() throws SQLException
  { return (String) _struct.getAttribute(89); }

  public void setHearingType(String hearingType) throws SQLException
  { _struct.setAttribute(89, hearingType); }


  public String getRepType() throws SQLException
  { return (String) _struct.getAttribute(90); }

  public void setRepType(String repType) throws SQLException
  { _struct.setAttribute(90, repType); }


  public String getErrorMessage() throws SQLException
  { return (String) _struct.getAttribute(91); }

  public void setErrorMessage(String errorMessage) throws SQLException
  { _struct.setAttribute(91, errorMessage); }


  public java.sql.Timestamp getDateCaseCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(92); }

  public void setDateCaseCreated(java.sql.Timestamp dateCaseCreated) throws SQLException
  { _struct.setAttribute(92, dateCaseCreated); }


  public java.sql.Timestamp getRepOrderDecisionDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(93); }

  public void setRepOrderDecisionDate(java.sql.Timestamp repOrderDecisionDate) throws SQLException
  { _struct.setAttribute(93, repOrderDecisionDate); }


  public java.sql.Timestamp getCcRepOrderDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(94); }

  public void setCcRepOrderDate(java.sql.Timestamp ccRepOrderDate) throws SQLException
  { _struct.setAttribute(94, ccRepOrderDate); }


  public java.sql.Timestamp getFullAssessmentDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(95); }

  public void setFullAssessmentDate(java.sql.Timestamp fullAssessmentDate) throws SQLException
  { _struct.setAttribute(95, fullAssessmentDate); }


  public java.sql.Timestamp getIncomeEvidencePrintDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(96); }

  public void setIncomeEvidencePrintDate(java.sql.Timestamp incomeEvidencePrintDate) throws SQLException
  { _struct.setAttribute(96, incomeEvidencePrintDate); }


  public java.sql.Timestamp getContrNoticeOrderPrintDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(97); }

  public void setContrNoticeOrderPrintDate(java.sql.Timestamp contrNoticeOrderPrintDate) throws SQLException
  { _struct.setAttribute(97, contrNoticeOrderPrintDate); }


  public String getCapitalEquityStatus() throws SQLException
  { return (String) _struct.getAttribute(98); }

  public void setCapitalEquityStatus(String capitalEquityStatus) throws SQLException
  { _struct.setAttribute(98, capitalEquityStatus); }


  public java.math.BigDecimal getProcessedInSeven() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(99); }

  public void setProcessedInSeven(java.math.BigDecimal processedInSeven) throws SQLException
  { _struct.setAttribute(99, processedInSeven); }


  public java.math.BigDecimal getProcessedOutSeven() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(100); }

  public void setProcessedOutSeven(java.math.BigDecimal processedOutSeven) throws SQLException
  { _struct.setAttribute(100, processedOutSeven); }


  public java.math.BigDecimal getAppsNotCompleted() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(101); }

  public void setAppsNotCompleted(java.math.BigDecimal appsNotCompleted) throws SQLException
  { _struct.setAttribute(101, appsNotCompleted); }


  public String getFullAssessmentOutstanding() throws SQLException
  { return (String) _struct.getAttribute(102); }

  public void setFullAssessmentOutstanding(String fullAssessmentOutstanding) throws SQLException
  { _struct.setAttribute(102, fullAssessmentOutstanding); }


  public String getIncEvidReqNeedsPrinting() throws SQLException
  { return (String) _struct.getAttribute(103); }

  public void setIncEvidReqNeedsPrinting(String incEvidReqNeedsPrinting) throws SQLException
  { _struct.setAttribute(103, incEvidReqNeedsPrinting); }


  public String getContrNotOrdNeedsPrinting() throws SQLException
  { return (String) _struct.getAttribute(104); }

  public void setContrNotOrdNeedsPrinting(String contrNotOrdNeedsPrinting) throws SQLException
  { _struct.setAttribute(104, contrNotOrdNeedsPrinting); }


  public String getCapitalEquityIncomplete() throws SQLException
  { return (String) _struct.getAttribute(105); }

  public void setCapitalEquityIncomplete(String capitalEquityIncomplete) throws SQLException
  { _struct.setAttribute(105, capitalEquityIncomplete); }


  public String getXtraFailureReason() throws SQLException
  { return (String) _struct.getAttribute(106); }

  public void setXtraFailureReason(String xtraFailureReason) throws SQLException
  { _struct.setAttribute(106, xtraFailureReason); }


  public String getPreviousFdcFileSent() throws SQLException
  { return (String) _struct.getAttribute(107); }

  public void setPreviousFdcFileSent(String previousFdcFileSent) throws SQLException
  { _struct.setAttribute(107, previousFdcFileSent); }


  public String getNullMaatCommittalDate() throws SQLException
  { return (String) _struct.getAttribute(108); }

  public void setNullMaatCommittalDate(String nullMaatCommittalDate) throws SQLException
  { _struct.setAttribute(108, nullMaatCommittalDate); }


  public String getNullMaatAsn() throws SQLException
  { return (String) _struct.getAttribute(109); }

  public void setNullMaatAsn(String nullMaatAsn) throws SQLException
  { _struct.setAttribute(109, nullMaatAsn); }


  public String getCommittalDateUnmatched() throws SQLException
  { return (String) _struct.getAttribute(110); }

  public void setCommittalDateUnmatched(String committalDateUnmatched) throws SQLException
  { _struct.setAttribute(110, committalDateUnmatched); }


  public String getAsnUnmatched() throws SQLException
  { return (String) _struct.getAttribute(111); }

  public void setAsnUnmatched(String asnUnmatched) throws SQLException
  { _struct.setAttribute(111, asnUnmatched); }


  public String getLastNameUnmatched() throws SQLException
  { return (String) _struct.getAttribute(112); }

  public void setLastNameUnmatched(String lastNameUnmatched) throws SQLException
  { _struct.setAttribute(112, lastNameUnmatched); }


  public String getFirstNameUnmatched() throws SQLException
  { return (String) _struct.getAttribute(113); }

  public void setFirstNameUnmatched(String firstNameUnmatched) throws SQLException
  { _struct.setAttribute(113, firstNameUnmatched); }


  public String getDobUnmatched() throws SQLException
  { return (String) _struct.getAttribute(114); }

  public void setDobUnmatched(String dobUnmatched) throws SQLException
  { _struct.setAttribute(114, dobUnmatched); }


  public java.sql.Timestamp getDateOfLastMatchAttempt() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(115); }

  public void setDateOfLastMatchAttempt(java.sql.Timestamp dateOfLastMatchAttempt) throws SQLException
  { _struct.setAttribute(115, dateOfLastMatchAttempt); }


  public String getCrownCourtImprisoned() throws SQLException
  { return (String) _struct.getAttribute(116); }

  public void setCrownCourtImprisoned(String crownCourtImprisoned) throws SQLException
  { _struct.setAttribute(116, crownCourtImprisoned); }


  public java.math.BigDecimal getNumberOfCharges() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(117); }

  public void setNumberOfCharges(java.math.BigDecimal numberOfCharges) throws SQLException
  { _struct.setAttribute(117, numberOfCharges); }


  public String getXhibitFileName() throws SQLException
  { return (String) _struct.getAttribute(118); }

  public void setXhibitFileName(String xhibitFileName) throws SQLException
  { _struct.setAttribute(118, xhibitFileName); }


  public java.sql.Timestamp getXhibitFileTimestamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(119); }

  public void setXhibitFileTimestamp(java.sql.Timestamp xhibitFileTimestamp) throws SQLException
  { _struct.setAttribute(119, xhibitFileTimestamp); }


  public String getBenchWarrantIssued() throws SQLException
  { return (String) _struct.getAttribute(120); }

  public void setBenchWarrantIssued(String benchWarrantIssued) throws SQLException
  { _struct.setAttribute(120, benchWarrantIssued); }


  public String getRepOrderStatus() throws SQLException
  { return (String) _struct.getAttribute(121); }

  public void setRepOrderStatus(String repOrderStatus) throws SQLException
  { _struct.setAttribute(121, repOrderStatus); }


  public String getXhibitCcOutcome() throws SQLException
  { return (String) _struct.getAttribute(122); }

  public void setXhibitCcOutcome(String xhibitCcOutcome) throws SQLException
  { _struct.setAttribute(122, xhibitCcOutcome); }


  public java.sql.Timestamp getXhibitCcOutcomeDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(123); }

  public void setXhibitCcOutcomeDate(java.sql.Timestamp xhibitCcOutcomeDate) throws SQLException
  { _struct.setAttribute(123, xhibitCcOutcomeDate); }


  public String getCcRepOrderDecision() throws SQLException
  { return (String) _struct.getAttribute(124); }

  public void setCcRepOrderDecision(String ccRepOrderDecision) throws SQLException
  { _struct.setAttribute(124, ccRepOrderDecision); }


  public String getCcRepOrderWithdrawn() throws SQLException
  { return (String) _struct.getAttribute(125); }

  public void setCcRepOrderWithdrawn(String ccRepOrderWithdrawn) throws SQLException
  { _struct.setAttribute(125, ccRepOrderWithdrawn); }


  public java.sql.Timestamp getCcRoWithdrawalDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(126); }

  public void setCcRoWithdrawalDate(java.sql.Timestamp ccRoWithdrawalDate) throws SQLException
  { _struct.setAttribute(126, ccRoWithdrawalDate); }


  public oracle.sql.CLOB getChargeDetails() throws SQLException
  { return (oracle.sql.CLOB) _struct.getOracleAttribute(127); }

  public void setChargeDetails(oracle.sql.CLOB chargeDetails) throws SQLException
  { _struct.setOracleAttribute(127, chargeDetails); }


  public java.math.BigDecimal getWorkTypeOrder() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(128); }

  public void setWorkTypeOrder(java.math.BigDecimal workTypeOrder) throws SQLException
  { _struct.setAttribute(128, workTypeOrder); }


  public String getBillScenario() throws SQLException
  { return (String) _struct.getAttribute(129); }

  public void setBillScenario(String billScenario) throws SQLException
  { _struct.setAttribute(129, billScenario); }


  public java.sql.Timestamp getDateBillPaid() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(130); }

  public void setDateBillPaid(java.sql.Timestamp dateBillPaid) throws SQLException
  { _struct.setAttribute(130, dateBillPaid); }


  public String getAltMaatRefs() throws SQLException
  { return (String) _struct.getAttribute(131); }

  public void setAltMaatRefs(String altMaatRefs) throws SQLException
  { _struct.setAttribute(131, altMaatRefs); }


  public java.math.BigDecimal getTotalAsns() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(132); }

  public void setTotalAsns(java.math.BigDecimal totalAsns) throws SQLException
  { _struct.setAttribute(132, totalAsns); }


  public java.math.BigDecimal getDummyAsns() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(133); }

  public void setDummyAsns(java.math.BigDecimal dummyAsns) throws SQLException
  { _struct.setAttribute(133, dummyAsns); }


  public String getContrNoticeNeedsPrinting() throws SQLException
  { return (String) _struct.getAttribute(134); }

  public void setContrNoticeNeedsPrinting(String contrNoticeNeedsPrinting) throws SQLException
  { _struct.setAttribute(134, contrNoticeNeedsPrinting); }


  public String getContrOrderNeedsPrinting() throws SQLException
  { return (String) _struct.getAttribute(135); }

  public void setContrOrderNeedsPrinting(String contrOrderNeedsPrinting) throws SQLException
  { _struct.setAttribute(135, contrOrderNeedsPrinting); }


  public String getIncEvidReqNeedsGenerating() throws SQLException
  { return (String) _struct.getAttribute(136); }

  public void setIncEvidReqNeedsGenerating(String incEvidReqNeedsGenerating) throws SQLException
  { _struct.setAttribute(136, incEvidReqNeedsGenerating); }


  public String getIncEvidRemNeedsPrinting() throws SQLException
  { return (String) _struct.getAttribute(137); }

  public void setIncEvidRemNeedsPrinting(String incEvidRemNeedsPrinting) throws SQLException
  { _struct.setAttribute(137, incEvidRemNeedsPrinting); }


  public String getCapEvidReqNeedsGenerating() throws SQLException
  { return (String) _struct.getAttribute(138); }

  public void setCapEvidReqNeedsGenerating(String capEvidReqNeedsGenerating) throws SQLException
  { _struct.setAttribute(138, capEvidReqNeedsGenerating); }


  public String getCapEvidReqNeedsPrinting() throws SQLException
  { return (String) _struct.getAttribute(139); }

  public void setCapEvidReqNeedsPrinting(String capEvidReqNeedsPrinting) throws SQLException
  { _struct.setAttribute(139, capEvidReqNeedsPrinting); }


  public java.math.BigDecimal getVolTotalTrials() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(140); }

  public void setVolTotalTrials(java.math.BigDecimal volTotalTrials) throws SQLException
  { _struct.setAttribute(140, volTotalTrials); }


  public java.math.BigDecimal getVolContrNoticesNeedPrint() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(141); }

  public void setVolContrNoticesNeedPrint(java.math.BigDecimal volContrNoticesNeedPrint) throws SQLException
  { _struct.setAttribute(141, volContrNoticesNeedPrint); }


  public java.math.BigDecimal getVolContrOrdersNeedPrint() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(142); }

  public void setVolContrOrdersNeedPrint(java.math.BigDecimal volContrOrdersNeedPrint) throws SQLException
  { _struct.setAttribute(142, volContrOrdersNeedPrint); }


  public java.math.BigDecimal getVolIncEvidReqsNeedPrint() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(143); }

  public void setVolIncEvidReqsNeedPrint(java.math.BigDecimal volIncEvidReqsNeedPrint) throws SQLException
  { _struct.setAttribute(143, volIncEvidReqsNeedPrint); }


  public java.math.BigDecimal getVolIncEvidRemsNeedPrint() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(144); }

  public void setVolIncEvidRemsNeedPrint(java.math.BigDecimal volIncEvidRemsNeedPrint) throws SQLException
  { _struct.setAttribute(144, volIncEvidRemsNeedPrint); }


  public java.math.BigDecimal getVolCapEvidReqsNeedPrint() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(145); }

  public void setVolCapEvidReqsNeedPrint(java.math.BigDecimal volCapEvidReqsNeedPrint) throws SQLException
  { _struct.setAttribute(145, volCapEvidReqsNeedPrint); }


  public java.math.BigDecimal getVolIncEvidReqsNeedGen() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(146); }

  public void setVolIncEvidReqsNeedGen(java.math.BigDecimal volIncEvidReqsNeedGen) throws SQLException
  { _struct.setAttribute(146, volIncEvidReqsNeedGen); }


  public java.math.BigDecimal getVolCapEvidReqsNeedGen() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(147); }

  public void setVolCapEvidReqsNeedGen(java.math.BigDecimal volCapEvidReqsNeedGen) throws SQLException
  { _struct.setAttribute(147, volCapEvidReqsNeedGen); }

}
