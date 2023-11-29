package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class FdcItemsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.FDC_ITEMS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final FdcItemsTypeRef _FdcItemsTypeRefFactory = new FdcItemsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _FdcItemsTypeRefFactory; }
  /* constructor */
  public FdcItemsTypeRef()
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
    FdcItemsTypeRef r = new FdcItemsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static FdcItemsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (FdcItemsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to FdcItemsTypeRef: "+exn.toString()); }
  }

  public FdcItemsType getValue() throws SQLException
  {
     return (FdcItemsType) FdcItemsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(FdcItemsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
