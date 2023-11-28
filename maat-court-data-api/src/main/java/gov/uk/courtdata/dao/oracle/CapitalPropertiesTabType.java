package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CapitalPropertiesTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CAPITAL_PROPERTIES_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final CapitalPropertiesTabType _CapitalPropertiesTabTypeFactory = new CapitalPropertiesTabType();

  public static ORADataFactory getORADataFactory()
  { return _CapitalPropertiesTabTypeFactory; }
  /* constructors */
  public CapitalPropertiesTabType()
  {
    this((CapitalPropertyType[])null);
  }

  public CapitalPropertiesTabType(CapitalPropertyType[] a)
  {
    _array = new MutableArray(2002, a, CapitalPropertyType.getORADataFactory());
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
    CapitalPropertiesTabType a = new CapitalPropertiesTabType();
    a._array = new MutableArray(2002, (ARRAY) d, CapitalPropertyType.getORADataFactory());
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
  public CapitalPropertyType[] getArray() throws SQLException
  {
    return (CapitalPropertyType[]) _array.getObjectArray(
      new CapitalPropertyType[_array.length()]);
  }

  public CapitalPropertyType[] getArray(long index, int count) throws SQLException
  {
    return (CapitalPropertyType[]) _array.getObjectArray(index,
      new CapitalPropertyType[_array.sliceLength(index, count)]);
  }

  public void setArray(CapitalPropertyType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(CapitalPropertyType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public CapitalPropertyType getElement(long index) throws SQLException
  {
    return (CapitalPropertyType) _array.getObjectElement(index);
  }

  public void setElement(CapitalPropertyType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
