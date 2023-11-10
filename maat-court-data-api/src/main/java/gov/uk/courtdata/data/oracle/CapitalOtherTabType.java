package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalOtherTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CAPITAL_OTHER_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final CapitalOtherTabType _CapitalOtherTabTypeFactory = new CapitalOtherTabType();

  public static ORADataFactory getORADataFactory()
  { return _CapitalOtherTabTypeFactory; }
  /* constructors */
  public CapitalOtherTabType()
  {
    this((CapitalOtherType[])null);
  }

  public CapitalOtherTabType(CapitalOtherType[] a)
  {
    _array = new MutableArray(2002, a, CapitalOtherType.getORADataFactory());
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
    CapitalOtherTabType a = new CapitalOtherTabType();
    a._array = new MutableArray(2002, (ARRAY) d, CapitalOtherType.getORADataFactory());
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
  public CapitalOtherType[] getArray() throws SQLException
  {
    return (CapitalOtherType[]) _array.getObjectArray(
      new CapitalOtherType[_array.length()]);
  }

  public CapitalOtherType[] getArray(long index, int count) throws SQLException
  {
    return (CapitalOtherType[]) _array.getObjectArray(index,
      new CapitalOtherType[_array.sliceLength(index, count)]);
  }

  public void setArray(CapitalOtherType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(CapitalOtherType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public CapitalOtherType getElement(long index) throws SQLException
  {
    return (CapitalOtherType) _array.getObjectElement(index);
  }

  public void setElement(CapitalOtherType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
