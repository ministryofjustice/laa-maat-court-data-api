package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PropertyTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.PROPERTY_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final PropertyTypeTypeRef _PropertyTypeTypeRefFactory = new PropertyTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _PropertyTypeTypeRefFactory; }
  /* constructor */
  public PropertyTypeTypeRef()
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
    PropertyTypeTypeRef r = new PropertyTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static PropertyTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (PropertyTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to PropertyTypeTypeRef: "+exn.toString()); }
  }

  public PropertyTypeType getValue() throws SQLException
  {
     return (PropertyTypeType) PropertyTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(PropertyTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
