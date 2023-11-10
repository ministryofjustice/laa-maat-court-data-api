package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class FrequencyTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.FREQUENCY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final FrequencyTypeRef _FrequencyTypeRefFactory = new FrequencyTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _FrequencyTypeRefFactory; }
  /* constructor */
  public FrequencyTypeRef()
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
    FrequencyTypeRef r = new FrequencyTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static FrequencyTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (FrequencyTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to FrequencyTypeRef: "+exn.toString()); }
  }

  public FrequencyType getValue() throws SQLException
  {
     return (FrequencyType) FrequencyType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(FrequencyType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
