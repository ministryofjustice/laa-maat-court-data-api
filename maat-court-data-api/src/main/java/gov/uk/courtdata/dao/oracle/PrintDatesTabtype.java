package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class PrintDatesTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.PRINT_DATES_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final PrintDatesTabtype _PrintDatesTabtypeFactory = new PrintDatesTabtype();

  public static ORADataFactory getORADataFactory()
  { return _PrintDatesTabtypeFactory; }
  /* constructors */
  public PrintDatesTabtype()
  {
    this((PrintDatesType[])null);
  }

  public PrintDatesTabtype(PrintDatesType[] a)
  {
    _array = new MutableArray(2002, a, PrintDatesType.getORADataFactory());
  }

  /* ORAData interface */
  public Datum toDatum(Connection c) throws SQLException
  {
    return _array.toDatum(c, _SQL_NAME);
  }

  /* ORADataFactory interface */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null; 
    PrintDatesTabtype a = new PrintDatesTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, PrintDatesType.getORADataFactory());
    return a;
  }

  public int length() throws SQLException
  {
    return _array.length();
  }

  public int getBaseType() throws SQLException
  {
    return _array.getBaseType();
  }

  public String getBaseTypeName() throws SQLException
  {
    return _array.getBaseTypeName();
  }

  public ArrayDescriptor getDescriptor() throws SQLException
  {
    return _array.getDescriptor();
  }

  /* array accessor methods */
  public PrintDatesType[] getArray() throws SQLException
  {
    return (PrintDatesType[]) _array.getObjectArray(
      new PrintDatesType[_array.length()]);
  }

  public PrintDatesType[] getArray(long index, int count) throws SQLException
  {
    return (PrintDatesType[]) _array.getObjectArray(index,
      new PrintDatesType[_array.sliceLength(index, count)]);
  }

  public void setArray(PrintDatesType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(PrintDatesType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public PrintDatesType getElement(long index) throws SQLException
  {
    return (PrintDatesType) _array.getObjectElement(index);
  }

  public void setElement(PrintDatesType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
