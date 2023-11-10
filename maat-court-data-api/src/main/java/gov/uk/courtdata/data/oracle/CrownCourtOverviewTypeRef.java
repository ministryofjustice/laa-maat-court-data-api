package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CrownCourtOverviewTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.CROWN_COURT_OVERVIEW_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final CrownCourtOverviewTypeRef _CrownCourtOverviewTypeRefFactory = new CrownCourtOverviewTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _CrownCourtOverviewTypeRefFactory; }
  /* constructor */
  public CrownCourtOverviewTypeRef()
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
    CrownCourtOverviewTypeRef r = new CrownCourtOverviewTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static CrownCourtOverviewTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (CrownCourtOverviewTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to CrownCourtOverviewTypeRef: "+exn.toString()); }
  }

  public CrownCourtOverviewType getValue() throws SQLException
  {
     return (CrownCourtOverviewType) CrownCourtOverviewType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(CrownCourtOverviewType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
