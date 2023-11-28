package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HrSectionTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.HR_SECTION_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final HrSectionTypeRef _HrSectionTypeRefFactory = new HrSectionTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _HrSectionTypeRefFactory; }
  /* constructor */
  public HrSectionTypeRef()
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
    HrSectionTypeRef r = new HrSectionTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static HrSectionTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (HrSectionTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to HrSectionTypeRef: "+exn.toString()); }
  }

  public HrSectionType getValue() throws SQLException
  {
     return (HrSectionType) HrSectionType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(HrSectionType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
