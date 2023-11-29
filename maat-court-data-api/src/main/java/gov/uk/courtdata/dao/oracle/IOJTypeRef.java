package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class IOJTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.IOJ_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final IOJTypeRef _IOJTypeRefFactory = new IOJTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _IOJTypeRefFactory; }
  /* constructor */
  public IOJTypeRef()
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
    IOJTypeRef r = new IOJTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static IOJTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (IOJTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to IOJTypeRef: "+exn.toString()); }
  }

  public IOJType getValue() throws SQLException
  {
     return (IOJType) IOJType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(IOJType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
