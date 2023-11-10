package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrProgressActionTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_PROGRESS_ACTION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrProgressActionTypeRef _HrProgressActionTypeRefFactory = new HrProgressActionTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrProgressActionTypeRefFactory; }
  /* constructor */
  public HrProgressActionTypeRef()
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
    HrProgressActionTypeRef r = new HrProgressActionTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrProgressActionTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrProgressActionTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrProgressActionTypeRef: "+exn.toString()); }
  }

  public HrProgressActionType getValue() throws SQLException
  {
     return (HrProgressActionType) HrProgressActionType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrProgressActionType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
