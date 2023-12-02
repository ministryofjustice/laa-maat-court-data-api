package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class CorrespondenceTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CORRESPONDENCE_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final CorrespondenceTabType _CorrespondenceTabTypeFactory = new CorrespondenceTabType();

  public static ORADataFactory getORADataFactory()
  { return _CorrespondenceTabTypeFactory; }
  /* constructors */
  public CorrespondenceTabType()
  {
    this((CorrespondenceType[])null);
  }

  public CorrespondenceTabType(CorrespondenceType[] a)
  {
    _array = new MutableArray(2002, a, CorrespondenceType.getORADataFactory());
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
    CorrespondenceTabType a = new CorrespondenceTabType();
    a._array = new MutableArray(2002, (ARRAY) d, CorrespondenceType.getORADataFactory());
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
  public CorrespondenceType[] getArray() throws SQLException
  {
    return (CorrespondenceType[]) _array.getObjectArray(
      new CorrespondenceType[_array.length()]);
  }

  public CorrespondenceType[] getArray(long index, int count) throws SQLException
  {
    return (CorrespondenceType[]) _array.getObjectArray(index,
      new CorrespondenceType[_array.sliceLength(index, count)]);
  }

  public void setArray(CorrespondenceType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(CorrespondenceType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public CorrespondenceType getElement(long index) throws SQLException
  {
    return (CorrespondenceType) _array.getObjectElement(index);
  }

  public void setElement(CorrespondenceType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
