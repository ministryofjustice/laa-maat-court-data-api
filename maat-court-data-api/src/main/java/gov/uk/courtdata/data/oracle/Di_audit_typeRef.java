package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Di_audit_typeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.DI_AUDIT_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final Di_audit_typeRef _Di_audit_typeRefFactory = new Di_audit_typeRef();

  public static ORADataFactory getORADataFactory()
  { return _Di_audit_typeRefFactory; }
  /* constructor */
  public Di_audit_typeRef()
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
    Di_audit_typeRef r = new Di_audit_typeRef();
    r._ref = (REF) d;
    return r;
  }

  public static Di_audit_typeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (Di_audit_typeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to Di_audit_typeRef: "+exn.toString()); }
  }

  public Di_audit_type getValue() throws SQLException
  {
     return (Di_audit_type) Di_audit_type.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(Di_audit_type c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
