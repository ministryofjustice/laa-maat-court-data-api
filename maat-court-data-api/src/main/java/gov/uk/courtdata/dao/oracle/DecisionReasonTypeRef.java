package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DecisionReasonTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.DECISION_REASON_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final DecisionReasonTypeRef _DecisionReasonTypeRefFactory = new DecisionReasonTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _DecisionReasonTypeRefFactory; }
  /* constructor */
  public DecisionReasonTypeRef()
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
    DecisionReasonTypeRef r = new DecisionReasonTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static DecisionReasonTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (DecisionReasonTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to DecisionReasonTypeRef: "+exn.toString()); }
  }

  public DecisionReasonType getValue() throws SQLException
  {
     return (DecisionReasonType) DecisionReasonType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(DecisionReasonType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
