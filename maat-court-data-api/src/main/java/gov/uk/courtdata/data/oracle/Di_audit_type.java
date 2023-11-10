package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class Di_audit_type implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DI_AUDIT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,2,91,12,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[6];
  protected static final Di_audit_type _Di_audit_typeFactory = new Di_audit_type();

  public static ORADataFactory getORADataFactory()
  { return _Di_audit_typeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[6], _sqlType, _factory); }
  public Di_audit_type()
  { _init_struct(true); }
  public Di_audit_type(java.math.BigDecimal id, java.math.BigDecimal usn, java.math.BigDecimal repId, java.sql.Timestamp dateActioned, String userActioned, String statusCode) throws SQLException
  { _init_struct(true);
    setId(id);
    setUsn(usn);
    setRepId(repId);
    setDateActioned(dateActioned);
    setUserActioned(userActioned);
    setStatusCode(statusCode);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(Di_audit_type o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new Di_audit_type();
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


  public java.sql.Timestamp getDateActioned() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(3); }

  public void setDateActioned(java.sql.Timestamp dateActioned) throws SQLException
  { _struct.setAttribute(3, dateActioned); }


  public String getUserActioned() throws SQLException
  { return (String) _struct.getAttribute(4); }

  public void setUserActioned(String userActioned) throws SQLException
  { _struct.setAttribute(4, userActioned); }


  public String getStatusCode() throws SQLException
  { return (String) _struct.getAttribute(5); }

  public void setStatusCode(String statusCode) throws SQLException
  { _struct.setAttribute(5, statusCode); }

}
