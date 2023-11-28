package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HardshipOverviewTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HARDSHIP_OVERVIEW_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HardshipOverviewTypeRef _HardshipOverviewTypeRefFactory = new HardshipOverviewTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HardshipOverviewTypeRefFactory; }
  /* constructor */
  public HardshipOverviewTypeRef()
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
    HardshipOverviewTypeRef r = new HardshipOverviewTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HardshipOverviewTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HardshipOverviewTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HardshipOverviewTypeRef: "+exn.toString()); }
  }

  public HardshipOverviewType getValue() throws SQLException
  {
     return (HardshipOverviewType) HardshipOverviewType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HardshipOverviewType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
