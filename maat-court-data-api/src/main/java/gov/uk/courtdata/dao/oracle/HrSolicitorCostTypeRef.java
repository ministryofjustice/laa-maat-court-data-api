package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrSolicitorCostTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_SOLICITOR_COSTS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrSolicitorCostTypeRef _HrSolicitorCostTypeRefFactory = new HrSolicitorCostTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrSolicitorCostTypeRefFactory; }
  /* constructor */
  public HrSolicitorCostTypeRef()
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
    HrSolicitorCostTypeRef r = new HrSolicitorCostTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrSolicitorCostTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrSolicitorCostTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrSolicitorCostTypeRef: "+exn.toString()); }
  }

  public HrSolicitorCostType getValue() throws SQLException
  {
     return (HrSolicitorCostType) HrSolicitorCostType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrSolicitorCostType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
