package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class RoleTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ROLETYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final RoleTypeRef _RoleTypeRefFactory = new RoleTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _RoleTypeRefFactory; }
  /* constructor */
  public RoleTypeRef()
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
    RoleTypeRef r = new RoleTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static RoleTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (RoleTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to RoleTypeRef: "+exn.toString()); }
  }

  public RoleType getValue() throws SQLException
  {
     return (RoleType) RoleType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(RoleType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
