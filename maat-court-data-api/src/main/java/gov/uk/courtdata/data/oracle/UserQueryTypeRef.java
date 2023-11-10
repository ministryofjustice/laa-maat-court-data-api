package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class UserQueryTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.USER_QUERY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final UserQueryTypeRef _UserQueryTypeRefFactory = new UserQueryTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _UserQueryTypeRefFactory; }
  /* constructor */
  public UserQueryTypeRef()
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
    UserQueryTypeRef r = new UserQueryTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static UserQueryTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (UserQueryTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to UserQueryTypeRef: "+exn.toString()); }
  }

  public UserQueryType getValue() throws SQLException
  {
     return (UserQueryType) UserQueryType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(UserQueryType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
