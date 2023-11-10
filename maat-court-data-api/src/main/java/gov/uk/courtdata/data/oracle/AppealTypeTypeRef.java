package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AppealTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.APPEAL_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AppealTypeTypeRef _AppealTypeTypeRefFactory = new AppealTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AppealTypeTypeRefFactory; }
  /* constructor */
  public AppealTypeTypeRef()
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
    AppealTypeTypeRef r = new AppealTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AppealTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AppealTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AppealTypeTypeRef: "+exn.toString()); }
  }

  public AppealTypeType getValue() throws SQLException
  {
     return (AppealTypeType) AppealTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AppealTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
