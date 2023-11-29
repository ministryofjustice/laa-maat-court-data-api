package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrProgressResponseTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_PROGRESS_RESPONSE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrProgressResponseTypeRef _HrProgressResponseTypeRefFactory = new HrProgressResponseTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrProgressResponseTypeRefFactory; }
  /* constructor */
  public HrProgressResponseTypeRef()
  {
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _ref;
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    HrProgressResponseTypeRef r = new HrProgressResponseTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrProgressResponseTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrProgressResponseTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrProgressResponseTypeRef: "+exn.toString()); }
  }

  public HrProgressResponseType getValue() throws SQLException
  {
     return (HrProgressResponseType) HrProgressResponseType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrProgressResponseType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
