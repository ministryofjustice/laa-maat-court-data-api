package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrDetailTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_DETAILTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrDetailTypeRef _HrDetailTypeRefFactory = new HrDetailTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrDetailTypeRefFactory; }
  /* constructor */
  public HrDetailTypeRef()
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
    HrDetailTypeRef r = new HrDetailTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrDetailTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrDetailTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrDetailTypeRef: "+exn.toString()); }
  }

  public HrDetailType getValue() throws SQLException
  {
     return (HrDetailType) HrDetailType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrDetailType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
