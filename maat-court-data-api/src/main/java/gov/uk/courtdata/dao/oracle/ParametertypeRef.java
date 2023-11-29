package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ParametertypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "EMI_DATA.PARAMETERTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ParametertypeRef _ParametertypeRefFactory = new ParametertypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ParametertypeRefFactory; }
  /* constructor */
  public ParametertypeRef()
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
    ParametertypeRef r = new ParametertypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ParametertypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ParametertypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ParametertypeRef: "+exn.toString()); }
  }

  public Parametertype getValue() throws SQLException
  {
     return (Parametertype) Parametertype.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(Parametertype c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
