package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EmpStatusTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.EMP_STATUS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final EmpStatusTypeRef _EmpStatusTypeRefFactory = new EmpStatusTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _EmpStatusTypeRefFactory; }
  /* constructor */
  public EmpStatusTypeRef()
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
    EmpStatusTypeRef r = new EmpStatusTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static EmpStatusTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (EmpStatusTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to EmpStatusTypeRef: "+exn.toString()); }
  }

  public EmpStatusType getValue() throws SQLException
  {
     return (EmpStatusType) EmpStatusType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(EmpStatusType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
