package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AreaTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.AREATYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AreaTypeRef _AreaTypeRefFactory = new AreaTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AreaTypeRefFactory; }
  /* constructor */
  public AreaTypeRef()
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
    AreaTypeRef r = new AreaTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AreaTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AreaTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AreaTypeRef: "+exn.toString()); }
  }

  public AreaType getValue() throws SQLException
  {
     return (AreaType) AreaType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AreaType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
