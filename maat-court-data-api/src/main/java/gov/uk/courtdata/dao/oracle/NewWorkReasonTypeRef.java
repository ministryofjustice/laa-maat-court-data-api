package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class NewWorkReasonTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.NEW_WORK_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final NewWorkReasonTypeRef _NewWorkReasonTypeRefFactory = new NewWorkReasonTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _NewWorkReasonTypeRefFactory; }
  /* constructor */
  public NewWorkReasonTypeRef()
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
    NewWorkReasonTypeRef r = new NewWorkReasonTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static NewWorkReasonTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (NewWorkReasonTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to NewWorkReasonTypeRef: "+exn.toString()); }
  }

  public NewWorkReasonType getValue() throws SQLException
  {
     return (NewWorkReasonType) NewWorkReasonType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(NewWorkReasonType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
