package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class FinAssessmentType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FIN_ASSESSMENT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2002,2002,2002,2002,12,93,2 };
  protected static ORADataFactory[] _factory = new ORADataFactory[8];
  static
  {
    _factory[1] = InitialAssessmentType.getORADataFactory();
    _factory[2] = FullAssessmentType.getORADataFactory();
    _factory[3] = IncomeEvidenceSummaryType.getORADataFactory();
    _factory[4] = HardshipOverviewType.getORADataFactory();
  }
  protected static final FinAssessmentType _FinAssessmentTypeFactory = new FinAssessmentType();

  public static ORADataFactory getORADataFactory()
  { return _FinAssessmentTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[8], _sqlType, _factory); }
  public FinAssessmentType()
  { _init_struct(true); }
  public FinAssessmentType(java.math.BigDecimal finAssessmentId, InitialAssessmentType initialAssessmentObject, FullAssessmentType fullAssessmentObject, IncomeEvidenceSummaryType incomeEvidenceSummaryObject, HardshipOverviewType hardshipOverviewObject, String fullAvailable, java.sql.Timestamp timeStamp, java.math.BigDecimal usn) throws SQLException
  { _init_struct(true);
    setFinAssessmentId(finAssessmentId);
    setInitialAssessmentObject(initialAssessmentObject);
    setFullAssessmentObject(fullAssessmentObject);
    setIncomeEvidenceSummaryObject(incomeEvidenceSummaryObject);
    setHardshipOverviewObject(hardshipOverviewObject);
    setFullAvailable(fullAvailable);
    setTimeStamp(timeStamp);
    setUsn(usn);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(FinAssessmentType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new FinAssessmentType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getFinAssessmentId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setFinAssessmentId(java.math.BigDecimal finAssessmentId) throws SQLException
  { _struct.setAttribute(0, finAssessmentId); }


  public InitialAssessmentType getInitialAssessmentObject() throws SQLException
  { return (InitialAssessmentType) _struct.getAttribute(1); }

  public void setInitialAssessmentObject(InitialAssessmentType initialAssessmentObject) throws SQLException
  { _struct.setAttribute(1, initialAssessmentObject); }


  public FullAssessmentType getFullAssessmentObject() throws SQLException
  { return (FullAssessmentType) _struct.getAttribute(2); }

  public void setFullAssessmentObject(FullAssessmentType fullAssessmentObject) throws SQLException
  { _struct.setAttribute(2, fullAssessmentObject); }


  public IncomeEvidenceSummaryType getIncomeEvidenceSummaryObject() throws SQLException
  { return (IncomeEvidenceSummaryType) _struct.getAttribute(3); }

  public void setIncomeEvidenceSummaryObject(IncomeEvidenceSummaryType incomeEvidenceSummaryObject) throws SQLException
  { _struct.setAttribute(3, incomeEvidenceSummaryObject); }


  public HardshipOverviewType getHardshipOverviewObject() throws SQLException
  { return (HardshipOverviewType) _struct.getAttribute(4); }

  public void setHardshipOverviewObject(HardshipOverviewType hardshipOverviewObject) throws SQLException
  { _struct.setAttribute(4, hardshipOverviewObject); }


  public String getFullAvailable() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setFullAvailable(String fullAvailable) throws SQLException
  { _struct.setAttribute(5, fullAvailable); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(6, timeStamp); }


  public java.math.BigDecimal getUsn() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(7); }

  public void setUsn(java.math.BigDecimal usn) throws SQLException
  { _struct.setAttribute(7, usn); }

}
