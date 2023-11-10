package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class LSCTransferTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.LSC_TRANSFERSTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final LSCTransferTypeRef _LSCTransferTypeRefFactory = new LSCTransferTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _LSCTransferTypeRefFactory; }
  /* constructor */
  public LSCTransferTypeRef()
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
    LSCTransferTypeRef r = new LSCTransferTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static LSCTransferTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (LSCTransferTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to LSCTransferTypeRef: "+exn.toString()); }
  }

  public LSCTransferType getValue() throws SQLException
  {
     return (LSCTransferType) LSCTransferType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(LSCTransferType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
