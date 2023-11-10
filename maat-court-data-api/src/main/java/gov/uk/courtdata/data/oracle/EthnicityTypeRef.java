package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EthnicityTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.ETHNICITY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final EthnicityTypeRef _EthnicityTypeRefFactory = new EthnicityTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _EthnicityTypeRefFactory; }
  /* constructor */
  public EthnicityTypeRef()
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
    EthnicityTypeRef r = new EthnicityTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static EthnicityTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (EthnicityTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to EthnicityTypeRef: "+exn.toString()); }
  }

  public EthnicityType getValue() throws SQLException
  {
     return (EthnicityType) EthnicityType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(EthnicityType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
