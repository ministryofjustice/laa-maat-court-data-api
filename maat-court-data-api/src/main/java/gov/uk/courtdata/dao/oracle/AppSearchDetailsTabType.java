package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AppSearchDetailsTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.APP_SEARCH_DETAILS_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final AppSearchDetailsTabType _AppSearchDetailsTabTypeFactory = new AppSearchDetailsTabType();

  public static ORADataFactory getORADataFactory()
  { return _AppSearchDetailsTabTypeFactory; }
  /* constructors */
  public AppSearchDetailsTabType()
  {
    this((AppSearchDetailsType[])null);
  }

  public AppSearchDetailsTabType(AppSearchDetailsType[] a)
  {
    _array = new MutableArray(2002, a, AppSearchDetailsType.getORADataFactory());
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
    AppSearchDetailsTabType a = new AppSearchDetailsTabType();
    a._array = new MutableArray(2002, (ARRAY) d, AppSearchDetailsType.getORADataFactory());
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
  public AppSearchDetailsType[] getArray() throws SQLException
  {
    return (AppSearchDetailsType[]) _array.getObjectArray(
      new AppSearchDetailsType[_array.length()]);
  }

  public AppSearchDetailsType[] getArray(long index, int count) throws SQLException
  {
    return (AppSearchDetailsType[]) _array.getObjectArray(index,
      new AppSearchDetailsType[_array.sliceLength(index, count)]);
  }

  public void setArray(AppSearchDetailsType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(AppSearchDetailsType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public AppSearchDetailsType getElement(long index) throws SQLException
  {
    return (AppSearchDetailsType) _array.getObjectElement(index);
  }

  public void setElement(AppSearchDetailsType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
