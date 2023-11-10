package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ContributionSummaryTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.CONTRIBUTION_SUMMARY_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final ContributionSummaryTabType _ContributionSummaryTabTypeFactory = new ContributionSummaryTabType();

  public static ORADataFactory getORADataFactory()
  { return _ContributionSummaryTabTypeFactory; }
  /* constructors */
  public ContributionSummaryTabType()
  {
    this((ContributionSummaryType[])null);
  }

  public ContributionSummaryTabType(ContributionSummaryType[] a)
  {
    _array = new MutableArray(2002, a, ContributionSummaryType.getORADataFactory());
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
    ContributionSummaryTabType a = new ContributionSummaryTabType();
    a._array = new MutableArray(2002, (ARRAY) d, ContributionSummaryType.getORADataFactory());
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
  public ContributionSummaryType[] getArray() throws SQLException
  {
    return (ContributionSummaryType[]) _array.getObjectArray(
      new ContributionSummaryType[_array.length()]);
  }

  public ContributionSummaryType[] getArray(long index, int count) throws SQLException
  {
    return (ContributionSummaryType[]) _array.getObjectArray(index,
      new ContributionSummaryType[_array.sliceLength(index, count)]);
  }

  public void setArray(ContributionSummaryType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(ContributionSummaryType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public ContributionSummaryType getElement(long index) throws SQLException
  {
    return (ContributionSummaryType) _array.getObjectElement(index);
  }

  public void setElement(ContributionSummaryType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
