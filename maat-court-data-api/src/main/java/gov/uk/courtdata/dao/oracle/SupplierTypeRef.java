package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SupplierTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.SUPPLIER_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final SupplierTypeRef _SupplierTypeRefFactory = new SupplierTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _SupplierTypeRefFactory; }
  /* constructor */
  public SupplierTypeRef()
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
    SupplierTypeRef r = new SupplierTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static SupplierTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (SupplierTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to SupplierTypeRef: "+exn.toString()); }
  }

  public SupplierType getValue() throws SQLException
  {
     return (SupplierType) SupplierType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(SupplierType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
