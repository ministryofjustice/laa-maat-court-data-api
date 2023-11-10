package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ContraryInterestTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CONTRARY_INTEREST_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ContraryInterestTypeRef _ContraryInterestTypeRefFactory = new ContraryInterestTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ContraryInterestTypeRefFactory; }
  /* constructor */
  public ContraryInterestTypeRef()
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
    ContraryInterestTypeRef r = new ContraryInterestTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ContraryInterestTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ContraryInterestTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ContraryInterestTypeRef: "+exn.toString()); }
  }

  public ContraryInterestType getValue() throws SQLException
  {
     return (ContraryInterestType) ContraryInterestType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ContraryInterestType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
