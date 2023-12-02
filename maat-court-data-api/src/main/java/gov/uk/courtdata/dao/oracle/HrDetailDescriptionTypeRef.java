package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrDetailDescriptionTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_DETAIL_DESCRIPTION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrDetailDescriptionTypeRef _HrDetailDescriptionTypeRefFactory = new HrDetailDescriptionTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrDetailDescriptionTypeRefFactory; }
  /* constructor */
  public HrDetailDescriptionTypeRef()
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
    HrDetailDescriptionTypeRef r = new HrDetailDescriptionTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrDetailDescriptionTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrDetailDescriptionTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrDetailDescriptionTypeRef: "+exn.toString()); }
  }

  public HrDetailDescriptionType getValue() throws SQLException
  {
     return (HrDetailDescriptionType) HrDetailDescriptionType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrDetailDescriptionType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
