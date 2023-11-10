package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ReportDataTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.REPORT_DATATYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final ReportDataTypeRef _ReportDataTypeRefFactory = new ReportDataTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _ReportDataTypeRefFactory; }
  /* constructor */
  public ReportDataTypeRef()
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
    ReportDataTypeRef r = new ReportDataTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static ReportDataTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (ReportDataTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to ReportDataTypeRef: "+exn.toString()); }
  }

  public ReportDataType getValue() throws SQLException
  {
     return (ReportDataType) ReportDataType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(ReportDataType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
