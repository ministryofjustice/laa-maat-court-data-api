package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OutcomeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.OUTCOME_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final OutcomeTypeRef _OutcomeTypeRefFactory = new OutcomeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _OutcomeTypeRefFactory; }
  /* constructor */
  public OutcomeTypeRef()
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
    OutcomeTypeRef r = new OutcomeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static OutcomeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (OutcomeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to OutcomeTypeRef: "+exn.toString()); }
  }

  public OutcomeType getValue() throws SQLException
  {
     return (OutcomeType) OutcomeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(OutcomeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
