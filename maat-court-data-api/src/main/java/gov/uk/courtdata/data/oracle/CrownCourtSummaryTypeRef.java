package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CrownCourtSummaryTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CROWN_COURT_SUMMARY_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CrownCourtSummaryTypeRef _CrownCourtSummaryTypeRefFactory = new CrownCourtSummaryTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CrownCourtSummaryTypeRefFactory; }
  /* constructor */
  public CrownCourtSummaryTypeRef()
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
    CrownCourtSummaryTypeRef r = new CrownCourtSummaryTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CrownCourtSummaryTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CrownCourtSummaryTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CrownCourtSummaryTypeRef: "+exn.toString()); }
  }

  public CrownCourtSummaryType getValue() throws SQLException
  {
     return (CrownCourtSummaryType) CrownCourtSummaryType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CrownCourtSummaryType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
