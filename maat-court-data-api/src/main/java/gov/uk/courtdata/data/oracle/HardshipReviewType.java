package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class HardshipReviewType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HARDSHIP_REVIEWTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,2,12,91,12,12,2002,2,2,2003,2003,2002,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[14];
  static
  {
    _factory[1] = NewWorkReasonType.getORADataFactory();
    _factory[7] = HrSolicitorCostType.getORADataFactory();
    _factory[10] = HRSectionTabType.getORADataFactory();
    _factory[11] = HRProgressTabType.getORADataFactory();
    _factory[12] = AssStatusType.getORADataFactory();
  }
  protected static final HardshipReviewType _HardshipReviewTypeFactory = new HardshipReviewType();

  public static ORADataFactory getORADataFactory()
  { return _HardshipReviewTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[14], _sqlType, _factory); }
  public HardshipReviewType()
  { _init_struct(true); }
  public HardshipReviewType(java.math.BigDecimal id, NewWorkReasonType newWorkReasonObject, java.math.BigDecimal cmuId, String reviewResult, java.sql.Timestamp reviewDate, String notes, String decisionNotes, HrSolicitorCostType solicitorCostsObject, java.math.BigDecimal disposIncome, java.math.BigDecimal disposIncomeAfterHardship, HRSectionTabType sectionTab, HRProgressTabType progressTab, AssStatusType statusObject, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setNewWorkReasonObject(newWorkReasonObject);
    setCmuId(cmuId);
    setReviewResult(reviewResult);
    setReviewDate(reviewDate);
    setNotes(notes);
    setDecisionNotes(decisionNotes);
    setSolicitorCostsObject(solicitorCostsObject);
    setDisposIncome(disposIncome);
    setDisposIncomeAfterHardship(disposIncomeAfterHardship);
    setSectionTab(sectionTab);
    setProgressTab(progressTab);
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
  protected ORAData create(HardshipReviewType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new HardshipReviewType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public NewWorkReasonType getNewWorkReasonObject() throws SQLException
  { return (NewWorkReasonType) _struct.getAttribute(1); }

  public void setNewWorkReasonObject(NewWorkReasonType newWorkReasonObject) throws SQLException
  { _struct.setAttribute(1, newWorkReasonObject); }


  public java.math.BigDecimal getCmuId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setCmuId(java.math.BigDecimal cmuId) throws SQLException
  { _struct.setAttribute(2, cmuId); }


  public String getReviewResult() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setReviewResult(String reviewResult) throws SQLException
  { _struct.setAttribute(3, reviewResult); }


  public java.sql.Timestamp getReviewDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setReviewDate(java.sql.Timestamp reviewDate) throws SQLException
  { _struct.setAttribute(4, reviewDate); }


  public String getNotes() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setNotes(String notes) throws SQLException
  { _struct.setAttribute(5, notes); }


  public String getDecisionNotes() throws SQLException
  { return (String) _struct.getAttribute(6); }

  public void setDecisionNotes(String decisionNotes) throws SQLException
  { _struct.setAttribute(6, decisionNotes); }


  public HrSolicitorCostType getSolicitorCostsObject() throws SQLException
  { return (HrSolicitorCostType) _struct.getAttribute(7); }

  public void setSolicitorCostsObject(HrSolicitorCostType solicitorCostsObject) throws SQLException
  { _struct.setAttribute(7, solicitorCostsObject); }


  public java.math.BigDecimal getDisposIncome() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(8); }

  public void setDisposIncome(java.math.BigDecimal disposIncome) throws SQLException
  { _struct.setAttribute(8, disposIncome); }


  public java.math.BigDecimal getDisposIncomeAfterHardship() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(9); }

  public void setDisposIncomeAfterHardship(java.math.BigDecimal disposIncomeAfterHardship) throws SQLException
  { _struct.setAttribute(9, disposIncomeAfterHardship); }


  public HRSectionTabType getSectionTab() throws SQLException
  { return (HRSectionTabType) _struct.getAttribute(10); }

  public void setSectionTab(HRSectionTabType sectionTab) throws SQLException
  { _struct.setAttribute(10, sectionTab); }


  public HRProgressTabType getProgressTab() throws SQLException
  { return (HRProgressTabType) _struct.getAttribute(11); }

  public void setProgressTab(HRProgressTabType progressTab) throws SQLException
  { _struct.setAttribute(11, progressTab); }


  public AssStatusType getStatusObject() throws SQLException
  { return (AssStatusType) _struct.getAttribute(12); }

  public void setStatusObject(AssStatusType statusObject) throws SQLException
  { _struct.setAttribute(12, statusObject); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(13); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(13, timeStamp); }

}
