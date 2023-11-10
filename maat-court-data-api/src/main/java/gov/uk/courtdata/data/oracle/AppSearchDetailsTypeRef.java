package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AppSearchDetailsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.APP_SEARCH_DETAILS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AppSearchDetailsTypeRef _AppSearchDetailsTypeRefFactory = new AppSearchDetailsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AppSearchDetailsTypeRefFactory; }
  /* constructor */
  public AppSearchDetailsTypeRef()
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
    AppSearchDetailsTypeRef r = new AppSearchDetailsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AppSearchDetailsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AppSearchDetailsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AppSearchDetailsTypeRef: "+exn.toString()); }
  }

  public AppSearchDetailsType getValue() throws SQLException
  {
     return (AppSearchDetailsType) AppSearchDetailsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AppSearchDetailsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
