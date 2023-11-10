package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class PassportAssessmentType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.PASSPORT_ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,2002,91,12,12,12,12,91,12,12,12,2002,2002,12,93,12,12,12,12,91,12,12,12,12,2002,2,12,2002 };
  protected static ORADataFactory[] _factory = new ORADataFactory[29];
  static
  {
    _factory[2] = AssStatusType.getORADataFactory();
    _factory[12] = NewWorkReasonType.getORADataFactory();
    _factory[13] = PassportConfirmationType.getORADataFactory();
    _factory[25] = IncomeEvidenceSummaryType.getORADataFactory();
    _factory[28] = ReviewTypeType.getORADataFactory();
  }
  protected static final PassportAssessmentType _PassportAssessmentTypeFactory = new PassportAssessmentType();

  public static ORADataFactory getORADataFactory()
  { return _PassportAssessmentTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[29], _sqlType, _factory); }
  public PassportAssessmentType()
  { _init_struct(true); }
  public PassportAssessmentType(java.math.BigDecimal id, java.math.BigDecimal cmuId, AssStatusType statusObject, java.sql.Timestamp assDate, String partnerBenefitClaimed, String partnerFirstName, String partnerSurname, String partnerNiNo, java.sql.Timestamp partnerDob, String incomeSupport, String jobSeekers, String statePensionCredit, NewWorkReasonType newWorkReasonObject, PassportConfirmationType confirmationObject, String result, java.sql.Timestamp timeStamp, String dwpResult, String passportNote, String under18HeardYouthCourt, String under18HeardMagsCourt, java.sql.Timestamp lastSignOnDate, String esa, String under18FullEducation, String under16, String between1617, IncomeEvidenceSummaryType incomeEvidenceSummaryObject, java.math.BigDecimal usn, String whoDwpChecked, ReviewTypeType reviewTypeObject) throws SQLException
  { _init_struct(true);
    setId(id);
    setCmuId(cmuId);
    setStatusObject(statusObject);
    setAssDate(assDate);
    setPartnerBenefitClaimed(partnerBenefitClaimed);
    setPartnerFirstName(partnerFirstName);
    setPartnerSurname(partnerSurname);
    setPartnerNiNo(partnerNiNo);
    setPartnerDob(partnerDob);
    setIncomeSupport(incomeSupport);
    setJobSeekers(jobSeekers);
    setStatePensionCredit(statePensionCredit);
    setNewWorkReasonObject(newWorkReasonObject);
    setConfirmationObject(confirmationObject);
    setResult(result);
    setTimeStamp(timeStamp);
    setDwpResult(dwpResult);
    setPassportNote(passportNote);
    setUnder18HeardYouthCourt(under18HeardYouthCourt);
    setUnder18HeardMagsCourt(under18HeardMagsCourt);
    setLastSignOnDate(lastSignOnDate);
    setEsa(esa);
    setUnder18FullEducation(under18FullEducation);
    setUnder16(under16);
    setBetween1617(between1617);
    setIncomeEvidenceSummaryObject(incomeEvidenceSummaryObject);
    setUsn(usn);
    setWhoDwpChecked(whoDwpChecked);
    setReviewTypeObject(reviewTypeObject);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(PassportAssessmentType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new PassportAssessmentType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getCmuId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setCmuId(java.math.BigDecimal cmuId) throws SQLException
  { _struct.setAttribute(1, cmuId); }


  public AssStatusType getStatusObject() throws SQLException
  { return (AssStatusType) _struct.getAttribute(2); }

  public void setStatusObject(AssStatusType statusObject) throws SQLException
  { _struct.setAttribute(2, statusObject); }


  public java.sql.Timestamp getAssDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setAssDate(java.sql.Timestamp assDate) throws SQLException
  { _struct.setAttribute(3, assDate); }


  public String getPartnerBenefitClaimed() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setPartnerBenefitClaimed(String partnerBenefitClaimed) throws SQLException
  { _struct.setAttribute(4, partnerBenefitClaimed); }


  public String getPartnerFirstName() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setPartnerFirstName(String partnerFirstName) throws SQLException
  { _struct.setAttribute(5, partnerFirstName); }


  public String getPartnerSurname() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setPartnerSurname(String partnerSurname) throws SQLException
  { _struct.setAttribute(6, partnerSurname); }


  public String getPartnerNiNo() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setPartnerNiNo(String partnerNiNo) throws SQLException
  { _struct.setAttribute(7, partnerNiNo); }


  public java.sql.Timestamp getPartnerDob() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(8); }

  public void setPartnerDob(java.sql.Timestamp partnerDob) throws SQLException
  { _struct.setAttribute(8, partnerDob); }


  public String getIncomeSupport() throws SQLException
  { return (String) _struct.getAttribute(9); }

  public void setIncomeSupport(String incomeSupport) throws SQLException
  { _struct.setAttribute(9, incomeSupport); }


  public String getJobSeekers() throws SQLException
  { return (String) _struct.getAttribute(10); }

  public void setJobSeekers(String jobSeekers) throws SQLException
  { _struct.setAttribute(10, jobSeekers); }


  public String getStatePensionCredit() throws SQLException
  { return (String) _struct.getAttribute(11); }

  public void setStatePensionCredit(String statePensionCredit) throws SQLException
  { _struct.setAttribute(11, statePensionCredit); }


  public NewWorkReasonType getNewWorkReasonObject() throws SQLException
  { return (NewWorkReasonType) _struct.getAttribute(12); }

  public void setNewWorkReasonObject(NewWorkReasonType newWorkReasonObject) throws SQLException
  { _struct.setAttribute(12, newWorkReasonObject); }


  public PassportConfirmationType getConfirmationObject() throws SQLException
  { return (PassportConfirmationType) _struct.getAttribute(13); }

  public void setConfirmationObject(PassportConfirmationType confirmationObject) throws SQLException
  { _struct.setAttribute(13, confirmationObject); }


  public String getResult() throws SQLException
  { return (String) _struct.getAttribute(14); }

  public void setResult(String result) throws SQLException
  { _struct.setAttribute(14, result); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(15); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(15, timeStamp); }


  public String getDwpResult() throws SQLException
  { return (String) _struct.getAttribute(16); }

  public void setDwpResult(String dwpResult) throws SQLException
  { _struct.setAttribute(16, dwpResult); }


  public String getPassportNote() throws SQLException
  { return (String) _struct.getAttribute(17); }

  public void setPassportNote(String passportNote) throws SQLException
  { _struct.setAttribute(17, passportNote); }


  public String getUnder18HeardYouthCourt() throws SQLException
  { return (String) _struct.getAttribute(18); }

  public void setUnder18HeardYouthCourt(String under18HeardYouthCourt) throws SQLException
  { _struct.setAttribute(18, under18HeardYouthCourt); }


  public String getUnder18HeardMagsCourt() throws SQLException
  { return (String) _struct.getAttribute(19); }

  public void setUnder18HeardMagsCourt(String under18HeardMagsCourt) throws SQLException
  { _struct.setAttribute(19, under18HeardMagsCourt); }


  public java.sql.Timestamp getLastSignOnDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(20); }

  public void setLastSignOnDate(java.sql.Timestamp lastSignOnDate) throws SQLException
  { _struct.setAttribute(20, lastSignOnDate); }


  public String getEsa() throws SQLException
  { return (String) _struct.getAttribute(21); }

  public void setEsa(String esa) throws SQLException
  { _struct.setAttribute(21, esa); }


  public String getUnder18FullEducation() throws SQLException
  { return (String) _struct.getAttribute(22); }

  public void setUnder18FullEducation(String under18FullEducation) throws SQLException
  { _struct.setAttribute(22, under18FullEducation); }


  public String getUnder16() throws SQLException
  { return (String) _struct.getAttribute(23); }

  public void setUnder16(String under16) throws SQLException
  { _struct.setAttribute(23, under16); }


  public String getBetween1617() throws SQLException
  { return (String) _struct.getAttribute(24); }

  public void setBetween1617(String between1617) throws SQLException
  { _struct.setAttribute(24, between1617); }


  public IncomeEvidenceSummaryType getIncomeEvidenceSummaryObject() throws SQLException
  { return (IncomeEvidenceSummaryType) _struct.getAttribute(25); }

  public void setIncomeEvidenceSummaryObject(IncomeEvidenceSummaryType incomeEvidenceSummaryObject) throws SQLException
  { _struct.setAttribute(25, incomeEvidenceSummaryObject); }


  public java.math.BigDecimal getUsn() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(26); }

  public void setUsn(java.math.BigDecimal usn) throws SQLException
  { _struct.setAttribute(26, usn); }


  public String getWhoDwpChecked() throws SQLException
  { return (String) _struct.getAttribute(27); }

  public void setWhoDwpChecked(String whoDwpChecked) throws SQLException
  { _struct.setAttribute(27, whoDwpChecked); }


  public ReviewTypeType getReviewTypeObject() throws SQLException
  { return (ReviewTypeType) _struct.getAttribute(28); }

  public void setReviewTypeObject(ReviewTypeType reviewTypeObject) throws SQLException
  { _struct.setAttribute(28, reviewTypeObject); }

}
