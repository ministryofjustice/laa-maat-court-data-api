package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HRDetailTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_DETAIL_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final HRDetailTabType _HRDetailTabTypeFactory = new HRDetailTabType();

  public static ORADataFactory getORADataFactory()
  { return _HRDetailTabTypeFactory; }
  /* constructors */
  public HRDetailTabType()
  {
    this((HrDetailType[])null);
  }

  public HRDetailTabType(HrDetailType[] a)
  {
    _array = new MutableArray(2002, a, HrDetailType.getORADataFactory());
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
    HRDetailTabType a = new HRDetailTabType();
    a._array = new MutableArray(2002, (ARRAY) d, HrDetailType.getORADataFactory());
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
  public HrDetailType[] getArray() throws SQLException
  {
    return (HrDetailType[]) _array.getObjectArray(
      new HrDetailType[_array.length()]);
  }

  public HrDetailType[] getArray(long index, int count) throws SQLException
  {
    return (HrDetailType[]) _array.getObjectArray(index,
      new HrDetailType[_array.sliceLength(index, count)]);
  }

  public void setArray(HrDetailType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(HrDetailType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public HrDetailType getElement(long index) throws SQLException
  {
    return (HrDetailType) _array.getObjectElement(index);
  }

  public void setElement(HrDetailType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
