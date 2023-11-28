package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.SQLException;

public class PrintDatesType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.PRINT_DATES_TYPE";
  public static final int _SQL_TYPECODE = OracleTypes.STRUCT;

  protected MutableStruct _struct;

  protected static int[] _sqlType =  { 2,2,91 };
  protected static ORADataFactory[] _factory = new ORADataFactory[3];
  protected static final PrintDatesType _PrintDatesTypeFactory = new PrintDatesType();

  public static ORADataFactory getORADataFactory()
  { return _PrintDatesTypeFactory; }
  /* constructors */
  protected void _init_struct(boolean init)
  { if (init) _struct = new MutableStruct(new Object[3], _sqlType, _factory); }
  public PrintDatesType()
  { _init_struct(true); }
  public PrintDatesType(java.math.BigDecimal id, java.math.BigDecimal corrId, java.sql.Timestamp printDate) throws SQLException
  { _init_struct(true);
    setId(id);
    setCorrId(corrId);
    setPrintDate(printDate);
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _struct.toDatum(c, _SQL_NAME);
  }


  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  { return create(null, d, sqlType); }
  protected ORAData create(PrintDatesType o, Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    if (o == null) o = new PrintDatesType();
    o._struct = new MutableStruct((STRUCT) d, _sqlType, _factory);
    return o;
  }
  /* accessor methods */
  public java.math.BigDecimal getId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(0); }

  public void setId(java.math.BigDecimal id) throws SQLException
  { _struct.setAttribute(0, id); }


  public java.math.BigDecimal getCorrId() throws SQLException
  { return (java.math.BigDecimal) _struct.getAttribute(1); }

  public void setCorrId(java.math.BigDecimal corrId) throws SQLException
  { _struct.setAttribute(1, corrId); }


  public java.sql.Timestamp getPrintDate() throws SQLException
  { return (java.sql.Timestamp) _struct.getAttribute(2); }

  public void setPrintDate(java.sql.Timestamp printDate) throws SQLException
  { _struct.setAttribute(2, printDate); }

}
