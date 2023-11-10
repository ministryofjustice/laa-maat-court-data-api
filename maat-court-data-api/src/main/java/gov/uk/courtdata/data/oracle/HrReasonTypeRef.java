package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrReasonTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrReasonTypeRef _HrReasonTypeRefFactory = new HrReasonTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrReasonTypeRefFactory; }
  /* constructor */
  public HrReasonTypeRef()
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
    HrReasonTypeRef r = new HrReasonTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrReasonTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrReasonTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrReasonTypeRef: "+exn.toString()); }
  }

  public HrReasonType getValue() throws SQLException
  {
     return (HrReasonType) HrReasonType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrReasonType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
