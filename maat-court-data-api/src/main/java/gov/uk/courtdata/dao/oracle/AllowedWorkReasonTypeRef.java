package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AllowedWorkReasonTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ALLOWED_WORK_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AllowedWorkReasonTypeRef _AllowedWorkReasonTypeRefFactory = new AllowedWorkReasonTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AllowedWorkReasonTypeRefFactory; }
  /* constructor */
  public AllowedWorkReasonTypeRef()
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
    AllowedWorkReasonTypeRef r = new AllowedWorkReasonTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AllowedWorkReasonTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AllowedWorkReasonTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AllowedWorkReasonTypeRef: "+exn.toString()); }
  }

  public AllowedWorkReasonType getValue() throws SQLException
  {
     return (AllowedWorkReasonType) AllowedWorkReasonType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AllowedWorkReasonType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
