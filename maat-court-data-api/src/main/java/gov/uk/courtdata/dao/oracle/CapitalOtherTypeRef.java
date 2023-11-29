package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalOtherTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CAPITAL_OTHER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CapitalOtherTypeRef _CapitalOtherTypeRefFactory = new CapitalOtherTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CapitalOtherTypeRefFactory; }
  /* constructor */
  public CapitalOtherTypeRef()
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
    CapitalOtherTypeRef r = new CapitalOtherTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CapitalOtherTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CapitalOtherTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CapitalOtherTypeRef: "+exn.toString()); }
  }

  public CapitalOtherType getValue() throws SQLException
  {
     return (CapitalOtherType) CapitalOtherType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CapitalOtherType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
