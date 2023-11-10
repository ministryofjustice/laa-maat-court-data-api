package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AppealsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.APPEALS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final AppealsTypeRef _AppealsTypeRefFactory = new AppealsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _AppealsTypeRefFactory; }
  /* constructor */
  public AppealsTypeRef()
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
    AppealsTypeRef r = new AppealsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static AppealsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (AppealsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to AppealsTypeRef: "+exn.toString()); }
  }

  public AppealsType getValue() throws SQLException
  {
     return (AppealsType) AppealsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(AppealsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
