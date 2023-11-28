package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class TransferTypeTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.TRANSFER_TYPE_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final TransferTypeTypeRef _TransferTypeTypeRefFactory = new TransferTypeTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _TransferTypeTypeRefFactory; }
  /* constructor */
  public TransferTypeTypeRef()
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
    TransferTypeTypeRef r = new TransferTypeTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static TransferTypeTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (TransferTypeTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to TransferTypeTypeRef: "+exn.toString()); }
  }

  public TransferTypeType getValue() throws SQLException
  {
     return (TransferTypeType) TransferTypeType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(TransferTypeType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
