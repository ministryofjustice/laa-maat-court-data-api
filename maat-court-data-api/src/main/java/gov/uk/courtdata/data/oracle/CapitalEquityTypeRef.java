package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalEquityTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CAPITAL_EQUITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CapitalEquityTypeRef _CapitalEquityTypeRefFactory = new CapitalEquityTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CapitalEquityTypeRefFactory; }
  /* constructor */
  public CapitalEquityTypeRef()
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
    CapitalEquityTypeRef r = new CapitalEquityTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CapitalEquityTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CapitalEquityTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CapitalEquityTypeRef: "+exn.toString()); }
  }

  public CapitalEquityType getValue() throws SQLException
  {
     return (CapitalEquityType) CapitalEquityType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CapitalEquityType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
