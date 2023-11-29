package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrProgressTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_PROGRESS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrProgressTypeRef _HrProgressTypeRefFactory = new HrProgressTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrProgressTypeRefFactory; }
  /* constructor */
  public HrProgressTypeRef()
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
    HrProgressTypeRef r = new HrProgressTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrProgressTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrProgressTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrProgressTypeRef: "+exn.toString()); }
  }

  public HrProgressType getValue() throws SQLException
  {
     return (HrProgressType) HrProgressType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrProgressType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
