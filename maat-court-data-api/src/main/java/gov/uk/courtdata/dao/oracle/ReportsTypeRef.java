package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ReportsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.REPORTSTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ReportsTypeRef _ReportsTypeRefFactory = new ReportsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ReportsTypeRefFactory; }
  /* constructor */
  public ReportsTypeRef()
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
    ReportsTypeRef r = new ReportsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ReportsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ReportsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ReportsTypeRef: "+exn.toString()); }
  }

  public ReportsType getValue() throws SQLException
  {
     return (ReportsType) ReportsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ReportsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
