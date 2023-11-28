package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CMUTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CMU_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final CMUTabType _CMUTabTypeFactory = new CMUTabType();

  public static ORADataFactory getORADataFactory()
  { return _CMUTabTypeFactory; }
  /* constructors */
  public CMUTabType()
  {
    this((CmuType[])null);
  }

  public CMUTabType(CmuType[] a)
  {
    _array = new MutableArray(2002, a, CmuType.getORADataFactory());
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
    CMUTabType a = new CMUTabType();
    a._array = new MutableArray(2002, (ARRAY) d, CmuType.getORADataFactory());
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
  public CmuType[] getArray() throws SQLException
  {
    return (CmuType[]) _array.getObjectArray(
      new CmuType[_array.length()]);
  }

  public CmuType[] getArray(long index, int count) throws SQLException
  {
    return (CmuType[]) _array.getObjectArray(index,
      new CmuType[_array.sliceLength(index, count)]);
  }

  public void setArray(CmuType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(CmuType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public CmuType getElement(long index) throws SQLException
  {
    return (CmuType) _array.getObjectElement(index);
  }

  public void setElement(CmuType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
