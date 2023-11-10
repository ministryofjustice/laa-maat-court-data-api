package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRoleTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.USER_ROLETYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final UserRoleTypeRef _UserRoleTypeRefFactory = new UserRoleTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _UserRoleTypeRefFactory; }
  /* constructor */
  public UserRoleTypeRef()
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
    UserRoleTypeRef r = new UserRoleTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static UserRoleTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (UserRoleTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to UserRoleTypeRef: "+exn.toString()); }
  }

  public UserRoleType getValue() throws SQLException
  {
     return (UserRoleType) UserRoleType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(UserRoleType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
