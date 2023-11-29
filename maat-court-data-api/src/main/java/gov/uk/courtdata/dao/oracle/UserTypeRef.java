package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.USER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final UserTypeRef _UserTypeRefFactory = new UserTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _UserTypeRefFactory; }
  /* constructor */
  public UserTypeRef()
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
    UserTypeRef r = new UserTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static UserTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (UserTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to UserTypeRef: "+exn.toString()); }
  }

  public UserType getValue() throws SQLException
  {
     return (UserType) UserType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(UserType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
