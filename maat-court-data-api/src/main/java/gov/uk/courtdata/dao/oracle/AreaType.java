package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class AreaType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.AREATYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,12,12,93,2003 };
  protected static ORADataFactory[] _factory = new ORADataFactory[6];
  static
  {
    _factory[5] = CMUTabType.getORADataFactory();
  }
  protected static final AreaType _AreaTypeFactory = new AreaType();

  public static ORADataFactory getORADataFactory()
  { return _AreaTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[6], _sqlType, _factory); }
  public AreaType()
  { _init_struct(true); }
  public AreaType(java.math.BigDecimal id, String code, String description, String enabled, java.sql.Timestamp timeStamp, CMUTabType cmuTab) throws SQLException
  { _init_struct(true);
    setId(id);
    setCode(code);
    setDescription(description);
    setEnabled(enabled);
    setTimeStamp(timeStamp);
    setCmuTab(cmuTab);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(AreaType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new AreaType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getCode() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setCode(String code) throws SQLException
  { _struct.setAttribute(1, code); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(2); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(2, description); }


  public String getEnabled() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setEnabled(String enabled) throws SQLException
  { _struct.setAttribute(3, enabled); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(4); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(4, timeStamp); }


  public CMUTabType getCmuTab() throws SQLException
  { return (CMUTabType) _struct.getAttribute(5); }

  public void setCmuTab(CMUTabType cmuTab) throws SQLException
  { _struct.setAttribute(5, cmuTab); }

}
