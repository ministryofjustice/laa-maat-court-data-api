package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class MvoRegTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.MVO_REG_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final MvoRegTypeRef _MvoRegTypeRefFactory = new MvoRegTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _MvoRegTypeRefFactory; }
  /* constructor */
  public MvoRegTypeRef()
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
    MvoRegTypeRef r = new MvoRegTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static MvoRegTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (MvoRegTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to MvoRegTypeRef: "+exn.toString()); }
  }

  public MvoRegType getValue() throws SQLException
  {
     return (MvoRegType) MvoRegType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(MvoRegType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
