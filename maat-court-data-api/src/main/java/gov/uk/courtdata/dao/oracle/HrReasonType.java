package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class HrReasonType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[2];
  protected static final HrReasonType _HrReasonTypeFactory = new HrReasonType();

  public static ORADataFactory getORADataFactory()
  { return _HrReasonTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[2], _sqlType, _factory); }
  public HrReasonType()
  { _init_struct(true); }
  public HrReasonType(java.math.BigDecimal id, String reason) throws SQLException
  { _init_struct(true);
    setId(id);
    setReason(reason);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(HrReasonType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new HrReasonType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public String getReason() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setReason(String reason) throws SQLException
  { _struct.setAttribute(1, reason); }

}
