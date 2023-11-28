package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class FdcItemsTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.FDC_ITEMS_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final FdcItemsTabtype _FdcItemsTabtypeFactory = new FdcItemsTabtype();

  public static ORADataFactory getORADataFactory()
  { return _FdcItemsTabtypeFactory; }
  /* constructors */
  public FdcItemsTabtype()
  {
    this((FdcItemsType[])null);
  }

  public FdcItemsTabtype(FdcItemsType[] a)
  {
    _array = new MutableArray(2002, a, FdcItemsType.getORADataFactory());
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
    FdcItemsTabtype a = new FdcItemsTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, FdcItemsType.getORADataFactory());
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
  public FdcItemsType[] getArray() throws SQLException
  {
    return (FdcItemsType[]) _array.getObjectArray(
      new FdcItemsType[_array.length()]);
  }

  public FdcItemsType[] getArray(long index, int count) throws SQLException
  {
    return (FdcItemsType[]) _array.getObjectArray(index,
      new FdcItemsType[_array.sliceLength(index, count)]);
  }

  public void setArray(FdcItemsType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(FdcItemsType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public FdcItemsType getElement(long index) throws SQLException
  {
    return (FdcItemsType) _array.getObjectElement(index);
  }

  public void setElement(FdcItemsType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
