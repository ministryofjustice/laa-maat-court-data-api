package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssessmentSummaryTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASSESSMENTS_SUMMARY_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final AssessmentSummaryTabType _AssessmentSummaryTabTypeFactory = new AssessmentSummaryTabType();

  public static ORADataFactory getORADataFactory()
  { return _AssessmentSummaryTabTypeFactory; }
  /* constructors */
  public AssessmentSummaryTabType()
  {
    this((AssessmentSummaryType[])null);
  }

  public AssessmentSummaryTabType(AssessmentSummaryType[] a)
  {
    _array = new MutableArray(2002, a, AssessmentSummaryType.getORADataFactory());
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
    AssessmentSummaryTabType a = new AssessmentSummaryTabType();
    a._array = new MutableArray(2002, (ARRAY) d, AssessmentSummaryType.getORADataFactory());
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
  public AssessmentSummaryType[] getArray() throws SQLException
  {
    return (AssessmentSummaryType[]) _array.getObjectArray(
      new AssessmentSummaryType[_array.length()]);
  }

  public AssessmentSummaryType[] getArray(long index, int count) throws SQLException
  {
    return (AssessmentSummaryType[]) _array.getObjectArray(index,
      new AssessmentSummaryType[_array.sliceLength(index, count)]);
  }

  public void setArray(AssessmentSummaryType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(AssessmentSummaryType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public AssessmentSummaryType getElement(long index) throws SQLException
  {
    return (AssessmentSummaryType) _array.getObjectElement(index);
  }

  public void setElement(AssessmentSummaryType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
