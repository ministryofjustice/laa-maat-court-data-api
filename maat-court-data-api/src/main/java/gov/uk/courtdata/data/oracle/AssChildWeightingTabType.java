package gov.uk.courtdata.data.oracle;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableArray;
import oracle.sql.*;

import java.sql.Connection;
import java.sql.SQLException;

public class AssChildWeightingTabType implements ORAData, ORADataFactory
{
  public static final String _SQL_NAME = "TOGDATA.ASS_CHILD_WEIGHTING_TABTYPE";
  public static final int _SQL_TYPECODE = OracleTypes.ARRAY;

  MutableArray _array;

private static final AssChildWeightingTabType _AssChildWeightingTabTypeFactory = new AssChildWeightingTabType();

  public static ORADataFactory getORADataFactory()
  { return _AssChildWeightingTabTypeFactory; }
  /* constructors */
  public AssChildWeightingTabType()
  {
    this((AssessmentChildWeightingType[])null);
  }

  public AssChildWeightingTabType(AssessmentChildWeightingType[] a)
  {
    _array = new MutableArray(2002, a, AssessmentChildWeightingType.getORADataFactory());
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
    AssChildWeightingTabType a = new AssChildWeightingTabType();
    a._array = new MutableArray(2002, (ARRAY) d, AssessmentChildWeightingType.getORADataFactory());
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
  public AssessmentChildWeightingType[] getArray() throws SQLException
  {
    return (AssessmentChildWeightingType[]) _array.getObjectArray(
      new AssessmentChildWeightingType[_array.length()]);
  }

  public AssessmentChildWeightingType[] getArray(long index, int count) throws SQLException
  {
    return (AssessmentChildWeightingType[]) _array.getObjectArray(index,
      new AssessmentChildWeightingType[_array.sliceLength(index, count)]);
  }

  public void setArray(AssessmentChildWeightingType[] a) throws SQLException
  {
    _array.setObjectArray(a);
  }

  public void setArray(AssessmentChildWeightingType[] a, long index) throws SQLException
  {
    _array.setObjectArray(a, index);
  }

  public AssessmentChildWeightingType getElement(long index) throws SQLException
  {
    return (AssessmentChildWeightingType) _array.getObjectElement(index);
  }

  public void setElement(AssessmentChildWeightingType a, long index) throws SQLException
  {
    _array.setObjectElement(a, index);
  }

}
