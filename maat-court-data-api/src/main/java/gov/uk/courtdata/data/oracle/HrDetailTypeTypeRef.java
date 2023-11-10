package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrDetailTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_DETAIL_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrDetailTypeTypeRef _HrDetailTypeTypeRefFactory = new HrDetailTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrDetailTypeTypeRefFactory; }
  /* constructor */
  public HrDetailTypeTypeRef()
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
    HrDetailTypeTypeRef r = new HrDetailTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrDetailTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrDetailTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrDetailTypeTypeRef: "+exn.toString()); }
  }

  public HrDetailTypeType getValue() throws SQLException
  {
     return (HrDetailTypeType) HrDetailTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrDetailTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
