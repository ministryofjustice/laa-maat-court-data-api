package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class EmiSimpleReportType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.EMISIMPLEREPORTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 12,12,2,12,12,12,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[7];
  static
  {
    _factory[6] = Parametertabtype.getORADataFactory();
  }
  protected static final EmiSimpleReportType _EmiSimpleReportTypeFactory = new EmiSimpleReportType();

  public static ORADataFactory getORADataFactory()
  { return _EmiSimpleReportTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[7], _sqlType, _factory); }
  public EmiSimpleReportType()
  { _init_struct(true); }
  public EmiSimpleReportType(String title, String description, java.math.BigDecimal id, String userName, String reportDisplayNumber, String reportFrequency, Parametertabtype parameters) throws SQLException
  { _init_struct(true);
    setTitle(title);
    setDescription(description);
    setId(id);
    setUserName(userName);
    setReportDisplayNumber(reportDisplayNumber);
    setReportFrequency(reportFrequency);
    setParameters(parameters);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(EmiSimpleReportType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new EmiSimpleReportType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public String getTitle() throws SQLException
  { return (String) _struct.getAttribute(0); }

  public void setTitle(String title) throws SQLException
  { _struct.setAttribute(0, title); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(1, description); }


  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(2, id); }


  public String getUserName() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setUserName(String userName) throws SQLException
  { _struct.setAttribute(3, userName); }


  public String getReportDisplayNumber() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setReportDisplayNumber(String reportDisplayNumber) throws SQLException
  { _struct.setAttribute(4, reportDisplayNumber); }


  public String getReportFrequency() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setReportFrequency(String reportFrequency) throws SQLException
  { _struct.setAttribute(5, reportFrequency); }


  public Parametertabtype getParameters() throws SQLException
  { return (Parametertabtype) _struct.getAttribute(6); }

  public void setParameters(Parametertabtype parameters) throws SQLException
  { _struct.setAttribute(6, parameters); }

}
