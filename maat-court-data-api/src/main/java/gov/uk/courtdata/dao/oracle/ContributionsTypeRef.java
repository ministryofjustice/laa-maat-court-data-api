package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ContributionsTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CONTRIBUTIONS_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ContributionsTypeRef _ContributionsTypeRefFactory = new ContributionsTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ContributionsTypeRefFactory; }
  /* constructor */
  public ContributionsTypeRef()
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
    ContributionsTypeRef r = new ContributionsTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ContributionsTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ContributionsTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ContributionsTypeRef: "+exn.toString()); }
  }

  public ContributionsType getValue() throws SQLException
  {
     return (ContributionsType) ContributionsType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ContributionsType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
