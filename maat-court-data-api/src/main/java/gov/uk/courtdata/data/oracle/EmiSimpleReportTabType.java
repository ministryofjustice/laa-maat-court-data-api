package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class EmiSimpleReportTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.EMISIMPLEREPORTTABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final EmiSimpleReportTabType _EmiSimpleReportTabTypeFactory = new EmiSimpleReportTabType();

  public static ORADataFactory getORADataFactory()
  { return _EmiSimpleReportTabTypeFactory; }
  /* constructors */
  public EmiSimpleReportTabType()
  {
    this((EmiSimpleReportType[])null);
  }

  public EmiSimpleReportTabType(EmiSimpleReportType[] a)
  {
    _array = new MutableArray(2002, a, EmiSimpleReportType.getORADataFactory());
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
    EmiSimpleReportTabType a = new EmiSimpleReportTabType();
    a._array = new MutableArray(2002, (ARRAY) d, EmiSimpleReportType.getORADataFactory());
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
  public EmiSimpleReportType[] getArray() throws SQLException
  {
    return (EmiSimpleReportType[]) _array.getObjectArray(
      new EmiSimpleReportType[_array.length()]);
  }

  public EmiSimpleReportType[] getArray(long index, int count) throws SQLException
  {
    return (EmiSimpleReportType[]) _array.getObjectArray(index,
      new EmiSimpleReportType[_array.sliceLength(index, count)]);
  }

  public void setArray(EmiSimpleReportType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(EmiSimpleReportType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public EmiSimpleReportType getElement(long index) throws SQLException
  {
    return (EmiSimpleReportType) _array.getObjectElement(index);
  }

  public void setElement(EmiSimpleReportType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
