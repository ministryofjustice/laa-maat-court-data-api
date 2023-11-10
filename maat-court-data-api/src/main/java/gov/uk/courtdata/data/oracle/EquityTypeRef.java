package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EquityTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.EQUITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final EquityTypeRef _EquityTypeRefFactory = new EquityTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _EquityTypeRefFactory; }
  /* constructor */
  public EquityTypeRef()
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
    EquityTypeRef r = new EquityTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static EquityTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (EquityTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to EquityTypeRef: "+exn.toString()); }
  }

  public EquityType getValue() throws SQLException
  {
     return (EquityType) EquityType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(EquityType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
