package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AreaTransferTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.AREA_TRANSFERSTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AreaTransferTypeRef _AreaTransferTypeRefFactory = new AreaTransferTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AreaTransferTypeRefFactory; }
  /* constructor */
  public AreaTransferTypeRef()
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
    AreaTransferTypeRef r = new AreaTransferTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AreaTransferTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AreaTransferTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AreaTransferTypeRef: "+exn.toString()); }
  }

  public AreaTransferType getValue() throws SQLException
  {
     return (AreaTransferType) AreaTransferType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AreaTransferType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
