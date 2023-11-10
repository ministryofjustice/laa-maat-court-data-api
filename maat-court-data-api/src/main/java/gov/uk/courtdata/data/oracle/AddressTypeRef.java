package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AddressTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ADDRESS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AddressTypeRef _AddressTypeRefFactory = new AddressTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AddressTypeRefFactory; }
  /* constructor */
  public AddressTypeRef()
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
    AddressTypeRef r = new AddressTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AddressTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AddressTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AddressTypeRef: "+exn.toString()); }
  }

  public AddressType getValue() throws SQLException
  {
     return (AddressType) AddressType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AddressType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
