package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ThirdPartyTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.THIRD_PARTY_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final ThirdPartyTabtype _ThirdPartyTabtypeFactory = new ThirdPartyTabtype();

  public static ORADataFactory getORADataFactory()
  { return _ThirdPartyTabtypeFactory; }
  /* constructors */
  public ThirdPartyTabtype()
  {
    this((ThirdPartyType[])null);
  }

  public ThirdPartyTabtype(ThirdPartyType[] a)
  {
    _array = new MutableArray(2002, a, ThirdPartyType.getORADataFactory());
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
    ThirdPartyTabtype a = new ThirdPartyTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, ThirdPartyType.getORADataFactory());
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
  public ThirdPartyType[] getArray() throws SQLException
  {
    return (ThirdPartyType[]) _array.getObjectArray(
      new ThirdPartyType[_array.length()]);
  }

  public ThirdPartyType[] getArray(long index, int count) throws SQLException
  {
    return (ThirdPartyType[]) _array.getObjectArray(index,
      new ThirdPartyType[_array.sliceLength(index, count)]);
  }

  public void setArray(ThirdPartyType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(ThirdPartyType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public ThirdPartyType getElement(long index) throws SQLException
  {
    return (ThirdPartyType) _array.getObjectElement(index);
  }

  public void setElement(ThirdPartyType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
