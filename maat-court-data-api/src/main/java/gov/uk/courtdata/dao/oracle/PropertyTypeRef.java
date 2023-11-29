package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PropertyTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.PROPERTY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final PropertyTypeRef _PropertyTypeRefFactory = new PropertyTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _PropertyTypeRefFactory; }
  /* constructor */
  public PropertyTypeRef()
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
    PropertyTypeRef r = new PropertyTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static PropertyTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (PropertyTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to PropertyTypeRef: "+exn.toString()); }
  }

  public PropertyType getValue() throws SQLException
  {
     return (PropertyType) PropertyType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(PropertyType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
