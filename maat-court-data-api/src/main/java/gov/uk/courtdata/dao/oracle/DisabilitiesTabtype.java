package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DisabilitiesTabtype implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.DISABILITIES_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final DisabilitiesTabtype _DisabilitiesTabtypeFactory = new DisabilitiesTabtype();

  public static ORADataFactory getORADataFactory()
  { return _DisabilitiesTabtypeFactory; }
  /* constructors */
  public DisabilitiesTabtype()
  {
    this((DisabilityType[])null);
  }

  public DisabilitiesTabtype(DisabilityType[] a)
  {
    _array = new MutableArray(2002, a, DisabilityType.getORADataFactory());
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
    DisabilitiesTabtype a = new DisabilitiesTabtype();
    a._array = new MutableArray(2002, (ARRAY) d, DisabilityType.getORADataFactory());
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
  public DisabilityType[] getArray() throws SQLException
  {
    return (DisabilityType[]) _array.getObjectArray(
      new DisabilityType[_array.length()]);
  }

  public DisabilityType[] getArray(long index, int count) throws SQLException
  {
    return (DisabilityType[]) _array.getObjectArray(index,
      new DisabilityType[_array.sliceLength(index, count)]);
  }

  public void setArray(DisabilityType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(DisabilityType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public DisabilityType getElement(long index) throws SQLException
  {
    return (DisabilityType) _array.getObjectElement(index);
  }

  public void setElement(DisabilityType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
