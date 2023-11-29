package gov.uk.courtdata.dao.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class HRSectionTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.HR_SECTION_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final HRSectionTabType _HRSectionTabTypeFactory = new HRSectionTabType();

  public static ORADataFactory getORADataFactory()
  { return _HRSectionTabTypeFactory; }
  /* constructors */
  public HRSectionTabType()
  {
    this((HrSectionType[])null);
  }

  public HRSectionTabType(HrSectionType[] a)
  {
    _array = new MutableArray(2002, a, HrSectionType.getORADataFactory());
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
    HRSectionTabType a = new HRSectionTabType();
    a._array = new MutableArray(2002, (ARRAY) d, HrSectionType.getORADataFactory());
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
  public HrSectionType[] getArray() throws SQLException
  {
    return (HrSectionType[]) _array.getObjectArray(
      new HrSectionType[_array.length()]);
  }

  public HrSectionType[] getArray(long index, int count) throws SQLException
  {
    return (HrSectionType[]) _array.getObjectArray(index,
      new HrSectionType[_array.sliceLength(index, count)]);
  }

  public void setArray(HrSectionType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(HrSectionType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public HrSectionType getElement(long index) throws SQLException
  {
    return (HrSectionType) _array.getObjectElement(index);
  }

  public void setElement(HrSectionType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
