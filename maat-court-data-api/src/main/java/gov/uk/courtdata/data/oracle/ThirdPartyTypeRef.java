package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ThirdPartyTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.THIRD_PARTY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ThirdPartyTypeRef _ThirdPartyTypeRefFactory = new ThirdPartyTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ThirdPartyTypeRefFactory; }
  /* constructor */
  public ThirdPartyTypeRef()
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
    ThirdPartyTypeRef r = new ThirdPartyTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ThirdPartyTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ThirdPartyTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ThirdPartyTypeRef: "+exn.toString()); }
  }

  public ThirdPartyType getValue() throws SQLException
  {
     return (ThirdPartyType) ThirdPartyType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ThirdPartyType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
