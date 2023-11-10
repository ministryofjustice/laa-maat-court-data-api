package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class ReservationType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.RESERVATION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,12 };
  protected static ORADataFactory[] _factory = new ORADataFactory[2];
  protected static final ReservationType _ReservationTypeFactory = new ReservationType();

  public static ORADataFactory getORADataFactory()
  { return _ReservationTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[2], _sqlType, _factory); }
  public ReservationType()
  { _init_struct(true); }
  public ReservationType(java.math.BigDecimal recordId, String recordName) throws SQLException
  { _init_struct(true);
    setRecordId(recordId);
    setRecordName(recordName);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(ReservationType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new ReservationType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getRecordId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setRecordId(java.math.BigDecimal recordId) throws SQLException
  { _struct.setAttribute(0, recordId); }


  public String getRecordName() throws SQLException
  { return (String) _struct.getAttribute(1); }

  public void setRecordName(String recordName) throws SQLException
  { _struct.setAttribute(1, recordName); }

}
