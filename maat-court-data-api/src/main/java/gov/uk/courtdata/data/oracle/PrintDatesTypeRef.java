package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PrintDatesTypeRef implements ORAData, ORADataFactory
{
  public static final String _SQL_BASETYPE = "TOGDATA.PRINT_DATES_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.REF;

  REF _ref;

private static final PrintDatesTypeRef _PrintDatesTypeRefFactory = new PrintDatesTypeRef();

  public static ORADataFactory getORADataFactory()
  { return _PrintDatesTypeRefFactory; }
  /* constructor */
  public PrintDatesTypeRef()
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
    PrintDatesTypeRef r = new PrintDatesTypeRef();
    r._ref = (REF) d;
    return r;
  }

  public static PrintDatesTypeRef cast(ORAData o) throws SQLException
  {
     if (o == null) return null;
     try { return (PrintDatesTypeRef) getORADataFactory().create(o.toDatum(null), OracleTypes.REF); }
     catch (Exception exn)
     { throw new SQLException("Unable to convert "+o.getClass().getName()+" to PrintDatesTypeRef: "+exn.toString()); }
  }

  public PrintDatesType getValue() throws SQLException
  {
     return (PrintDatesType) PrintDatesType.getORADataFactory().create(
       _ref.getSTRUCT(), OracleTypes.REF);
  }

  public void setValue(PrintDatesType c) throws SQLException
  {
    _ref.setValue((STRUCT) c.toDatum(_ref.getJavaSqlConnection()));
  }
}
