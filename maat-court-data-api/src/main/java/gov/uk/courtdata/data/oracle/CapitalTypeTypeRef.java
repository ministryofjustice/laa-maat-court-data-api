package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CAPITAL_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CapitalTypeTypeRef _CapitalTypeTypeRefFactory = new CapitalTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CapitalTypeTypeRefFactory; }
  /* constructor */
  public CapitalTypeTypeRef()
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
    CapitalTypeTypeRef r = new CapitalTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CapitalTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CapitalTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CapitalTypeTypeRef: "+exn.toString()); }
  }

  public CapitalTypeType getValue() throws SQLException
  {
     return (CapitalTypeType) CapitalTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CapitalTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
