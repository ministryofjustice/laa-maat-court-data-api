package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class InitialAssessmentType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.INITIAL_ASSESSMENTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 91,2,2002,12,12,2,2,12,2003,2003,2,2,12,12,2002,93,2002 };
  protected static ORADataFactory[] _factory = new ORADataFactory[17];
  static
  {
    _factory[2] = NewWorkReasonType.getORADataFactory();
    _factory[8] = AssSectionSummaryTabType.getORADataFactory();
    _factory[9] = AssChildWeightingTabType.getORADataFactory();
    _factory[14] = AssStatusType.getORADataFactory();
    _factory[16] = ReviewTypeType.getORADataFactory();
  }
  protected static final InitialAssessmentType _InitialAssessmentTypeFactory = new InitialAssessmentType();

  public static ORADataFactory getORADataFactory()
  { return _InitialAssessmentTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[17], _sqlType, _factory); }
  public InitialAssessmentType()
  { _init_struct(true); }
  public InitialAssessmentType(java.sql.Timestamp assessmentDate, java.math.BigDecimal criteriaId, NewWorkReasonType newWorkReasonObject, String otherBenefitNote, String otherIncomeNote, java.math.BigDecimal totAggregatedIncome, java.math.BigDecimal adjustedIncomeValue, String notes, AssSectionSummaryTabType sectionSummariesTab, AssChildWeightingTabType childWeightingTab, java.math.BigDecimal lowerThreshold, java.math.BigDecimal upperThreshold, String result, String resultReason, AssStatusType statusObject, java.sql.Timestamp timeStamp, ReviewTypeType reviewTypeObject) throws SQLException
  { _init_struct(true);
    setAssessmentDate(assessmentDate);
    setCriteriaId(criteriaId);
    setNewWorkReasonObject(newWorkReasonObject);
    setOtherBenefitNote(otherBenefitNote);
    setOtherIncomeNote(otherIncomeNote);
    setTotAggregatedIncome(totAggregatedIncome);
    setAdjustedIncomeValue(adjustedIncomeValue);
    setNotes(notes);
    setSectionSummariesTab(sectionSummariesTab);
    setChildWeightingTab(childWeightingTab);
    setLowerThreshold(lowerThreshold);
    setUpperThreshold(upperThreshold);
    setResult(result);
    setResultReason(resultReason);
    setStatusObject(statusObject);
    setTimeStamp(timeStamp);
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
  protected ORAData create(InitialAssessmentType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new InitialAssessmentType();
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


  public NewWorkReasonType getNewWorkReasonObject() throws SQLException
  { return (NewWorkReasonType) _struct.getAttribute(2); }

  public void setNewWorkReasonObject(NewWorkReasonType newWorkReasonObject) throws SQLException
  { _struct.setAttribute(2, newWorkReasonObject); }


  public String getOtherBenefitNote() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setOtherBenefitNote(String otherBenefitNote) throws SQLException
  { _struct.setAttribute(3, otherBenefitNote); }


  public String getOtherIncomeNote() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setOtherIncomeNote(String otherIncomeNote) throws SQLException
  { _struct.setAttribute(4, otherIncomeNote); }


  public java.math.BigDecimal getTotAggregatedIncome() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(5); }

  public void setTotAggregatedIncome(java.math.BigDecimal totAggregatedIncome) throws SQLException
  { _struct.setAttribute(5, totAggregatedIncome); }


  public java.math.BigDecimal getAdjustedIncomeValue() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(6); }

  public void setAdjustedIncomeValue(java.math.BigDecimal adjustedIncomeValue) throws SQLException
  { _struct.setAttribute(6, adjustedIncomeValue); }


  public String getNotes() throws SQLException
  { return (String) _struct.getAttribute(7); }

  public void setNotes(String notes) throws SQLException
  { _struct.setAttribute(7, notes); }


  public AssSectionSummaryTabType getSectionSummariesTab() throws SQLException
  { return (AssSectionSummaryTabType) _struct.getAttribute(8); }

  public void setSectionSummariesTab(AssSectionSummaryTabType sectionSummariesTab) throws SQLException
  { _struct.setAttribute(8, sectionSummariesTab); }


  public AssChildWeightingTabType getChildWeightingTab() throws SQLException
  { return (AssChildWeightingTabType) _struct.getAttribute(9); }

  public void setChildWeightingTab(AssChildWeightingTabType childWeightingTab) throws SQLException
  { _struct.setAttribute(9, childWeightingTab); }


  public java.math.BigDecimal getLowerThreshold() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(10); }

  public void setLowerThreshold(java.math.BigDecimal lowerThreshold) throws SQLException
  { _struct.setAttribute(10, lowerThreshold); }


  public java.math.BigDecimal getUpperThreshold() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(11); }

  public void setUpperThreshold(java.math.BigDecimal upperThreshold) throws SQLException
  { _struct.setAttribute(11, upperThreshold); }


  public String getResult() throws SQLException
  { return (String) _struct.getAttribute(12); }

  public void setResult(String result) throws SQLException
  { _struct.setAttribute(12, result); }


  public String getResultReason() throws SQLException
  { return (String) _struct.getAttribute(13); }

  public void setResultReason(String resultReason) throws SQLException
  { _struct.setAttribute(13, resultReason); }


  public AssStatusType getStatusObject() throws SQLException
  { return (AssStatusType) _struct.getAttribute(14); }

  public void setStatusObject(AssStatusType statusObject) throws SQLException
  { _struct.setAttribute(14, statusObject); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(15); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(15, timeStamp); }


  public ReviewTypeType getReviewTypeObject() throws SQLException
  { return (ReviewTypeType) _struct.getAttribute(16); }

  public void setReviewTypeObject(ReviewTypeType reviewTypeObject) throws SQLException
  { _struct.setAttribute(16, reviewTypeObject); }

}
