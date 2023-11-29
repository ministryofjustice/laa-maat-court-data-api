package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Di_result_typeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.DI_RESULT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final Di_result_typeRef _Di_result_typeRefFactory = new Di_result_typeRef();

  public static ORADataFactory getORADataFactory()
  { return _Di_result_typeRefFactory; }
  /* constructor */
  public Di_result_typeRef()
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
    Di_result_typeRef r = new Di_result_typeRef();
    r._ref = (REF) d;
    return r;
  }

  public static Di_result_typeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (Di_result_typeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to Di_result_typeRef: "+exn.toString()); }
  }

  public Di_result_type getValue() throws SQLException
  {
     return (Di_result_type) Di_result_type.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(Di_result_type c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
