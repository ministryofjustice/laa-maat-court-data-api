package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssSectionSummaryTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASS_SECTION_SUMMARY_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final AssSectionSummaryTabType _AssSectionSummaryTabTypeFactory = new AssSectionSummaryTabType();

  public static ORADataFactory getORADataFactory()
  { return _AssSectionSummaryTabTypeFactory; }
  /* constructors */
  public AssSectionSummaryTabType()
  {
    this((AssSectionSummaryType[])null);
  }

  public AssSectionSummaryTabType(AssSectionSummaryType[] a)
  {
    _array = new MutableArray(2002, a, AssSectionSummaryType.getORADataFactory());
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
    AssSectionSummaryTabType a = new AssSectionSummaryTabType();
    a._array = new MutableArray(2002, (ARRAY) d, AssSectionSummaryType.getORADataFactory());
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
  public AssSectionSummaryType[] getArray() throws SQLException
  {
    return (AssSectionSummaryType[]) _array.getObjectArray(
      new AssSectionSummaryType[_array.length()]);
  }

  public AssSectionSummaryType[] getArray(long index, int count) throws SQLException
  {
    return (AssSectionSummaryType[]) _array.getObjectArray(index,
      new AssSectionSummaryType[_array.sliceLength(index, count)]);
  }

  public void setArray(AssSectionSummaryType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(AssSectionSummaryType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public AssSectionSummaryType getElement(long index) throws SQLException
  {
    return (AssSectionSummaryType) _array.getObjectElement(index);
  }

  public void setElement(AssSectionSummaryType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
