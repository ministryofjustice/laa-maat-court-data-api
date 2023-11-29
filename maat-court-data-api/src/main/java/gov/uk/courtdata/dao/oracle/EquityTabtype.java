package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EquityTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.EQUITY_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final EquityTabtype _EquityTabtypeFactory = new EquityTabtype();

  public static ORADataFactory getORADataFactory()
  { return _EquityTabtypeFactory; }
  /* constructors */
  public EquityTabtype()
  {
    this((EquityType[])null);
  }

  public EquityTabtype(EquityType[] a)
  {
    _array = new MutableArray(2002, a, EquityType.getORADataFactory());
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
    EquityTabtype a = new EquityTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, EquityType.getORADataFactory());
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
  public EquityType[] getArray() throws SQLException
  {
    return (EquityType[]) _array.getObjectArray(
      new EquityType[_array.length()]);
  }

  public EquityType[] getArray(long index, int count) throws SQLException
  {
    return (EquityType[]) _array.getObjectArray(index,
      new EquityType[_array.sliceLength(index, count)]);
  }

  public void setArray(EquityType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(EquityType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public EquityType getElement(long index) throws SQLException
  {
    return (EquityType) _array.getObjectElement(index);
  }

  public void setElement(EquityType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
