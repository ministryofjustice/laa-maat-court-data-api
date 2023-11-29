package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class RepStatusTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.REP_STATUS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final RepStatusTypeRef _RepStatusTypeRefFactory = new RepStatusTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _RepStatusTypeRefFactory; }
  /* constructor */
  public RepStatusTypeRef()
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
    RepStatusTypeRef r = new RepStatusTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static RepStatusTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (RepStatusTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to RepStatusTypeRef: "+exn.toString()); }
  }

  public RepStatusType getValue() throws SQLException
  {
     return (RepStatusType) RepStatusType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(RepStatusType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
