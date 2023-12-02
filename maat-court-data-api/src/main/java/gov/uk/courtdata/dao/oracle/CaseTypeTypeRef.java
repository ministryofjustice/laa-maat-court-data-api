package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CaseTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CASE_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CaseTypeTypeRef _CaseTypeTypeRefFactory = new CaseTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CaseTypeTypeRefFactory; }
  /* constructor */
  public CaseTypeTypeRef()
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
    CaseTypeTypeRef r = new CaseTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CaseTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CaseTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CaseTypeTypeRef: "+exn.toString()); }
  }

  public CaseTypeType getValue() throws SQLException
  {
     return (CaseTypeType) CaseTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CaseTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
