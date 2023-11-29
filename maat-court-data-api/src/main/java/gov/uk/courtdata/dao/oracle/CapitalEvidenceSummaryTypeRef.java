package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalEvidenceSummaryTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CAPITAL_EVIDENCE_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CapitalEvidenceSummaryTypeRef _CapitalEvidenceSummaryTypeRefFactory = new CapitalEvidenceSummaryTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CapitalEvidenceSummaryTypeRefFactory; }
  /* constructor */
  public CapitalEvidenceSummaryTypeRef()
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
    CapitalEvidenceSummaryTypeRef r = new CapitalEvidenceSummaryTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CapitalEvidenceSummaryTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CapitalEvidenceSummaryTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CapitalEvidenceSummaryTypeRef: "+exn.toString()); }
  }

  public CapitalEvidenceSummaryType getValue() throws SQLException
  {
     return (CapitalEvidenceSummaryType) CapitalEvidenceSummaryType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CapitalEvidenceSummaryType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
