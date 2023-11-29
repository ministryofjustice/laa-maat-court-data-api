package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DisabilityTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.DISABILITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final DisabilityTypeRef _DisabilityTypeRefFactory = new DisabilityTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _DisabilityTypeRefFactory; }
  /* constructor */
  public DisabilityTypeRef()
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
    DisabilityTypeRef r = new DisabilityTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static DisabilityTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (DisabilityTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to DisabilityTypeRef: "+exn.toString()); }
  }

  public DisabilityType getValue() throws SQLException
  {
     return (DisabilityType) DisabilityType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(DisabilityType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
