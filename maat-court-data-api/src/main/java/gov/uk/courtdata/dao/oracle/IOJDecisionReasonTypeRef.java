package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class IOJDecisionReasonTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.IOJ_DECISION_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final IOJDecisionReasonTypeRef _IOJDecisionReasonTypeRefFactory = new IOJDecisionReasonTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _IOJDecisionReasonTypeRefFactory; }
  /* constructor */
  public IOJDecisionReasonTypeRef()
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
    IOJDecisionReasonTypeRef r = new IOJDecisionReasonTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static IOJDecisionReasonTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (IOJDecisionReasonTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to IOJDecisionReasonTypeRef: "+exn.toString()); }
  }

  public IOJDecisionReasonType getValue() throws SQLException
  {
     return (IOJDecisionReasonType) IOJDecisionReasonType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(IOJDecisionReasonType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
