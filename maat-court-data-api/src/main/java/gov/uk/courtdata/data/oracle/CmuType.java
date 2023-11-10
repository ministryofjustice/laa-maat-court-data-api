package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class CmuType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CMUTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12,2,12,12,12,93 };
  protected static ORADataFactory[] _factory = new ORADataFactory[7];
  protected static final CmuType _CmuTypeFactory = new CmuType();

  public static ORADataFactory getORADataFactory()
  { return _CmuTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[7], _sqlType, _factory); }
  public CmuType()
  { _init_struct(true); }
  public CmuType(java.math.BigDecimal id, String code, java.math.BigDecimal areaId, String description, String enabled, String hasLibra, java.sql.Timestamp timeStamp) throws SQLException
  { _init_struct(true);
    setId(id);
    setCode(code);
    setAreaId(areaId);
    setDescription(description);
    setEnabled(enabled);
    setHasLibra(hasLibra);
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
  protected ORAData create(CmuType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new CmuType();
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


  public java.math.BigDecimal getAreaId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(2); }

  public void setAreaId(java.math.BigDecimal areaId) throws SQLException
  { _struct.setAttribute(2, areaId); }


  public String getDescription() throws SQLException
  { return (String) _struct.getAttribute(3); }

  public void setDescription(String description) throws SQLException
  { _struct.setAttribute(3, description); }


  public String getEnabled() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setEnabled(String enabled) throws SQLException
  { _struct.setAttribute(4, enabled); }


  public String getHasLibra() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setHasLibra(String hasLibra) throws SQLException
  { _struct.setAttribute(5, hasLibra); }


  public java.sql.Timestamp getTimeStamp() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(6); }

  public void setTimeStamp(java.sql.Timestamp timeStamp) throws SQLException
  { _struct.setAttribute(6, timeStamp); }

}
