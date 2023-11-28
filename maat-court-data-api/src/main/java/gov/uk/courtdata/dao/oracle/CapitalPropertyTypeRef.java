package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalPropertyTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CAPITAL_PROPERTY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CapitalPropertyTypeRef _CapitalPropertyTypeRefFactory = new CapitalPropertyTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CapitalPropertyTypeRefFactory; }
  /* constructor */
  public CapitalPropertyTypeRef()
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
    CapitalPropertyTypeRef r = new CapitalPropertyTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CapitalPropertyTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CapitalPropertyTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CapitalPropertyTypeRef: "+exn.toString()); }
  }

  public CapitalPropertyType getValue() throws SQLException
  {
     return (CapitalPropertyType) CapitalPropertyType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CapitalPropertyType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
