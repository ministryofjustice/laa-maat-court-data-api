package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class TransferStatusTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.TRANSFER_STATUS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final TransferStatusTypeRef _TransferStatusTypeRefFactory = new TransferStatusTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _TransferStatusTypeRefFactory; }
  /* constructor */
  public TransferStatusTypeRef()
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
    TransferStatusTypeRef r = new TransferStatusTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static TransferStatusTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (TransferStatusTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to TransferStatusTypeRef: "+exn.toString()); }
  }

  public TransferStatusType getValue() throws SQLException
  {
     return (TransferStatusType) TransferStatusType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(TransferStatusType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
