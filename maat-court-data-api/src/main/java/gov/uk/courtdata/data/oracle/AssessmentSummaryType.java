package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentSummaryType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASSESSMENT_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,91,12,12,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[6];
  protected static final AssessmentSummaryType _AssessmentSummaryTypeFactory = new AssessmentSummaryType();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentSummaryTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[6], _sqlType, _factory); }
  public AssessmentSummaryType()
  { _init_struct(true); }
  public AssessmentSummaryType(java.math.BigDecimal id, java.sql.Timestamp assessmentDate, String type, String status, String result, String reviewType) throws SQLException
  { _init_struct(true);
    setId(id);
    setAssessmentDate(assessmentDate);
    setType(type);
    setStatus(status);
    setResult(result);
    setReviewType(reviewType);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AssessmentSummaryType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AssessmentSummaryType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.sql.Timestamp getAssessmentDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(1); }

  public void setAssessmentDate(java.sql.Timestamp assessmentDate) throws SQLException
  { _struct.setAttribute(1, assessmentDate); }


  public String getType() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setType(String type) throws SQLException
  { _struct.setAttribute(2, type); }


  public String getStatus() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setStatus(String status) throws SQLException
  { _struct.setAttribute(3, status); }


  public String getResult() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setResult(String result) throws SQLException
  { _struct.setAttribute(4, result); }


  public String getReviewType() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setReviewType(String reviewType) throws SQLException
  { _struct.setAttribute(5, reviewType); }

}
