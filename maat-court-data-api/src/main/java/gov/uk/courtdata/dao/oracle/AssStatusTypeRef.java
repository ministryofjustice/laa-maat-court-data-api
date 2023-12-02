package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssStatusTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ASS_STATUS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AssStatusTypeRef _AssStatusTypeRefFactory = new AssStatusTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AssStatusTypeRefFactory; }
  /* constructor */
  public AssStatusTypeRef()
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
    AssStatusTypeRef r = new AssStatusTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AssStatusTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AssStatusTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AssStatusTypeRef: "+exn.toString()); }
  }

  public AssStatusType getValue() throws SQLException
  {
     return (AssStatusType) AssStatusType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AssStatusType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
