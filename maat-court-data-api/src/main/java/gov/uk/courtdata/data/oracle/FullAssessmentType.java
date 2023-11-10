package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class FullAssessmentType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FULL_ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 91,2,12,2003,2,12,2,2,2,12,12,2002,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[13];
  static
  {
    _factory[3] = AssSectionSummaryTabType.getORADataFactory();
    _factory[11] = AssStatusType.getORADataFactory();
  }
  protected static final FullAssessmentType _FullAssessmentTypeFactory = new FullAssessmentType();

  public static ORADataFactory getORADataFactory()
  { return _FullAssessmentTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[13], _sqlType, _factory); }
  public FullAssessmentType()
  { _init_struct(true); }
  public FullAssessmentType(java.sql.Timestamp assessmentDate, java.math.BigDecimal criteriaId, String assessmentNotes, AssSectionSummaryTabType sectionSummariesTab, java.math.BigDecimal adjustedLivingAllowance, String otherHousingNote, java.math.BigDecimal totAggregatedExp, java.math.BigDecimal totAnnualDisposableInc, java.math.BigDecimal threshold, String result, String resultReason, AssStatusType statusObject, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setAssessmentDate(assessmentDate);
    setCriteriaId(criteriaId);
    setAssessmentNotes(assessmentNotes);
    setSectionSummariesTab(sectionSummariesTab);
    setAdjustedLivingAllowance(adjustedLivingAllowance);
    setOtherHousingNote(otherHousingNote);
    setTotAggregatedExp(totAggregatedExp);
    setTotAnnualDisposableInc(totAnnualDisposableInc);
    setThreshold(threshold);
    setResult(result);
    setResultReason(resultReason);
    setStatusObject(statusObject);
    setTimeStamp(timeStamp);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(FullAssessmentType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new FullAssessmentType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.sql.Timestamp getAssessmentDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(0); }

  public void setAssessmentDate(java.sql.Timestamp assessmentDate) throws SQLException
  { _struct.setAttribute(0, assessmentDate); }


  public java.math.BigDecimal getCriteriaId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setCriteriaId(java.math.BigDecimal criteriaId) throws SQLException
  { _struct.setAttribute(1, criteriaId); }


  public String getAssessmentNotes() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setAssessmentNotes(String assessmentNotes) throws SQLException
  { _struct.setAttribute(2, assessmentNotes); }


  public AssSectionSummaryTabType getSectionSummariesTab() throws SQLException
  { return (AssSectionSummaryTabType) _struct.getAttribute(3); }

  public void setSectionSummariesTab(AssSectionSummaryTabType sectionSummariesTab) throws SQLException
  { _struct.setAttribute(3, sectionSummariesTab); }


  public java.math.BigDecimal getAdjustedLivingAllowance() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(4); }

  public void setAdjustedLivingAllowance(java.math.BigDecimal adjustedLivingAllowance) throws SQLException
  { _struct.setAttribute(4, adjustedLivingAllowance); }


  public String getOtherHousingNote() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setOtherHousingNote(String otherHousingNote) throws SQLException
  { _struct.setAttribute(5, otherHousingNote); }


  public java.math.BigDecimal getTotAggregatedExp() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(6); }

  public void setTotAggregatedExp(java.math.BigDecimal totAggregatedExp) throws SQLException
  { _struct.setAttribute(6, totAggregatedExp); }


  public java.math.BigDecimal getTotAnnualDisposableInc() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(7); }

  public void setTotAnnualDisposableInc(java.math.BigDecimal totAnnualDisposableInc) throws SQLException
  { _struct.setAttribute(7, totAnnualDisposableInc); }


  public java.math.BigDecimal getThreshold() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(8); }

  public void setThreshold(java.math.BigDecimal threshold) throws SQLException
  { _struct.setAttribute(8, threshold); }


  public String getResult() throws SQLException
  { return (String) _struct.getAttribute(9); }

  public void setResult(String result) throws SQLException
  { _struct.setAttribute(9, result); }


  public String getResultReason() throws SQLException
  { return (String) _struct.getAttribute(10); }

  public void setResultReason(String resultReason) throws SQLException
  { _struct.setAttribute(10, resultReason); }


  public AssStatusType getStatusObject() throws SQLException
  { return (AssStatusType) _struct.getAttribute(11); }

  public void setStatusObject(AssStatusType statusObject) throws SQLException
  { _struct.setAttribute(11, statusObject); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(12); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(12, timeStamp); }

}
