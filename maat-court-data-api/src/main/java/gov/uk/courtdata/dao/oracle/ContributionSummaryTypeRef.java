package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ContributionSummaryTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CONTRIBUTION_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ContributionSummaryTypeRef _ContributionSummaryTypeRefFactory = new ContributionSummaryTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ContributionSummaryTypeRefFactory; }
  /* constructor */
  public ContributionSummaryTypeRef()
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
    ContributionSummaryTypeRef r = new ContributionSummaryTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ContributionSummaryTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ContributionSummaryTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ContributionSummaryTypeRef: "+exn.toString()); }
  }

  public ContributionSummaryType getValue() throws SQLException
  {
     return (ContributionSummaryType) ContributionSummaryType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ContributionSummaryType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
