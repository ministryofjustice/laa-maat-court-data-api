package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class MvoRegTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.MVO_REG_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final MvoRegTabtype _MvoRegTabtypeFactory = new MvoRegTabtype();

  public static ORADataFactory getORADataFactory()
  { return _MvoRegTabtypeFactory; }
  /* constructors */
  public MvoRegTabtype()
  {
    this((MvoRegType[])null);
  }

  public MvoRegTabtype(MvoRegType[] a)
  {
    _array = new MutableArray(2002, a, MvoRegType.getORADataFactory());
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
    MvoRegTabtype a = new MvoRegTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, MvoRegType.getORADataFactory());
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
  public MvoRegType[] getArray() throws SQLException
  {
    return (MvoRegType[]) _array.getObjectArray(
      new MvoRegType[_array.length()]);
  }

  public MvoRegType[] getArray(long index, int count) throws SQLException
  {
    return (MvoRegType[]) _array.getObjectArray(index,
      new MvoRegType[_array.sliceLength(index, count)]);
  }

  public void setArray(MvoRegType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(MvoRegType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public MvoRegType getElement(long index) throws SQLException
  {
    return (MvoRegType) _array.getObjectElement(index);
  }

  public void setElement(MvoRegType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
