package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserSessionTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.USER_SESSION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final UserSessionTypeRef _UserSessionTypeRefFactory = new UserSessionTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _UserSessionTypeRefFactory; }
  /* constructor */
  public UserSessionTypeRef()
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
    UserSessionTypeRef r = new UserSessionTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static UserSessionTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (UserSessionTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to UserSessionTypeRef: "+exn.toString()); }
  }

  public UserSessionType getValue() throws SQLException
  {
     return (UserSessionType) UserSessionType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(UserSessionType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
