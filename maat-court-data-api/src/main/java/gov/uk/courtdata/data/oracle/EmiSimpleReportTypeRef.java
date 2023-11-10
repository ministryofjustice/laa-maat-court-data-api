package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EmiSimpleReportTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.EMISIMPLEREPORTTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final EmiSimpleReportTypeRef _EmiSimpleReportTypeRefFactory = new EmiSimpleReportTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _EmiSimpleReportTypeRefFactory; }
  /* constructor */
  public EmiSimpleReportTypeRef()
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
    EmiSimpleReportTypeRef r = new EmiSimpleReportTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static EmiSimpleReportTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (EmiSimpleReportTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to EmiSimpleReportTypeRef: "+exn.toString()); }
  }

  public EmiSimpleReportType getValue() throws SQLException
  {
     return (EmiSimpleReportType) EmiSimpleReportType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(EmiSimpleReportType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
