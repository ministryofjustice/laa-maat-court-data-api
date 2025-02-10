package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HRProgressTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_PROGRESS_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final HRProgressTabType _HRProgressTabTypeFactory = new HRProgressTabType();

  public static ORADataFactory getORADataFactory()
  { return _HRProgressTabTypeFactory; }
  /* constructors */
  public HRProgressTabType()
  {
    this((HrProgressType[])null);
  }

  public HRProgressTabType(HrProgressType[] a)
  {
    _array = new MutableArray(2002, a, HrProgressType.getORADataFactory());
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
    HRProgressTabType a = new HRProgressTabType();
    a._array = new MutableArray(2002, (ARRAY) d, HrProgressType.getORADataFactory());
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
  public HrProgressType[] getArray() throws SQLException
  {
    return (HrProgressType[]) _array.getObjectArray(
      new HrProgressType[_array.length()]);
  }

  public HrProgressType[] getArray(long index, int count) throws SQLException
  {
    return (HrProgressType[]) _array.getObjectArray(index,
      new HrProgressType[_array.sliceLength(index, count)]);
  }

  public void setArray(HrProgressType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(HrProgressType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public HrProgressType getElement(long index) throws SQLException
  {
    return (HrProgressType) _array.getObjectElement(index);
  }

  public void setElement(HrProgressType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
