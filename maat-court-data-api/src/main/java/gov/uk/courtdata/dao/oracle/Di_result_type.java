package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class Di_result_type implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DI_RESULT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,2,91,12,12,12,12,12,91,12,12,12,91,12,12,12,12,12,12,12,2,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[24];
  protected static final Di_result_type _Di_result_typeFactory = new Di_result_type();

  public static ORADataFactory getORADataFactory()
  { return _Di_result_typeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[24], _sqlType, _factory); }
  public Di_result_type()
  { _init_struct(true); }
  public Di_result_type(java.math.BigDecimal id, java.math.BigDecimal usn, java.math.BigDecimal repId, java.sql.Timestamp dateAppCreated, String caseId, String iojResult, String iojAssessorName, String meansResult, String meansAssessorName, java.sql.Timestamp dateMeansCreated, String iojReason, String passportResult, String passportAssessorName, java.sql.Timestamp datePassportCreated, String dwpResult, String iojAppealResult, String hardshipResult, String repOrderDecision, String ccRepOrdDec, String fundingDecision, String caseType, java.math.BigDecimal assessmentId, String assessmentType, String magsOutcome) throws SQLException
  { _init_struct(true);
    setId(id);
    setUsn(usn);
    setRepId(repId);
    setDateAppCreated(dateAppCreated);
    setCaseId(caseId);
    setIojResult(iojResult);
    setIojAssessorName(iojAssessorName);
    setMeansResult(meansResult);
    setMeansAssessorName(meansAssessorName);
    setDateMeansCreated(dateMeansCreated);
    setIojReason(iojReason);
    setPassportResult(passportResult);
    setPassportAssessorName(passportAssessorName);
    setDatePassportCreated(datePassportCreated);
    setDwpResult(dwpResult);
    setIojAppealResult(iojAppealResult);
    setHardshipResult(hardshipResult);
    setRepOrderDecision(repOrderDecision);
    setCcRepOrdDec(ccRepOrdDec);
    setFundingDecision(fundingDecision);
    setCaseType(caseType);
    setAssessmentId(assessmentId);
    setAssessmentType(assessmentType);
    setMagsOutcome(magsOutcome);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(Di_result_type o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new Di_result_type();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getUsn() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setUsn(java.math.BigDecimal usn) throws SQLException
  { _struct.setAttribute(1, usn); }


  public java.math.BigDecimal getRepId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setRepId(java.math.BigDecimal repId) throws SQLException
  { _struct.setAttribute(2, repId); }


  public java.sql.Timestamp getDateAppCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateAppCreated(java.sql.Timestamp dateAppCreated) throws SQLException
  { _struct.setAttribute(3, dateAppCreated); }


  public String getCaseId() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setCaseId(String caseId) throws SQLException
  { _struct.setAttribute(4, caseId); }


  public String getIojResult() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setIojResult(String iojResult) throws SQLException
  { _struct.setAttribute(5, iojResult); }


  public String getIojAssessorName() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setIojAssessorName(String iojAssessorName) throws SQLException
  { _struct.setAttribute(6, iojAssessorName); }


  public String getMeansResult() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setMeansResult(String meansResult) throws SQLException
  { _struct.setAttribute(7, meansResult); }


  public String getMeansAssessorName() throws SQLException
  { return (String) _struct.getAttribute(8); }

  public void setMeansAssessorName(String meansAssessorName) throws SQLException
  { _struct.setAttribute(8, meansAssessorName); }


  public java.sql.Timestamp getDateMeansCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(9); }

  public void setDateMeansCreated(java.sql.Timestamp dateMeansCreated) throws SQLException
  { _struct.setAttribute(9, dateMeansCreated); }


  public String getIojReason() throws SQLException
  { return (String) _struct.getAttribute(10); }

  public void setIojReason(String iojReason) throws SQLException
  { _struct.setAttribute(10, iojReason); }


  public String getPassportResult() throws SQLException
  { return (String) _struct.getAttribute(11); }

  public void setPassportResult(String passportResult) throws SQLException
  { _struct.setAttribute(11, passportResult); }


  public String getPassportAssessorName() throws SQLException
  { return (String) _struct.getAttribute(12); }

  public void setPassportAssessorName(String passportAssessorName) throws SQLException
  { _struct.setAttribute(12, passportAssessorName); }


  public java.sql.Timestamp getDatePassportCreated() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(13); }

  public void setDatePassportCreated(java.sql.Timestamp datePassportCreated) throws SQLException
  { _struct.setAttribute(13, datePassportCreated); }


  public String getDwpResult() throws SQLException
  { return (String) _struct.getAttribute(14); }

  public void setDwpResult(String dwpResult) throws SQLException
  { _struct.setAttribute(14, dwpResult); }


  public String getIojAppealResult() throws SQLException
  { return (String) _struct.getAttribute(15); }

  public void setIojAppealResult(String iojAppealResult) throws SQLException
  { _struct.setAttribute(15, iojAppealResult); }


  public String getHardshipResult() throws SQLException
  { return (String) _struct.getAttribute(16); }

  public void setHardshipResult(String hardshipResult) throws SQLException
  { _struct.setAttribute(16, hardshipResult); }


  public String getRepOrderDecision() throws SQLException
  { return (String) _struct.getAttribute(17); }

  public void setRepOrderDecision(String repOrderDecision) throws SQLException
  { _struct.setAttribute(17, repOrderDecision); }


  public String getCcRepOrdDec() throws SQLException
  { return (String) _struct.getAttribute(18); }

  public void setCcRepOrdDec(String ccRepOrdDec) throws SQLException
  { _struct.setAttribute(18, ccRepOrdDec); }


  public String getFundingDecision() throws SQLException
  { return (String) _struct.getAttribute(19); }

  public void setFundingDecision(String fundingDecision) throws SQLException
  { _struct.setAttribute(19, fundingDecision); }


  public String getCaseType() throws SQLException
  { return (String) _struct.getAttribute(20); }

  public void setCaseType(String caseType) throws SQLException
  { _struct.setAttribute(20, caseType); }


  public java.math.BigDecimal getAssessmentId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(21); }

  public void setAssessmentId(java.math.BigDecimal assessmentId) throws SQLException
  { _struct.setAttribute(21, assessmentId); }


  public String getAssessmentType() throws SQLException
  { return (String) _struct.getAttribute(22); }

  public void setAssessmentType(String assessmentType) throws SQLException
  { _struct.setAttribute(22, assessmentType); }


  public String getMagsOutcome() throws SQLException
  { return (String) _struct.getAttribute(23); }

  public void setMagsOutcome(String magsOutcome) throws SQLException
  { _struct.setAttribute(23, magsOutcome); }

}
