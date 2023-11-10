package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class IncomeEvidenceSummaryTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.INCOME_EVIDENCE_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final IncomeEvidenceSummaryTypeRef _IncomeEvidenceSummaryTypeRefFactory = new IncomeEvidenceSummaryTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _IncomeEvidenceSummaryTypeRefFactory; }
  /* constructor */
  public IncomeEvidenceSummaryTypeRef()
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
    IncomeEvidenceSummaryTypeRef r = new IncomeEvidenceSummaryTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static IncomeEvidenceSummaryTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (IncomeEvidenceSummaryTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to IncomeEvidenceSummaryTypeRef: "+exn.toString()); }
  }

  public IncomeEvidenceSummaryType getValue() throws SQLException
  {
     return (IncomeEvidenceSummaryType) IncomeEvidenceSummaryType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(IncomeEvidenceSummaryType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
