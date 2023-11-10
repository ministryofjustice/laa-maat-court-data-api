package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class FdcContributionsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.FDC_CONTRIBUTIONS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final FdcContributionsTypeRef _FdcContributionsTypeRefFactory = new FdcContributionsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _FdcContributionsTypeRefFactory; }
  /* constructor */
  public FdcContributionsTypeRef()
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
    FdcContributionsTypeRef r = new FdcContributionsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static FdcContributionsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (FdcContributionsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to FdcContributionsTypeRef: "+exn.toString()); }
  }

  public FdcContributionsType getValue() throws SQLException
  {
     return (FdcContributionsType) FdcContributionsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(FdcContributionsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
